package com.example.flickrsearchengine.service;

import com.example.flickrsearchengine.Models.geo.Geo;
import com.example.flickrsearchengine.Models.info.Info;
import com.example.flickrsearchengine.Models.search.Post;
import com.example.flickrsearchengine.Models.size.SizeExample;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestInterface {
    @GET("rest")
    Call<Post> getPost(
            @Query("method") String method,
            @Query("api_key") String api_key,
            @Query("text") String safe_search,
            @Query("has_geo") int has_geo,
            @Query("per_page") int per_page,
            @Query("page") int page,
            @Query("format") String format,
            @Query("nojsoncallback") int nojsoncallback

    );
    @GET("rest")
    Call<SizeExample> getSize(
            @Query("method") String method,
            @Query("api_key") String api_key,
            @Query("photo_id") String photo_id,
            @Query("format") String format,
            @Query("nojsoncallback") int nojsoncallback
    );
    @GET("rest")
    Call<Info> getInfo(
            @Query("method") String method,
            @Query("api_key") String api_key,
            @Query("photo_id") String photo_id,
            @Query("format") String format,
            @Query("nojsoncallback") int nojsoncallback
    );
    @GET("rest")
    Call<Geo> getGeo(
            @Query("method") String method,
            @Query("api_key") String api_key,
            @Query("photo_id") String photo_id,
            @Query("format") String format,
            @Query("nojsoncallback") int nojsoncallback
    );




}
