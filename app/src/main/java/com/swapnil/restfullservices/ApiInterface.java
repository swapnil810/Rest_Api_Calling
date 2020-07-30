package com.swapnil.restfullservices;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    //This header is used to normal data sending process
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
    })

    //This header is used for @FormUrlEncoded data sending process
    /*@Headers({
            "Accept: application/json",
            "Content-Type  = application/x-www-from-urlencoded",
 })*/

    @POST("login")
    Call<ResponseBody> login(@Header("x-api-key") String xApiKey,
                             @Body HashMap<String, String> login);

    @POST("signUp")
    Call<ResponseBody> signup(@Header("x-api-key") String xApiKey,
            /*@Header("Authorization") String auth,*/
                              @Body HashMap<String, String> signup);


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

    @POST("addContact")
    Call<ResponseBody> addContact(@Header("x-api-key") String xApiKey,
                                  @Header("Authorization") String auth,
                                  @Header("User-Rallie-Id") String user_id,
                                  @Header("Auth-Token") String auth_token,
                                  @Body JsonObject jsonStr);

    @FormUrlEncoded
    @POST("CheckEcode")
    Call<ResponseBody> CheckEcodeApi(@FieldMap Map<String, String> fields);

}
