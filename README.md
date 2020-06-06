# bundlesAPI
Implementation of the backend server (the "Service") and its API that will be consumed by the Administrator Interface in order for Bundles to be created and stored in the system’s Database.

## Description
A new Service will be developed. This service enables existing users to activate offers from a pool of pre-existing offers (from now on called Bundles). In order for these offers to become available to the users that use Vodafone’s services there will be an administrator interface available for administrators which they will use to insert new Bundles available for purchase. This administrator Interface will communicate with the backend server via a REST API that will be exposed from the Backend. 

The basic entity is Bundle and its properties are:
* **code**: Long, Primary Key – The Bundle’s code
* **name**: String, Not Null – The Bundle’s name
* **price**: float, Not Null – The Bundle’s price
* **activated**: Boolean, Not Null – This variable represents if the bundle is
activated or not
* **expiration**: Timestamp – This variable represents the expiration date
of the bundle
* **availability**: Timestamp, Not Null – This variable represents the
availability date of the bundle

The data are stored in a local **H2 database** table named “bundles” stored in the file
bundles.mv.db and properties above represent the columns of the table. User
requirements and reasonable calculations lead to the following constraints:
* Name’s length MUST be between 1 and 100 characters.
* If expiration date is before current date then bundle MUST be
deactivated.
* If availability date is after current date then bundle MUST be
deactivated.
* Expiration date MUST be after availability date.

For this reason, in addition to checking these restrictions when registering a new entry
in the database, every 30 seconds the service automatically checks these conditions
and disables as many services as needed. The file “data.sql” contains some commands
as comments which violate the restrictions. Uncomment and run the service to find
out that entering these entries fails due to restrictions.

The form of data exchanged between client and server is **JSON**. For a GET request
server returns a JSON array with bundles and their properties. In similar way post
requests send a bundle object in JSON format

# Technologies
* JAVA
* Spring Boot
* JSON
* H2 Database

# Requests

Request actions (GET, POST, PUT, DELETE) are described in “swagger.yaml” in detail.

URI  | HTTP Verbs | Action
------------- | ------------- | ------------- 
/bundles  | GET | List all bundles in ascending order by code
/bundles  | POST | Create a new bundle
/bundles/{code} | GET | Get a specific bundle
/bundles/{code} | PUT | Update a specific bundle
/bundles/sortbyname | GET | List all bundles in ascending order by name
/bundles/sortbypriceasc | GET | List all bundles in ascending order by price
/bundles/sortbypricedesc | GET | List all bundles in descending order by price

# Instructions

* Unzip and import Existing Maven Projects from “BundlesProject” folder into your IDE.
* Run BundlesProjectApplication class from /BundlesProject/src/main/java/com/restapi/BundlesProject/
* Visit http://localhost:8080/bundles to list all bundles in ascending order by code.

# H2-Console

H2 database enables the use of a console, in which transactions with the database can
be made very easily. It can be accessed by a browser using http://localhost:8080/h2-
console. This tool can be used for adding, editing or deleting bundles. Additionally, it
can sort the items (ascending or descending order) by any column. Last but not least
it can run database queries. It is a very fast and easy way to edit the database. Please
connect with the credentials shown in the following image.

Login with these credentials : `Saved Settings: Generic H2(Embedded),
Driver Class: org.h2.Driver,
JDBC URL: jdbc:h2:file:./bundles,
Username: sa`

# API Security
Ιn this implementation no security was used. In case our system needed to be secured,
then it would be related mainly to the modification of the database and not to its
access since it must be publicly accessible by the users of the services. That is why a
form of authentication should be used in users who have the ability to do POST, PUT,
DELETE requests. The main idea behind user authentication is:
* The user logs in with a POST request containing his credentials (username and
password),
* The server returns a temporary authentication token,
* The user sends the token within each HTTP request via an HTTP header,
* When the user logs out, the token is cleared.
These are the key features that could be used to identify and
authenticate the users who can process the database and generally secure the system.
