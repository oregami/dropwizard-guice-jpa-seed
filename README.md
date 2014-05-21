dropwizard-guice-jpa-seed
=========================
This a sample REST application written in Java. It's purpose is to create a generic project that can be used as a starting point for a new project, but also for learning efforts.

- built on [Dropwizard](https://dropwizard.github.io/dropwizard/) version 0.7.0
- dependency injection with [Google Guice](https://code.google.com/p/google-guice/)
- [Hibernate](http://hibernate.org/) / JPA 2.1 as database access framework
- [HSQLDB](http://hsqldb.org/) as database
- "Session-per-HTTP-request" with Guice [PersistentFilter](https://code.google.com/p/google-guice/wiki/JPA)
- suport for [cross-origin resource sharing](http://en.wikipedia.org/wiki/Cross-origin_resource_sharing)
- JPA entities with [UUIDs](http://en.wikipedia.org/wiki/Universally_Unique_Identifier) as primary keys
- a pattern for accessing and manipulation entities with HTTP REST calls (Resource => Service => DAO => entity)
- a pattern for ServiceResult objects which contain ServiceErrorMessages (which can later be bound to web form fields in the client)

**Feel free to suggest corrections, optimizations or extensions via pull requests!**
