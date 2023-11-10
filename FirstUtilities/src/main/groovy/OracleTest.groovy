import groovy.sql.Sql

def sql1 = Sql.newInstance("jdbc:oracle:thin:@vm:1521:orcl", "Sicom", "sa", "oracle.jdbc.driver.OracleDriver")

sql1.eachRow("SELECT * FROM Entity", row -> println(row))

sql1.close()
