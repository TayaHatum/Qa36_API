package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthRequestDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class RegistrationTestsRA {
    @BeforeMethod
    public void precondition(){
        RestAssured.baseURI="https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath="v1";
    }

    @Test
    public void registrationSuccess(){
        int i = new Random().nextInt(1000)+1000;
        AuthRequestDto dto = AuthRequestDto.builder().username("lis"+i+"@gmail.com").password("Aa12345$").build();

        given()
                .body(dto)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(200);
    }


    @Test
    public void registrationWrongEmail(){

        AuthRequestDto dto = AuthRequestDto.builder().username("lisgmail.com").password("Aa12345$").build();

        given()
                .body(dto)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.username",containsString("must be a well-formed email address"));
    }

    @Test
    public void registrationWrongPassword(){

        AuthRequestDto dto = AuthRequestDto.builder().username("noa@gmail.com").password("Aa123").build();

        given()
                .body(dto)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.password",containsString("At least 8 characters"));
    }

    @Test
    public void registrationDublicate(){

        AuthRequestDto dto = AuthRequestDto.builder().username("noa@gmail.com").password("Nnoa12345$").build();

        given()
                .body(dto)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(409)
                .assertThat().body("message",containsString("already exists"));
    }


}
