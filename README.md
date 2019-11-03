# Kotik Sms Delivery
## A REST API for a fictitious Sms service
### Prerequisites
* JDK 1.8 or later
* Maven 3.2 or later
### Framework
* Spring Boot 2.1.8
* H2 in-memory database
### Building and running
* clone this git repo

* to build the application and run the tests:
```
./mvnw clean package
```
* for starting the application
```
./mvnw spring-boot:run
```
Default port is 8080 to specify a different port use the syntax:
```
./mvnw spring-boot:run -Dserver.port=9090
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
  "userId": "user",
  "sourceNumber": "+00123456890",
  "destNumber": "+00123456890",
  "body": "message body"
}
```

Response:
* Status 400 bad request if validation fails
* Status 200 ok 
* Body if successful (example):
```
{
  "href": "/public/api/sms/301d401a-e9fe-46ad-88db-5e225b224506"
}
```

Example request:
```
curl -X POST -H "Content-Type: application/json" -d "{\"userId\": \"newtestuser\", \"body\": \"test\", \"sourceNumber\": \"+00117561791\", \"destNumber\": \"+00116851750\"}" "http://localhost:8083/public/api/v1/sms/create"
```

Example response:
```
{"href":"/public/api/v1/sms/8210141d-d3a7-43d0-b4ae-8bac9fe1871b"}
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
  "userId": "user",
  "sourceNumber": "+00123456890",
  "destNumber": "+00123456890",
  "body": "message body",
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

Response
```
{
    "content": [
        {"userId":"userId","sourceNumber":"+00116851750","destNumber":"+00217561791","body":"some text","state":"ACCEPTED","receptionDate":"2019-10-14T08:54","sentDate":"3000-01-08T22:00"},
        {"userId":"userId","sourceNumber":"+00116851750","destNumber":"+00217561791","body":"some text","state":"ACCEPTED","receptionDate":"2019-10-14T08:54","sentDate":"3000-01-08T22:00"},
        ..
    ],
    "next":"http://localhost/public/api/v1/sms?filter=sentDate%3E3000-01-01,sentDate%3C3000-01-10&offset=50&limit=25"],
    "prev": "http://localhost/public/api/v1/sms?filter=sentDate%3E3000-01-01,sentDate%3C3000-01-10&offset=0&limit=25"",
}
```

### Docker image
Build a docker image running the command:
```
./mvnw install dockerfile:build
```

The default image name is "kotik/smsdelivery"
To run the image is the command (port number is just an example):
```
docker run -p 8082:8082 -t kotik/smsdelivery
```
#### NB
After a test on a kubuntu machine at home I had to make these steps, you _may_ need too:
1. after cloning the repo I had to issue a chmod to use the embedded maven:
```
$ chmod +x ./mvnw
```
2. trying to generate the docker image I got a "java.io.IOException" trying to connect to http://localhost:80.
After a quick google search I found that this could (maybe obviously) depend on lack of permissions, so I found that I had to run the build command as root:
```
sudo ./mvnw install dockerfile:build
```
