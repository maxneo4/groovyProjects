import oracle.sql.CLOB
import groovy.sql.Sql
import groovy.json.JsonSlurper

import groovy.xml.XmlSlurper

def xmlFile = new File("C:\\Users\\maxne\\Downloads\\SICOM_20231023_Full_Bex_ProcesoOrdendePedidoAnticipadaIndividual\\Catalog__Objects.xml")
def xmlSlurper = new XmlSlurper()
def objects = xmlSlurper.parse(xmlFile)


def sql = Sql.newInstance("jdbc:oracle:thin:@vm:1521:orcl", "Sicom", "sa", "oracle.jdbc.driver.OracleDriver")

class Entity {
    String guid
    String name
    String resolved
    Object jsonData
}

Map<String, Entity> listEntity = [:]
def json = new JsonSlurper()

sql.eachRow("select guidobject, objName, fnBA_DB_BlobToClob(objcontentresolved) as resolved from VWBA_CATALOG_BABIZAGICATALOG where objtypename = 'Entity' and deleted = 0", row ->{
        CLOB content = row.getObject('resolved')
    if(content != null) {
        def jsonVal = content.getCharacterStream().getText()
        def currentEntity = new Entity()
        currentEntity.guid = row.getObject('guidObject').toString()
        currentEntity.name = row.getString('objName')
        currentEntity.resolved = jsonVal
        currentEntity.jsonData = json.parseText(jsonVal)
        if(currentEntity.jsonData['bussinessKey']!= null)
            listEntity[currentEntity.guid] = currentEntity
    }
})

class EV {
    String guid
    Object jsonData
    EV(){

    }
    EV(String guid, Object jsonData){
        this.guid = guid
        this.jsonData = jsonData
    }
}

Map<String, EV[]> bexEvByParent = [:]

objects.children().forEach(x-> {
    if(x.Type == "Entity" &&  x.children().last().name() == "StringContentResolved"){
        def currentEntity = new Entity()
        currentEntity.guid = x.Id
        currentEntity.name = x.Name
        currentEntity.resolved = x.StringContentResolved
        currentEntity.jsonData = x.StringContentResolved
        if(currentEntity.jsonData['bussinessKey']!= null)
            listEntity[currentEntity.guid] = currentEntity
    }
    if(x.Type == "EntityValue" &&  x.children().last().name() == "StringContentResolved"){
        def newEV = new EV()
        newEV.guid = x.Id
        newEV.jsonData = x.StringContentResolved
        if(!bexEvByParent.containsKey(x.ParentId)){
            bexEvByParent[x.ParentId] = [newEV]
        }else{
            bexEvByParent[x.ParentId] += newEV
        }
    }
})
//@TODO con entSrc si es Master consultar en la tabla y armar el entityValue segun los valores el BK, como guid del atributo y attribSrc o consultar
//armando la query de groupBy para ver si tiene duplicados directamente
listEntity.values().each(e -> {
    Map<String, EV> listEntityValue = [:]
    def statment = """select guidObject, fnBA_DB_BlobToClob(objcontentresolved) as resolved from VWBA_CATALOG_BABIZAGICATALOG where objtypename = 'EntityValue' 
and deleted = 0 and guidobjectParent = '${e.guid.toString()}'""".toString()
    println("procesando la entidad ${e.name}")
    sql.eachRow(statment, row -> {
        CLOB content = row.getObject('resolved')
        def jsonVal = json.parseText(content.getCharacterStream().getText())
        def guidEv = row.getObject('guidObject').toString()
        listEntityValue[guidEv] = new EV(guidEv, jsonVal)
    })
    //add bex EntityValues
    if(bexEvByParent.containsKey(e.guid)){
        println("contains EV in bex")
        bexEvByParent[e.guid].each {
            listEntityValue[it.guid] = it
        }
    }
    println("listEntityValue.size(): ${listEntityValue.size()}")
    if(listEntityValue.size() > 0){
        def dynamicProperties = [:]
        bks = e.jsonData['bussinessKey']

        listEntityValue.values().each(ev->{
            dynamicProperties[ev.guid] = [ev.guid]
            bks.each(bk ->{
                def valFieldCol = ev.jsonData['fields'][bk['baref']['ref']]
                def val = valFieldCol != null && valFieldCol.toString().length()>0? valFieldCol : "null"
                dynamicProperties[ev.guid].add(val)
            })
        })
        println("propiedades dinamicas ${dynamicProperties}")
        def groupBy = listEntityValue.values().countBy { dynamicProperties[it.guid] }
        println("groupBy=")
        println(groupBy)
    }
})


println(listEntity.size())


sql.close()



