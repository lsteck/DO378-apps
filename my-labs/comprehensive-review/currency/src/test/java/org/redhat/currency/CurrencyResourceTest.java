package org.redhat.currency;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.stream.Stream;

import org.hamcrest.CoreMatchers;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class CurrencyResourceTest {

    @Test
    public void testCurrencyRootEndpoint() {
    given()
    .when().get("/")
    .then()
    .statusCode(200);
    }

    @Test
    public void testCurrencyGetIDEndpoint() {
        Stream.of("AUD", "CAD", "CHF", "EUR", "GBP", "JPY", "USD")
                .forEach(ccy -> 
                RestAssured.when().get("/" + ccy)
                .then()
                .statusCode(200)
                .body("name", CoreMatchers.is(ccy)));
   }

}