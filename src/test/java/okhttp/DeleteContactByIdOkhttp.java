package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ContactResponseDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactByIdOkhttp {
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibm9hQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjc3ODI0NjU1LCJpYXQiOjE2NzcyMjQ2NTV9.QNMIyY8IdF203gVKPJYYGNhlMNFxIAhSGx2NiTmeXvg";

    private final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    String  id ;

    @BeforeMethod
    public void precondition() throws IOException {
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

        ContactResponseDto resdto = gson.fromJson(response.body().string(), ContactResponseDto.class);
        System.out.println(resdto.getMessage()); // Contact was added! ID: 5576b4a8-deed-4a73-9b49-37d8b126a8f0

        String  message = resdto.getMessage();
        String [] all =message.split(": ");
        id = all[1];
    }

    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
                .delete()
                .addHeader("Authorization",token).build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());

        ContactResponseDto resdto =gson.fromJson(response.body().string(), ContactResponseDto.class);
        Assert.assertEquals(resdto.getMessage(),"Contact was deleted!");



    }
}