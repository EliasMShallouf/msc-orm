# Simple Reservation System
#### An example of how to build a reservation system with Java Spring Boot as a back-end service and Thymeleaf view engine for server-side rendering as front-end with simple auth service. 
___

> ### This project addresses the following scenario
> 
>We have a simple reservation system controlled by two types of users. One is a regular user who can only browse places and reserve them for a specific period of time, and the other user is an admin who can modify everything in the system’s components in addition to modifying or deleting other users’ reservations and closing places for specific periods to prevent any user from booking them during these periods. .
<br/>The components of this system are very simple, starting with rooms which consists of places, and each place contains specific items, so the user can reserve the places that he wants for specific periods.

*** 
___The primary goal of this project was purely educational to help my undergraduate students starting their own projects using this framework.___
***

### Stack

* Clean Architecture (Onion Architecture)
* Java Spring Boot
* Hibernate ORM
* MySQL
* GitHub OAuth
* Thymeleaf
* HTML, CSS, Bootstrap
* JavaScript, JQuery

### What this project covers

* Building Web App with Spring Boot framework.
* Building REST API.
* Use Hibernate ORM to create and connect to MySQL database.
* Server side rendering with Thymeleaf.
* Simple web pages design with pure html, css, js.
* Simple OAuth service (GitHub).
* Login and Register service.
* Session storing.
* Database CRUD examples for each db tables.
* URL Redirection with payloads.
* Rule based access control (rbac).

### Installation and run project

1. Clone the repo and open it with any editor you like (for me, I'm obsessed with IntelliJ IDEA).
2. Create a new MySQL db called ```simple_reservation_system2``` or any name you like (or even any SQL based dbms, *if you change the db from mysql then you must change the driver class and dialect*).
3. If you change the db name, then open ```application.properties``` file inside ```/src/main/resources/```
4. Edit ```spring.datasource.url``` (after the last forward slash ' / ') and set the proper database name (which you previously created at step 2).
5. If you have another db user info from (root with empty password) then modify this 2 properties ```spring.datasource.username``` and ```spring.datasource.password```.
6. Go to your GitHub account and go for your account Settings > Developer Settings, Then click on OAuth Apps > New OAuth App, and fill the fields like this :
   * Application name: anything you want
   * Homepage URL: http://localhost:8080
   * Authorization callback URL: http://localhost:8080/login/oauth2/code/github
7. Now, copy your ***client id*** and ***client secret*** and replace them with the two properties inside the ```application.properties``` file which named 
```spring.security.oauth2.client.registration.github.client-id``` and
```spring.security.oauth2.client.registration.github.client-secret```.
8. Finally, just run the project and Hibernate will automatically create the tables and a new admin user with this data *(email: admin, password: admin)* to sign in and modify the app's data.

*** 
**Note:** Screenshots of the final project can be found inside ```/screenshots``` folder and the Database ERD file is in the root repo folder and called ```ERD.png```.<br/><br/>
___In the end, I hope that I have provided a clear explanation of the contents of this project and hope my code is comprehensive, easy, clear and simple and covers all the points that I have talked about.<br/>
If you have any questions, do not hesitate to contact me via [email](mailto://elias.m.shallouf@gmail.com).___
***
