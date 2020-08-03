package com.example.finalsih;

import com.example.finalsih.Models.Headlines;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("everything")
    Call<Headlines> getHeadlines(
            //@Query("country")String country,
                /*@Query("q")String goa,
                        @Query("q")String cattle,
                                @Query("q")String stray,
                                    @Query("q")String animals,
                                        @Query("q")String activist,
                                            @Query("q")String peta,
                                                */@Query("q") String blog,


            @Query("q") String animalcruelty,
            @Query("q") String cattlecare,
            @Query("apiKey") String apiKey
    );
}
