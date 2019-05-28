# Save Data into Mongo collection "Employee" of mongo DB "mongo"

#Swagger Documentation:
[GET] http://localhost:8080/swagger-ui.html#

#CURD Operations

#Save
[POST] http://localhost:8080/emp
{
	"name":"Anil",
	"salary": "567.54",
	"phones": ["123456789", "987654321"]
}

#Get by Id
[GET] http://localhost:8080/emp/5cc3af3eb7cf8b245415f962

#Get all
[Get] http://localhost:8080/emp

#Delete by Id
[DELETE] http://localhost:8080/emp/5cc3af3eb7cf8b245415f962


application.properties
-----------------------
spring.data.mongodb.uri=mongodb+srv://admin:tiger@cluster0-a1qsn.mongodb.net/mongo?retryWrites=true
spring.data.mongodb.database=mongo

-- Here "mongo" is DB, "admin" username and "tiger" is password








