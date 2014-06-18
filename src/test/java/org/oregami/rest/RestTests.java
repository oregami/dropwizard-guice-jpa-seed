package org.oregami.rest;

import io.dropwizard.testing.junit.DropwizardAppRule;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.oregami.dropwizard.ToDoApplication;
import org.oregami.dropwizard.ToDoConfiguration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;

public class RestTests {

    @ClassRule
    public static final DropwizardAppRule<ToDoConfiguration> RULE =
            new DropwizardAppRule<ToDoConfiguration>(ToDoApplication.class, "todo.yml");

    @BeforeClass
    public static void init() {
    	RestAssured.baseURI = "http://localhost";
    	RestAssured.port = 8080;
    	RestAssured.authentication = RestAssured.basic("username", "password");
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
		
	}
}
