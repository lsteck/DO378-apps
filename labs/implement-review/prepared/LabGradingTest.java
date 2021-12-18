package org.acme.conference.speaker;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.not;

import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;


@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@QuarkusTest
public class LabGradingTest {
    String speakerStr1;
    String speakerStr2;
    Speaker toBeRemoved;
    Speaker toRemain;

    @BeforeAll
    public void setupTests() {
        Jsonb jsonb = JsonbBuilder.create();
        Speaker speaker1 = createSpeaker("First1", "Last1", "Org1", "Bio1", "Pic1", "Tweet1");
        speakerStr1 = jsonb.toJson(speaker1);
        Speaker speaker2 = createSpeaker("First2", "Last2", "Org2", "Bio2", "Pic2", "Tweet2");
        speakerStr2 = jsonb.toJson(speaker2);
    }

    private Speaker createSpeaker(String nameFirst, String nameLast, String organization,
            String biography, String picture, String twitterHandle) {
        Speaker sp = new Speaker();
        sp.nameFirst = nameFirst;
        sp.nameLast = nameLast;
        sp.organization = organization;
        sp.biography = biography;
        sp.picture = picture;
        sp.twitterHandle = twitterHandle;
        return sp;
    }

    @Test
    @Order(1)
    public void testListEmpty() {
        ////////// we test we start with no elements and we get an empty JSON array
        given()
          .when()
            .get("/speaker")
          .then()
             .statusCode(200)
             .body(equalTo("[]"));
    }

    @Test
    @Order(2)
    public void testCreateFirstSpeaker() {
        ////////// we test that we can add a first object
        given()
            .body(speakerStr1)
            .contentType(ContentType.JSON)
          .when()
            .post("/speaker")
          .then()
            .statusCode(200)
            .body("biography", emptyOrNullString())
            .body("uuid", not(emptyOrNullString()));
    }

    @Test
    @Order(3)
    public void testDisplayPopulatedListSingleElement() {
        //////// we test that the object was added and can be displayed in a list
        given()
          .when()
            .get("/speaker")
          .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("nameFirst[0]", equalTo("First1"))
            .body("$.size()", is(1));
    }

    @Test
    @Order(4)
    public void testCreateSecondSpeaker() {
        /////////// we test that we can add a second object
        given()
            .body(speakerStr2)
            .contentType(ContentType.JSON)
          .when()
            .post("/speaker")
          .then()
            .statusCode(200)
            .body("uuid", not(emptyOrNullString()));
    }

    @Test
    @Order(5)
    public void testDisplayPopulatedListTwoElements() {
        //////////// we test that the second one was added and the list displays both
        var response = given()
          .when()
            .get("/speaker");
        response.then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("$.size()", is(2));
        List<Speaker> respList = response.getBody().jsonPath().getList(".", Speaker.class);
        toBeRemoved = respList.get(0);
        toRemain = respList.get(1);
    }

    @Test
    @Order(6)
    public void testFindUuidSuccessful() {
        ///// We test findByUuid
        given()
            .pathParam("uuid", toBeRemoved.getUuid())
          .when()
            .get("/speaker/{uuid}")
          .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("nameFirst", is(toBeRemoved.getNameFirst()));
    }

    @Test
    @Order(7)
    public void testSearchSuccessfulSingle() {
        /////// we test search and try to find First1. Should get one result
        given()
          .when()
            .get("/speaker/search?q=First1")
          .then()
            .contentType(ContentType.JSON)
            .statusCode(anyOf(is(200), is(204)))
            .body("$.size()", is(1));
    }

    @Test
    @Order(8)
    public void testSearchSuccessfulMultiple() {
        /////// we test search and try to find First. Should get two results
        given()
          .when().get("/speaker/search?q=First")
          .then()
            .contentType(ContentType.JSON)
            .statusCode(anyOf(is(200), is(204)))
            .body("$.size()", is(2));
    }

    @Test
    @Order(9)
    public void testSearchZeroResults() {
        /////// we test search and try to find First0. Should get zero results
        given()
          .when().get("/speaker/search?q=First0")
          .then()
            .contentType(ContentType.JSON)
            .statusCode(anyOf(is(200), is(204)))
            .body("$.size()", is(0));
    }

    @Test
    @Order(10)
    public void testDeleteByUuid() {
        ///////// We hope to be able to remove the Speaker
        given()
          .pathParam("uuid", toBeRemoved.getUuid())
          .when()
            .delete("/speaker/{uuid}")
          .then()
            .statusCode(anyOf(is(200), is(204)));
    }

    @Test
    @Order(11)
    public void testFindByUuidUnsuccesful() {
        /////// We test the findByUuid returns statusCode 404 after removal
        given()
          .pathParam("uuid", toBeRemoved.getUuid())
          .when()
            .get("/speaker/{uuid}")
          .then()
            .statusCode(404);
    }

    @Test
    @Order(12)
    public void testFinalState() {
        //// test that once again we get only a single element from the list method
        given()
          .when()
            .get("/speaker")
          .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("nameFirst[0]", equalTo(toRemain.getNameFirst()))
            .body("$.size()", is(1));
    }
}