dropwizard-guice-jpa-seed
=========================
This a sample REST application written in Java. It's purpose is to create a generic project that can be used as a starting point for a new project, but also for learning efforts.
- built on Dropwizard version 0.7.0
- dependency injection with Google Guice
- Hibernate / JPA 2.1 as database access framework
- HSQLDB as database
- "Session-per-HTTP-request" with Guice PersistentFilter
- suport for cross-origin resource sharing
- JPA entities with UUIDs as primary keys
- a pattern for accessing and manipulation entities with HTTP REST calls (Resource => Service => DAO => entity)
- a pattern for ServiceResult objects which contain ServiceErrorMessages (which can later be bound to web form fields in the client)
