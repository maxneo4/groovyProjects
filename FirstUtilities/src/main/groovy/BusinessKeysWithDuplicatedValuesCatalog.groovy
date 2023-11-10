import oracle.sql.CLOB
import groovy.sql.Sql
import groovy.json.JsonSlurper

def sql = Sql.newInstance("jdbc:oracle:thin:@vm:1521:orcl", "Sicom", "sa", "oracle.jdbc.driver.OracleDriver")

class Entity {
    String guid
    String resolved
    Object jsonData
}

Entity[] listEntity = []
def json = new JsonSlurper()

sql.eachRow("select guidobject, fnBA_DB_BlobToClob(objcontentresolved) as resolved from BABIZAGICATALOG where objtypename = 'Entity' and deleted = 0", row ->{
        CLOB content = row.getObject('resolved')
    if(content != null) {
        def jsonVal = content.getCharacterStream().getText()
        def currentEntity = new Entity()
        currentEntity.guid = row.getObject('guidObject').toString()
        currentEntity.resolved = jsonVal
        currentEntity.jsonData = json.parseText(jsonVal)
        if(currentEntity.jsonData['bussinessKey']!= null)
            listEntity += currentEntity
    }
})

class EV {
    String guid
    Object jsonData
    EV(String guid, Object jsonData){
        this.guid = guid
        this.jsonData = jsonData
    }
}

listEntity.each(e -> {
    EV[] listEntityValue = []
    def statment = """select guidObject, fnBA_DB_BlobToClob(objcontentresolved) as resolved from BABIZAGICATALOG where objtypename = 'EntityValue' 
and deleted = 0 and guidobjectParent = '${e.guid.toString()}'"""
    sql.eachRow(statment, row -> {
        CLOB content = row.getObject('resolved')
        def jsonVal = json.parseText(content.getCharacterStream().getText())
        listEntityValue += new EV(row.getObject('guidObject').toString(), jsonVal)
    })
    def dynamicProperties = [:]
    bks = e.jsonData['bussinessKey']

    listEntityValue.each(ev->{
        dynamicProperties[ev.guid] = []
        bks.each(bk ->{
            dynamicProperties[ev.guid].add(ev.jsonData['fields'][bk['baref']['ref'].toString()])
        })
    })

    def groupBy = listEntityValue.countBy { dynamicProperties[it.guid] }
    if(listEntityValue.size() > 0)
        println(groupBy)
})



println(listEntity.size())


sql.close()


