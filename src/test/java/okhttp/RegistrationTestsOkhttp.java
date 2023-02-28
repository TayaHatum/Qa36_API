package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class RegistrationTestsOkhttp {
    private final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    @Test
    public void registrationSuccess() throws IOException {
        int i = new Random().nextInt(1000)+1000;
        AuthRequestDto dto = AuthRequestDto.builder().username("lis"+i+"@gmail.com").password("Aa12345$").build();

        RequestBody body = RequestBody.create(gson.toJson(dto),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body).build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),200);
    }

    @Test
    public void registrationWrongEmail() throws IOException {

        AuthRequestDto dto = AuthRequestDto.builder().username("lisgmail.com").password("Aa12345$").build();

        RequestBody body = RequestBody.create(gson.toJson(dto),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body).build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),400);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getMessage().toString(),"{username=must be a well-formed email address}");
    }

    @Test
    public void registrationWrongPassword() throws IOException {

        AuthRequestDto dto = AuthRequestDto.builder().username("noa@gmail.com").password("Aa123").build();

        RequestBody body = RequestBody.create(gson.toJson(dto),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body).build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),400);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getMessage().toString(),"{password= At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number; Can contain special characters [@$#^&*!]}");

        Assert.assertTrue(errorDto.getMessage().toString().contains("At least 8 characters"));
    }

}
