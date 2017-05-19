The solution is based on four entities, **Customer**, **Address**, **Invoice** and **Client**. Those entities are mapped to their equivalent tables in a **MySQL** database with the following relationship:

  -  A Customer has One-To-Many relationship to Address
  -  A Customer has One-To-Many relationship to Invoice
  -  An Address has One-To-Many relationship to Invoice

The more important thing here is the entity **Client**. This table is use to manage the one-to-many relationship between the **Customer** and **Address** but also it handle another particular situation. The main concern is prevent to create an Invoice with a *customer_id* and *address_id* that is not reflected in the database, I mean the user is not tied to the address. With *clients* you can be sure that all the invoices created are for *customers* tied to *address*. The data stored in this table are *customer_id* and *address_id*. To prevent the situation mentioned above just need to follow the next restrictions:  

  - the primary key for *client* is *(customer_id, address_id)*
  - the table *clients* has a foreign key to *customer*
  - the table *clients* has a foreign key to *address*
  - the table *invoice* has a foreign key to *client* related to both keys *(customer_id, address_id)*
  - *address_id* is UNIQUE in the table *clients* (I asumed this restriction to prevent *address* has many *customers*)
    
Now all the *invoices's* *customer_id* and *address_id* are checked by the foreign key, so they need to be in the table *clients*, so the *customer* and the *address* are related. 

# Architecture

The project contains two layers, one to access to the data in the database, and other to define the entry point (URLs) the API has, get the data from the database based on the entry point selected, map it and return a response. 

# Data Layer
The data layer is responsible to handle all the operations regarding to get access and modify the underline data. To do this there are data access objects (DAO) that do the necessary, business logic. Each DAO correspond to the logic for the underlaying Entiry e.g. the **InvoiceDAO** is responsible to handle (access/modify) the data in the **Invoice** entity (the table **Invoice** in the database). Also the behaviour that must follow each DAO is represented in an Interface called **Repository**. 

In this particular case **Repository** only has the most basic methods to ensure the requirements for the Entities: **Address** and **Customer**, so to ensure the requirements about the **Invoice** entity there is the interface ***QueryParamsRepository**, this just extends **Repository** and add the necessary methods for the filters e.g. the method **findByQueryParams** with the parameter **customerId** to return all the invoices related to the specific customer (all the other filters are handles with overloads of this method). To Represent the data coming from the database there are classes, using **Hibernate** to handle the relationship between their.

# Business Layer
These are the controllers, just define the entry point for each request and the logic inside it. Here the controller get the data from the database, handle all the validations and map the result to create the correspond response. The controller is how decide if the response is a **200 Ok**, a **201 Created**, a **404 Not Found** or anything else. The data that is finally returned is handle by data transfer objects (DTO) given flexibility to determine what data should be available in the response. For every entity we have the equivalent DTO e.g. **InvoiceDTO** represent the data exposed for the entry point related to **Invoice**.

# Framework
The project was built using **Java** and [DropWizard](http://www.dropwizard.io/1.1.0/docs/) to create the **RESTful web wervices**. This is a **Maven** project so you will need the following requirements:

  -  **JDK** 1.8
  -  **Maven**
  -  **MySQL**


# Build and run
To build the project go to the root of the soruce code directory, you will be in *you_path_to_project*/acme. Here execute:
*mvn package*, this will build the project and create a .jar called *acme-1.0-SNAPSHOT.jar*. The project depends on a database, so I'll provide to you the script to create the database and add some data to test it. The script will create three address (just the first and third belongs to the customer with *id=1*, they are in table *clients*), two customers and two invoices (all belongs to the customer with *id=1*). One of the invoices is of type *shop*. Once you do that, everything should by ready to run the project. To run the project, stay in the same dir and execute this command:
*java -jar target/acme-1.0-SNAPSHOT.jar server config.yml* this will start the server and will give the info to know the urls for each endpoint, something like this:
    
    ....
    GET     /customers/{id} (vandebron.resources.CustomerController)
    GET     /invoices (vandebron.resources.InvoiceController)
    POST    /invoices (vandebron.resources.InvoiceController)
    GET     /invoices/{id} (vandebron.resources.InvoiceController)
    ...
The host will be *http://localhost:8080/*. In order to add some invoices you should pass a *JSON* that match the structure of **InvoiceDTO**. This is an example using *curl*:

    curl -H "Content-Type: application/json" -X POST -d '{"customerId":1,"addressId":2,"invoiceType":"AdvancePurchase","invoiceDate":"2017-05-10 00:00:00","startDate":"2017-05-10 00:00:00","endDate":"2017-06-10 00:00:00"}' http://localhost:8080/invoices

**Please notice the format of the dates, it should by: yyyy-MM-dd HH:mm:ss**

You can also create a customer with their respective address and invoices for that address, this an example:

    curl -H "Content-Type: application/json" -X POST -d '{"name": "customer_test",  "email": "test@email.com",  "addresses": [ { "name": "address_test"}], "invoices": [{"invoiceType":"AdvancePurchase" ,"invoiceDate":"2017-05-10 00:00:00","startDate":"2017-05-10 00:00:00","endDate":"2017-06-10 00:00:00"}]}' http://localhost:8080/customers    


