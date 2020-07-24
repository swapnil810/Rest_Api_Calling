package com.swapnil.restfullservices;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
    })


    @POST("login")
    Call<ResponseBody> login(@Header("x-api-key") String xApiKey,
                             @Body HashMap<String, String> login);

    @POST("signUp")
    Call<ResponseBody> signup(@Header("x-api-key") String xApiKey,
            /*@Header("Authorization") String auth,*/
                              @Body HashMap<String, String> signup);


    @GET("logout/")
    Call<ResponseBody> logOut(@Header("x-api-key") String xApiKey,
//                              @Header("Authorization") String auth,
                              @Header("auth_token") String auth_token,
                              @Header("user_id") String user_id);

    @GET("getUserProfile/")
    Call<ResponseBody> getUserDetails(@Header("x-api-key") String xApiKey,
                                      @Header("auth_token") String auth_token,
                                      @Header("user_id") String user_id);


    @POST("#")
    Call<ResponseBody> getProductDetailsApi(@Header("x-api-key") String xApiKey,
                                            @Header("auth_token") String auth_token,
                                            @Header("user_id") String getProductDetails,
                                            @Body HashMap<String, String> measurement);





    @PUT("User/setTakeUserPicture")
    Call<ResponseBody> measurement(@Header("x-api-key") String xApiKey,
                                   @Header("auth_token") String auth_token,
                                   @Header("user_id") String user_id,
                                   @Body HashMap<String, String> measurement);


    @Multipart
    @POST("#")
    Call<ResponseBody> updateProfilePic(@Header("x-api-key") String xApiKey,
                                        @Header("Authorization") String auth,
//                                        @Header("User-Id") String userRallieId,
//                                        @Header("Auth-Token") String auth_token,
                                        @Part MultipartBody.Part file);


    @DELETE("#")
    Call<ResponseBody> delete(@Header("x-api-key") String xApiKey,
                              @Header("Authorization") String auth,
                              @Header("user_id") String userId,
                              @Header("auth_token") String authToken,
                              @Body HashMap<String, String> deleteU);

    @GET("getSwapnilDetailsById/{swapnil_id}")
    Call<ResponseBody> getSwapnilDetailsById(@Header("x-api-key") String xApiKey,
                                             @Header("Authorization") String auth,
                                             @Path("patient_id") String getPatientDetailsById);

}
