[![Build Status](https://travis-ci.org/oregami/dropwizard-guice-jpa-seed.png)](https://travis-ci.org/oregami/dropwizard-guice-jpa-seed)

dropwizard-guice-jpa-seed
=========================
This a sample REST application written in Java. It's purpose is to create a generic project that can be used as a starting point for a new project, but also for learning efforts (I am building an open game database at www.oregami.org).

- built on [Dropwizard](https://dropwizard.github.io/dropwizard/) version 0.7.0
- dependency injection with [Google Guice](https://code.google.com/p/google-guice/) (no Spring dependencies!)
- [Hibernate](http://hibernate.org/) / JPA 2.1 as database access framework
- [HSQLDB](http://hsqldb.org/) as database
- "Session-per-HTTP-request" with Guice [PersistentFilter](https://code.google.com/p/google-guice/wiki/JPA)
- suport for [cross-origin resource sharing](http://en.wikipedia.org/wiki/Cross-origin_resource_sharing)
- JPA entities with [UUIDs](http://en.wikipedia.org/wiki/Universally_Unique_Identifier) as primary keys
- Auditing/Version control of entities with [hibernate envers](http://envers.jboss.org/)
- authentication with JSON Web Token via [dropwizard-auth-jwt](https://github.com/ToastShaman/dropwizard-auth-jwt)
- Integration tests with [rest-assured](https://code.google.com/p/rest-assured/)
- a pattern for accessing and manipulation entities with HTTP REST calls (Resource => Service => DAO => entity)
- a pattern for ServiceResult objects which contain ServiceErrorMessages (which can later be bound to web form fields in the client)

**Feel free to suggest corrections, optimizations or extensions via pull requests!**

A corresponding **JavaScript client application (AngularJS)** is available at https://github.com/oregami/angularjs-rest-client/

# system architecture

![](docs/system_architecture.png?raw=true)

# road map / things to do

* more complex entities
* hypermedia / HATEOAS


# Usage

* Start the application with the class "ToDoApplication" with the parameters "server todo.yml".

* List all tasks with:

        GET => http://localhost:8080/task

* Add a new task with:

        POST => http://localhost:8080/task

    Header:

        Content-Type:application/json

    JSON-Body e.g. :

        {"name" : "task 1", "description" : "This is a description"}

* Modify a task:

        PUT => http://localhost:8080/task/[id]

    Header:

        Content-Type:application/json
        Accept:application/json

    JSON-Body e.g.:

        {
        "id": "402880944687600101468760d9ea0000",
        "version": "0",
        "name": "task 1 with new name",
        "description": "This is an updated description",
        "finished": "false"
        }
        
* Remove a task:

        DELETE => http://localhost:8080/task/[id]
        
* List all revision numbers of a task with:

        GET => http://localhost:8080/task/[id]/revisions
        
* Show a single task with:

        GET => http://localhost:8080/task/[id]      
        
* Show a special revision of a single task with:

        GET => http://localhost:8080/task/[id]/revisions/[revisionNumber]
        
* Create JSON Web Token for authentication:
        
        POST => http://localhost:8080/jwt/login?username=[username]&password=[password]
        e.g. with user1/password1 or user2/password2

* Check if your JSON Web Token is valid for authentication:

        GET => http://localhost:8080/jwt/test
        Authentication-Header: "Bearer [token]"
        
I recommend you use the great **chrome extension [Postman](http://getpostman.com)** to make such HTTP calls!

# Data model
see http://wiki.oregami.org/display/DTA/Data+Model
