package org.oregami.rest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.hamcrest.Matchers;
import org.junit.*;
import org.oregami.data.DatabaseFiller;
import org.oregami.dropwizard.ToDoAppRule;
import org.oregami.dropwizard.ToDoApplication;
import org.oregami.dropwizard.ToDoConfiguration;
import org.oregami.entities.*;
import org.oregami.util.StartHelper;

import java.util.List;

public class RestTests {

    @ClassRule
    public static final DropwizardAppRule<ToDoConfiguration> RULE =
            new ToDoAppRule(ToDoApplication.class, StartHelper.CONFIG_FILENAME_TEST);

    @BeforeClass
    public static void initClass() {
        StartHelper.init(StartHelper.CONFIG_FILENAME_TEST);
    }

    @AfterClass
    public static void finish() {
        StartHelper.getInstance(DatabaseFiller.class).dropAllData();
    }


    @Test
	public void authenticateAndAddNewTasks() {

        TaskDao taskDao = StartHelper.getInstance(TaskDao.class);

        List<Task> taskList = taskDao.findAll();
        int size = taskList.size();

		String newTaskJson = "{\"name\" : \"REST task 1\", \"description\" : \"This is a description\"}";
		String newTaskJson2 = "{\"name\" : \"REST task 2\", \"description\" : \"This is another description\"}";
		Header jsonContentHeader = new Header("Content-Type", "application/json");

        Header header = new Header("Content-Type", "application/x-www-form-urlencoded");
        Response response = RestAssured.given().formParam("username", "user1").formParam("password", "password1").header(header).request().post(RestTestHelper.URL_LOGIN);
        response.then().contentType(ContentType.JSON).statusCode(200);
        response.then().contentType(ContentType.JSON).body("token", Matchers.notNullValue());
        response.then().contentType(ContentType.JSON).body("token", Matchers.containsString("."));

        //get JsonWebToken from response:
        String token = response.body().jsonPath().get("token");
        System.out.println("token: " + token);
        Header authHeader = new Header("Authorization", "bearer " + token);

		RestAssured.given().header(jsonContentHeader).header(authHeader).request().body(newTaskJson).post(RestTestHelper.URL_TASK);
		RestAssured.given().header(jsonContentHeader).header(authHeader).request().body(newTaskJson2).post(RestTestHelper.URL_TASK);


        taskList = taskDao.findAll();
        Assert.assertEquals(size+2, taskList.size());
        response = RestAssured.get(RestTestHelper.URL_TASK);
		response.then().contentType(ContentType.JSON).body("name", Matchers.hasItems("REST task 1", "REST task 2"));
		response.then().contentType(ContentType.JSON).body("description", Matchers.hasItems("This is a description", "This is another description"));


        //Check if 2 revisions with userId and for Tasks are in the database
        RevisionEntityDao revDao = StartHelper.getInstance(RevisionEntityDao.class);
        List<CustomRevisionEntity> allRevs = revDao.findAll();

        int revisionEntitiesWithUser = 0;
        for (CustomRevisionEntity rev: allRevs) {
            if (rev.getUserId()!=null) {
                revisionEntitiesWithUser++;
                Assert.assertNotNull(rev.getEntityId());
                Assert.assertEquals(rev.getEntityDiscriminator(), TopLevelEntity.Discriminator.TASK);
            }
        }
        Assert.assertEquals(2, revisionEntitiesWithUser);

    }

  @Test
  public void authenticateSuccess() {

    Header header = new Header("Content-Type", "application/x-www-form-urlencoded");
    Response response = RestAssured.given().formParam("username", "user1").formParam("password", "password1").header(header).request().post(RestTestHelper.URL_LOGIN);
    response.then().contentType(ContentType.JSON).statusCode(200);
    response.then().contentType(ContentType.JSON).body("token", Matchers.notNullValue());
    response.then().contentType(ContentType.JSON).body("token", Matchers.containsString("."));

  }

  @Test
  public void authenticateErrorWrongPassword() {
    //wrong password => no valid status code, no token
    Header header = new Header("Content-Type", "application/x-www-form-urlencoded");
    Response response = RestAssured.given().formParam("username", "user1").formParam("password", "nonsense").header(header).request().post(RestTestHelper.URL_LOGIN);
    response.then().contentType(ContentType.JSON).statusCode(Matchers.greaterThan(399));
    response.then().contentType(ContentType.JSON).body(Matchers.isEmptyString());

  }

  @Test
  public void authenticateErrorNoUsernameOrNoPassword() {
    //wrong password => no valid status code, no token
    Header header = new Header("Content-Type", "application/x-www-form-urlencoded");
    Response response = RestAssured.given()
      .formParam("username", "user1")
      .header(header).request().post(RestTestHelper.URL_LOGIN);
    response.then().contentType(ContentType.JSON).statusCode(Matchers.greaterThan(399));
    response.then().contentType(ContentType.JSON).body(Matchers.isEmptyString());

    response = RestAssured.given()
      .formParam("password", "nonsense")
      .header(header).request().post(RestTestHelper.URL_LOGIN);
    response.then().contentType(ContentType.JSON).statusCode(Matchers.greaterThan(399));
    response.then().contentType(ContentType.JSON).body(Matchers.isEmptyString());

    response = RestAssured.given()
      .header(header).request().post(RestTestHelper.URL_LOGIN);
    response.then().contentType(ContentType.JSON).statusCode(Matchers.greaterThan(399));
    response.then().contentType(ContentType.JSON).body(Matchers.isEmptyString());

  }

}
