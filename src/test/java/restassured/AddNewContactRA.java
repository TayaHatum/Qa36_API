package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import dto.ErrorDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class AddNewContactRA {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibm9hQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjc4MTk4MzIyLCJpYXQiOjE2Nzc1OTgzMjJ9.4PMGYuikv37IplS50HSk73FveYgymx4LZCAlU9ARDYk";

    @BeforeMethod
    public void preCondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath="v1";
    }

    @Test
    public void addNewContactSuccess(){

        int i = (int) (System.currentTimeMillis()/1000);
        ContactDto contactDto = ContactDto.builder()
                .name("Maya")
                .lastName("Dow")
                .email("maya"+i+"@mail.com")
                .address("Haifa")
                .phone("8888"+i)
                .description("Friend").build();

        ContactDto contact = given()
                .header("Authorization",token)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(ContactDto.class);
        System.out.println(contact.getId());

    }
    @Test
    public void addNewContactWrongName(){

        ContactDto contactDto = ContactDto.builder()
                .lastName("Dow")
                .email("maya@mail.com")
                .address("Haifa")
                .phone("8888000000")
                .description("Friend").build();

        ErrorDto errorDto = given()
                .header("Authorization",token)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .extract()
                .response()
                .as(ErrorDto.class);
        Assert.assertTrue(errorDto.getMessage().toString().contains("name=must not be blank"));

    }
    @Test
    public void addNewContactWrongLastName(){

        ContactDto contactDto = ContactDto.builder()
                .name("Katty")
                .email("katty@mail.com")
                .address("Haifa")
                .phone("8888000001")
                .description("Friend").build();

        given()
                .header("Authorization",token)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.lastName",containsString("must not be blank"));



    }
    @Test
    public void addNewContactWrongPhone(){

        ContactDto contactDto = ContactDto.builder()
                .name("Katty")
                .lastName("Doe")
                .email("katty@mail.com")
                .address("Haifa")
                .phone("88880")
                .description("Friend").build();

        given()
                .header("Authorization",token)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.phone",containsString("Phone number must contain only digits! And length min 10, max 15!"));




    }

}
