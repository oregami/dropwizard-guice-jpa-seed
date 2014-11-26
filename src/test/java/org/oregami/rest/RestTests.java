package org.oregami.rest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.jayway.restassured.http.ContentType;
import io.dropwizard.testing.junit.DropwizardAppRule;

import org.hamcrest.Matchers;
import org.junit.*;
import org.oregami.dropwizard.ToDoApplication;
import org.oregami.dropwizard.ToDoConfiguration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;
import org.oregami.entities.*;
import org.oregami.test.DatabaseUtils;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        DatabaseUtils.clearDatabaseTables();

        TaskDao taskDao = injector.getInstance(TaskDao.class);

		String newTaskJson = "{\"name\" : \"REST task 1\", \"description\" : \"This is a description\"}";
		String newTaskJson2 = "{\"name\" : \"REST task 2\", \"description\" : \"This is another description\"}";
		Header jsonContentHeader = new Header("Content-Type", "application/json");

		RestAssured.given().header(jsonContentHeader).request().body(newTaskJson).post("/task");
		RestAssured.given().header(jsonContentHeader).request().body(newTaskJson2).post("/task");


        List<Task> taskList = taskDao.findAll();
        System.out.println("taskList.size(): " + taskList.size());
        Assert.assertEquals(2, taskList.size());
//        System.out.println("taskList: " + taskList);
        Response response = RestAssured.get("/task");
        System.out.println(response.body().prettyPrint());
		response.then().contentType(ContentType.JSON).body("name", Matchers.hasItems("REST task 1", "REST task 2"));
		response.then().contentType(ContentType.JSON).body("description", Matchers.hasItems("This is a description", "This is another description"));


        response.then().body("[0].name", Matchers.startsWith("REST task "));
		response.then().body("[1].name", Matchers.startsWith("REST task "));

//
//        DatabaseUtils.clearDatabaseTables();

	}

  @Test
  public void authenticateSuccess() {

    Header header = new Header("Content-Type", "application/x-www-form-urlencoded");
    Response response = RestAssured.given().formParam("username", "user1").formParam("password", "password1").header(header).request().post("/jwt/login");
    response.then().contentType(ContentType.JSON).statusCode(200);
    response.then().contentType(ContentType.JSON).body("token", Matchers.notNullValue());
    response.then().contentType(ContentType.JSON).body("token", Matchers.containsString("."));

  }

  @Test
  public void authenticateErrorWrongPassword() {
    //wrong password => no valid status code, no token
    Header header = new Header("Content-Type", "application/x-www-form-urlencoded");
    Response response = RestAssured.given().formParam("username", "user1").formParam("password", "nonsense").header(header).request().post("/jwt/login");
    response.then().contentType(ContentType.JSON).statusCode(Matchers.greaterThan(399));
    response.then().contentType(ContentType.JSON).body(Matchers.isEmptyString());

  }

  @Test
  public void authenticateErrorNoUsernameOrNoPassword() {
    //wrong password => no valid status code, no token
    Header header = new Header("Content-Type", "application/x-www-form-urlencoded");
    Response response = RestAssured.given()
      .formParam("username", "user1")
      //.formParam("password", "nonsense")
      .header(header).request().post("/jwt/login");
    response.then().contentType(ContentType.JSON).statusCode(Matchers.greaterThan(399));
    response.then().contentType(ContentType.JSON).body(Matchers.isEmptyString());

    response = RestAssured.given()
//      .formParam("username", "user1")
      .formParam("password", "nonsense")
      .header(header).request().post("/jwt/login");
    response.then().contentType(ContentType.JSON).statusCode(Matchers.greaterThan(399));
    response.then().contentType(ContentType.JSON).body(Matchers.isEmptyString());

    response = RestAssured.given()
//      .formParam("username", "user1")
//      .formParam("password", "nonsense")
      .header(header).request().post("/jwt/login");
    response.then().contentType(ContentType.JSON).statusCode(Matchers.greaterThan(399));
    response.then().contentType(ContentType.JSON).body(Matchers.isEmptyString());

  }

}
