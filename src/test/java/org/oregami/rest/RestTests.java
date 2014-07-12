package org.oregami.rest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import io.dropwizard.testing.junit.DropwizardAppRule;

import org.hamcrest.Matchers;
import org.junit.*;
import org.oregami.dropwizard.ToDoApplication;
import org.oregami.dropwizard.ToDoConfiguration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;
import org.oregami.test.DatabaseUtils;

import javax.persistence.EntityManager;

public class RestTests {

    @ClassRule
    public static final DropwizardAppRule<ToDoConfiguration> RULE =
            new DropwizardAppRule<ToDoConfiguration>(ToDoApplication.class, "todo.yml");

    private static Injector injector;

    static EntityManager entityManager = null;

    @BeforeClass
    public static void init() {
        JpaPersistModule jpaPersistModule = new JpaPersistModule(ToDoApplication.JPA_UNIT);
        injector = Guice.createInjector(jpaPersistModule);
        PersistService persistService = injector.getInstance(PersistService.class);
        persistService.start();
    	RestAssured.baseURI = "http://localhost";
    	RestAssured.port = 8080;
    	RestAssured.authentication = RestAssured.basic("username", "password");
        entityManager = injector.getInstance(EntityManager.class);
    }

    @AfterClass
    public static void finishClass() {
        DatabaseUtils.clearDatabaseTables();
    }


    @Test
	public void testAddTask() {

		String newTaskJson = "{\"name\" : \"task 1\", \"description\" : \"This is a description\"}";
		String newTaskJson2 = "{\"name\" : \"task 2\", \"description\" : \"This is another description\"}";
		Header jsonContentHeader = new Header("Content-Type", "application/json");
		RestAssured.given().header(jsonContentHeader).request().body(newTaskJson).post("/task");
		RestAssured.given().header(jsonContentHeader).request().body(newTaskJson2).post("/task");
		
		Response response = RestAssured.get("/task");
		response.then().body("name", Matchers.hasItems("task 1", "task 2"));
		response.then().body("description", Matchers.hasItems("This is a description", "This is another description"));
		
		response.then().body("[0].name", Matchers.equalTo("task 1"));
		response.then().body("[1].name", Matchers.equalTo("task 2"));

        DatabaseUtils.clearDatabaseTables();
		
	}
}
