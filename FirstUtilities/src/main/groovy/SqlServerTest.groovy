import groovy.sql.Sql

// Create a new Sql instance
def sql = Sql.newInstance("jdbc:sqlserver://localhost\\sql2019dev;DatabaseName=CITv10b;trustServerCertificate=true;", "sa", "sa", "com.microsoft.sqlserver.jdbc.SQLServerDriver")

// Execute a SQL statement
def results = sql.eachRow("SELECT * FROM Form", row -> println(row))

// Close the connection
sql.close()
