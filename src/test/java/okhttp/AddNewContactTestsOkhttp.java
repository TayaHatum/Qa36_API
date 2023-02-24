package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ContactResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class AddNewContactTestsOkhttp {
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibm9hQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjc3ODI0NjU1LCJpYXQiOjE2NzcyMjQ2NTV9.QNMIyY8IdF203gVKPJYYGNhlMNFxIAhSGx2NiTmeXvg";
    private final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    @Test
    public void addNewContactSuccess() throws IOException {
        int i = new Random().nextInt(1000) + 1000;
        ContactDto dto = ContactDto.builder()
                .name("Mia")
                .lastName("Dow")
                .email("mia" + i + "@mail.com")
                .phone("123412345" + i)
                .address("NY")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(dto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();
        Response response = client.newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        ContactResponseDto resdto = gson.fromJson(response.body().string(), ContactResponseDto.class);
        System.out.println(resdto.getMessage());
        Assert.assertTrue(resdto.getMessage().contains("Contact was added"));
    }

    @Test
    public void addNewContactWrongName() throws IOException {
        ContactDto dto = ContactDto.builder()
                .lastName("Dow")
                .email("mia@mail.com")
                .phone("1234123456777")
                .address("NY")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(dto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage().toString());
        Assert.assertEquals(errorDto.getMessage().toString(),"{name=must not be blank}");


    }

    @Test
    public void addNewContactWrongEmail() throws IOException {
        ContactDto dto = ContactDto.builder()
                .name("Mia")
                .lastName("Dow")
                .email("miamail.com")
                .phone("1234123456777")
                .address("NY")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(dto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage().toString());
        Assert.assertEquals(errorDto.getMessage().toString(),"{email=must be a well-formed email address}");


    }
}
