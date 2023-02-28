package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class DeleteContactByIdRA {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibm9hQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjc4MjA3NzE4LCJpYXQiOjE2Nzc2MDc3MTh9.ST9O8V7t6eluzJyTV4favFLydDxaps8GXOxIRpwkU_o";
    String id;

    @BeforeMethod
    public void precondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

        int i = new Random().nextInt(1000) + 1000;
        ContactDto dto = ContactDto.builder()
                .name("Mia")
                .lastName("Dow")
                .email("mia" + i + "@mail.com")
                .phone("123412345" + i)
                .address("NY")
                .description("Friend").build();
        String message = given()
                .header("Authorization", token)
                .body(dto)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract().path("message"); // // Contact was added! ID: 5576b4a8-deed-4a73-9b49-37d8b126a8f0
        String[] all = message.split(": ");
        id = all[1];


    }

    @Test
    public void deleteByIdSuccess() {

        given()
                .header("Authorization", token)
                .when()
                .delete("contacts/" + id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", equalTo("Contact was deleted!"));
    }

    @Test
    public void deleteByIdWrongID() {

        given()
                .header("Authorization", token)
                .when()
                .delete("contacts/e2c62d43-0903-4980-aa1c-5d42fa65ee3a")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message", equalTo("Contact with id: e2c62d43-0903-4980-aa1c-5d42fa65ee3a not found in your contacts!"))
                .assertThat().body("message", containsString("not found in your contacts!"));
    }

}