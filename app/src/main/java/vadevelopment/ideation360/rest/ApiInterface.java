package vadevelopment.ideation360.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Administrator on 10-29-2016.
 */

public interface ApiInterface {


//    @POST("api/{email}/{password}")
//    Call<Login> registration(@Path("email") String email, @Path("password") String password);

    @FormUrlEncoded
    //@POST("reg")
    @POST("register")
    @Headers("Authorization:Basic c2FBcHA6dWpyTE9tNGVy")
    Call<Object> FirstSignUpTask(@Field("Username") String name,
                                 @Field("Email") String email,
                                 @Field("Password") String password,
                                 @Field("FirstName") String gender,
                                 @Field("LastName") String lastName,
                                 @Field("CompanyName") String compname);

    @FormUrlEncoded
    @POST("auth")
    @Headers("Access-Token: MV8xNDc4NzY1NTg3")
    Call<Object> SecondSignupTask(@Field("usuario") String email,
                                  @Field("clave") String password);


    @FormUrlEncoded
    @POST("cc")
    Call<Object> getResetPasswordApi(@Field("usuario") String email);

}
