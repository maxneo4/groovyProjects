
import groovy.sql.Sql

// Create a new Sql instance
def sql = Sql.newInstance("jdbc:sqlserver://localhost\\sql2019dev;DatabaseName=CIT_Bizagi_dev_1;trustServerCertificate=true;", "sa", "sa", "com.microsoft.sqlserver.jdbc.SQLServerDriver")

GroovyShell shell = new GroovyShell()
def clipBoard = shell.parse(new File('../Lib/Clipboard.groovy'))

def stringBuffer = new StringBuffer()

// Execute a SQL statement
def results = sql.eachRow("""select '0x01' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x01) +'%'
union all
select '0x02' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x02) +'%'
union all
select '0x03' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x03) +'%'
union all
select '0x04' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x04) +'%'
union all
select '0x05' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x05) +'%'
union all
select '0x06' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x06) +'%'
union all
select '0x07' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x07) +'%'
union all
select '0x08' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x08) +'%'
union all
select '0x09' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x09) +'%'
union all
select '0x0A' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x0A) +'%'
union all
select '0x0B' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x0B) +'%'
union all
select '0x0D' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x0D) +'%'
union all
select '0x0E' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x0E) +'%'
union all
select '0X0F' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0X0F) +'%'
union all
select '0x10' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x10) +'%'
union all
select '0x11' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x11) +'%'
union all
select '0x12' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x12) +'%'
union all
select '0x13' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x13) +'%'
union all
select '0x14' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x14) +'%'
union all
select '0x15' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x15) +'%'
union all
select '0x16' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x16) +'%'
union all
select '0x17' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x17) +'%'
union all
select '0x18' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x18) +'%'
union all
select '0x19' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x19) +'%'
union all
select '0x1A' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x1A) +'%'
union all
select '0x1B' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x1B) +'%'
union all
select '0x1C' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x1C) +'%'
union all
select '0x1D' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x1D) +'%'
union all
select '0x1E' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x1E) +'%'
union all
select '0x1F' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x1F) +'%'
union all
select '0x7F' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x7F) +'%'
union all
select '0x81' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x81) +'%'
union all
select '0x8D' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x8D) +'%'
union all
select '0x8F' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x8F) +'%'
union all
select '0x90' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x90) +'%'
union all
select '0x9D' as hexa ,* from batagvalue where deleted = 0
and value like '%'+ CHAR(0x9D) +'%'""", row -> {
    tagVal = row.getString("value")
    stringBuffer.append(">>")
    stringBuffer.append(tagVal)
})



// Close the connection
sql.close()

clipBoard.setToClipboard(stringBuffer.toString())
