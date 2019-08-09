package com.cpr.ads.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Khởi tạo Retrofit (Singleton pattern)
 */
public class RetrofitClientInstance {
    private static Retrofit sRetrofit;
    private static final String BASE_URL = "http://cprcorp.com/cprsoftware/";

    /**
     * Khởi tạo Retrofit
     *
     * @return retrofit
     */
    public static Retrofit getRetrofitInstance() {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }
}
