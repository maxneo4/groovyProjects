import groovy.sql.Sql
import groovy.json.JsonSlurper
import liqp.Template
import liqp.parser.LiquidSupport

// Create a new Sql instance
def sql = Sql.newInstance("jdbc:sqlserver://localhost\\sql2019dev;DatabaseName=BPM;trustServerCertificate=true;", "sa", "sa", "com.microsoft.sqlserver.jdbc.SQLServerDriver")

class Data implements LiquidSupport {
    String guid
    int id
    String sCode
    String sComment

    @Override
    Map<String, Object> toLiquid() {
       return [guid: guid, id: id, sCode: sCode, sComment: sComment] as Map<String, Object>
    }
}
def listData = []
// Create a new JsonSlurper object
def jsonSlurper = new JsonSlurper()

// Execute a SQL statement
def results = sql.eachRow("""select guidObject, dbo.fnBA_DB_BlobToClob(objContentResolved) as resolved from BABIZAGICATALOG 
where guidObjectParent = '27d8923e-4993-419e-9082-388e42422ce6'
and objTypeName = 'EntityValue' and guidObject not in (
select guidP_VAR_CommentareSalesman from P_VAR_CommentareSalesman
) order by JSON_VALUE(dbo.fnBA_DB_BlobToClob(objContentResolved),'\$.idEntityValue')
""", row -> {
    def jsonData = jsonSlurper.parseText(row.getString('resolved'))
    def currentData = new Data()
    currentData.setGuid(row.getObject('guidObject').toString())
    currentData.setId(jsonData['idEntityValue'] as int)
    currentData.setsCode(jsonData['fields']['f5c6dd2c-d3af-4870-9e91-abb4abf012c5']?.toString())
    currentData.setsComment(jsonData['fields']['6aaf76d6-97ad-413b-8679-38fd790e64bd']?.toString())
    if(currentData.getsCode() != null || currentData.getsComment() != null)
        listData.add(currentData)
} )

// Close the connection
sql.close()

println(listData.size())

// Parse the Liquid Template template
def template = Template.parse("""{% for item in items %}
Insert into P_VAR_CommentareSalesman values({{item.id}}, 10571, '{{item.guid}}', 0, 0, '{{item.sCode}}', '{{item.sComment}}');{% endfor %}""")

def binding = [items: listData]

// Render the Liquid Template template with the binding
def renderedOutput = template.render(binding)

// Get the rendered output
println renderedOutput