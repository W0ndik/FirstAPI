package in.reqres;

import data.People;
import data.PeopleCreated;
import data.Resourse;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;
import static specification.Specification.*;

public class APITests {



    @Test
    public void firstTest(){
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .body("page", notNullValue())
                .body("per_page", notNullValue())
                .body("total", notNullValue())
                .body("total_pages", notNullValue())
                .body("data.id",not(hasItem(nullValue())))
                .body("data.first_name", hasItem("Lindsay"))
                .statusCode(200);
    }

    @Test
    public void fisrtTest2(){
        Map<String,String> requestData = new HashMap<>();
        requestData.put("name","Kirill");
        requestData.put("job","teacher");
        Response response = given()
                .contentType("application/json")
                .body(requestData)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().body()
                .statusCode(201)
                .extract().response();
        JsonPath jsonResponce = response.jsonPath();
        Assert.assertEquals(jsonResponce.get("name"), requestData.get("name"),
                "Ожидали создание пользователя с именем "+requestData.get("name")+", создался с именем: "+jsonResponce.get("name"));
        Assert.assertEquals(jsonResponce.get("job"),requestData.get("job"),
                "Ожидали создание пользователя с ролью "+requestData.get("job")+", создался с ролью: "+jsonResponce.get("job"));
    }

    @Test
    public void fisrtTest3(){
        People people = new People("Nikolay", "programmer");
        PeopleCreated peopleCreated = given()
                .contentType("application/json")
                .body(people)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(PeopleCreated.class);

        System.out.println("_______________________");
        System.out.println(peopleCreated.getCreatedAt());
    }

    @Test()
    public void fullBobrTest() {
        Resourse resourse = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().body()
                .extract().body().as(Resourse.class);
        resourse.getData().forEach(x-> System.out.println(x.getEmail()));
    }

    @Test
    public void specTest() {
        People people = new People("Anna","programmer");
        PeopleCreated peopleCreated = given()
                .spec(requestSpec())
                .when()
                .post("/api/users")
                .then()
                .log().all()
                .spec(responseSpec())
                .extract().body().as(PeopleCreated.class);
        System.out.println("__________");
        System.out.println(peopleCreated.getCreatedAt());
    }

    @Test()
    public void dtoSpecDefaultTest() {
        installSpec(requestSpec(), responseSpec());

        People people = new People("Kirill", "teacher");

        PeopleCreated peopleCreated = given()
                .body(people)
                .when()
                .post("/api/users")
                .then()
                .log().all()
                .extract().as(PeopleCreated.class);

        System.out.println(peopleCreated.getCreatedAt());

        deleteSpec();
    }























}
