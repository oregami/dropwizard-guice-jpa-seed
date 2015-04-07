package org.oregami.rest;

import com.jayway.restassured.RestAssured;

/**
 * Created by sebastian on 20.02.15.
 */
public class RestTestHelper {

    public static final String URL_LOGIN = "/jwt/login";
    public static final String URL_SECURED = "/jwt/secured";
    public static final String URL_TASK = "/task";
    public static final String URL_REVISIONS = "/revisions";

    public static void initRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8180;
    }
}
