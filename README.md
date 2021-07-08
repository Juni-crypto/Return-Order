Order Return Management -Enhancement Project 

> Create Database -Return Order

    create database return_order

> Create Two tables for Schema Validation

    create table Authentication_request(user_name varchar(50) null,password varchar(50) null);
    create table usercred (username varchar(50) null,password varchar(50) null);

> Insert the entry user credentials

    insert into Authentication_request (user_name, password) values ('juno', 'juno');
    insert into usercred  (username, password) values ('juno', 'juno');
 

> Connect the database as per your ID and Password in Each Application.Properties

 - Run All the Application in Spring-Boot and u can find the Login page under -8100/portal/login
 
|TeamMembers|Module Worked On|
|--|--|
|Karthik Bharathi S N  | Component Processing Module |
|  Shreya Bhattacharya|Return Order Portal Module  |
| Hemachander R | Authorisation Module |
|Archana Kumari  |Payment Module  |
| Ayush Patel  | Packaging and Delivery Module |

