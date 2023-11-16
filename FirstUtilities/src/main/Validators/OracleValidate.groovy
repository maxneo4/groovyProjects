
import groovy.sql.Sql

def sql1 = Sql.newInstance("jdbc:oracle:thin:@vm:1521:orcl", "Sicom", "sa", "oracle.jdbc.driver.OracleDriver")

sql1.eachRow("SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'TOAD_PLAN_TABLE'", row -> {
    if(row.getInt(1) > 0)
        println("You must to delete table TOAD_PLAN_TABLE")
})

sql1.close()
