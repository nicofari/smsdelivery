# Kotik Sms Delivery
## A REST API for a fictitious Sms service
### Prerequisites
* JDK 1.8 or later
* Maven 3.2 or later
### Framework
* Spring Boot 2.1.8
### Building and running
* clone this git repo

* to build the application and run the tests:
```
mvn clean package
```
* for starting the application
```
mvn spring-boot:run
```
Default port is 8080 to specify a different port use the syntax:
```
mvn spring-boot:run -Dserver.port=9090
```
### API documentation

For every API endpoint the response has always the Content-Type: application/json; charset=UTF-8 attribute.

#### Create Sms
Request
* Method: **POST**
* Url: /public/api/sms/create
* Headers: Content-Type: application/json; charset=UTF-8
* Body (example):
```
{
  userId: "user",
  sourceNumber: "+00123456890",
  destNumber: "+00123456890",
  body: "message body"
}
```

Response:
* Status 400 bad request if validation fails
* Status 200 ok 
* Body if successful (example):
```
{
  href: "/public/api/sms/301d401a-e9fe-46ad-88db-5e225b224506"
}
```
#### Get Sms
Request
* Method: **GET**
* Url: /public/api/sms/{smsid}

Response:
* Status 404 not found in case of invalid id
* Status 200 ok 
* Body if successful (example):
```
{
  userId: "user",
  sourceNumber: "+00123456890",
  destNumber: "+00123456890",
  body: "message body",
  ...
}
```
#### Send Sms
Request
* Method: **PUT**
* Url: /public/api/sms/{smsid}/send

Response:
* Status 404 not found in case of invalid id
* Status 422 unprocessable entity if sms in a wrong state
* Status 200 ok 

#### Delete Sms
Request
* Method: **DELETE**
* Url: /public/api/sms/{smsid}

Response:
* Status 404 not found in case of invalid id
* Status 422 unprocessable entity if sms in a wrong state
* Status 200 ok 

#### Search Sms
Request
* Method: **GET**
* Url: /public/api/sms?offset=x&limit=25&sort=receptionDate;DESC&search=

Response:
* Status 200 ok 

This API supports pagination, sorting and filtering. 
The client can control pagination sending _offset_ and _limit_ query parameters.

For sorting the syntax is [fieldname';'[ASC|DESC]] to specify field and sort direction respectively.

If nothing is specified the default order is receptionDate;ASC (ie sort by receptionDate ascending).

More than one field can be specified, separated by comma.

For searching/filtering the syntax is:

fieldName operator value 

Supported operators are
  
* < less than or equal to
* \> greater than or equal to 
* : like





