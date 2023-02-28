package restassured;

import com.jayway.restassured.RestAssured;
import dto.AllContactsDto;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetAllContactsTestsRA {
    String token="eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibm9hQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjc4MjA3NzE4LCJpYXQiOjE2Nzc2MDc3MTh9.ST9O8V7t6eluzJyTV4favFLydDxaps8GXOxIRpwkU_o";

    @BeforeMethod
    public void precondition(){
        RestAssured.baseURI="https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath="v1";
    }

    @Test
    public void getAllContactsSuccess(){
       AllContactsDto all= given()
                .header("Authorization",token)
                .when()
                .get("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(AllContactsDto.class);
        List<ContactDto> contacts = all.getContacts();
        for (ContactDto contactDto:contacts){
            System.out.println(contactDto.getId());
            System.out.println("****");
        }




    }


    @Test
    public void getAllContactsNegative(){
        given()
                .header("Authorization","hgty6")
                .when()
                .get("contacts")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("error",equalTo("Unauthorized"));


        }
//
//
//    e2c62d43-0903-4980-aa1c-5d42fa65ee3a
//****
//    da7e985c-39e6-4699-8cef-df46e510b495
//****
//    a250341a-ee0c-4b78-8f94-e2a7c7b80d73
//****
//    b66f367f-18c3-4d4d-9dc0-d5527e65759c
//****
//        4438e87a-9bfa-4930-8ca6-54e4b856f2a0
//****
//        23a4178a-44e8-4e45-b13a-984213bc9b97
//****
//        5576b4a8-deed-4a73-9b49-37d8b126a8f0
//****
//        6d55916a-de80-4557-8ab1-89f5bf2603fa
//****
//        5ce8f6ed-3f62-447e-a870-bc13c41addd0

}
