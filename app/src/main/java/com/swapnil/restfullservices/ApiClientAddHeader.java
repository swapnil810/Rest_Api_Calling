package com.swapnil.restfullservices;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.swapnil.utills.MyConstants;
import java.io.IOException;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class ApiClientAddHeader {
    public static final String base_url = "#";  //client's url

    static final OkHttpClient okhttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request newRequest = chain.request().newBuilder()
                    .addHeader(MyConstants.HEADER_TIME_ZONE, TimeZone.getDefault().getID())
                    .build();
            return chain.proceed(newRequest);
        }
    }).readTimeout(5000, TimeUnit.SECONDS)
            .writeTimeout(5000, TimeUnit.SECONDS)
            .connectTimeout(5000, TimeUnit.SECONDS)
            .addNetworkInterceptor(new StethoInterceptor()).build();

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url).client(okhttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}

