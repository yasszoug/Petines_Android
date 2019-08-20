package com.example.rishan.rishan.Services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.rishan.rishan.Model.OrderItem;
import com.example.rishan.rishan.Model.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductsService {

    String BASE_URL = "http://10.3.4.164:8080/";

    @GET("products")
    Call<List<Product>> getProducts();


    @GET("products/{productId}")
    Call<Product> getProduct(@Path("productId") int productId);

    @POST("oitems")
    Call<OrderItem> addToCart(@Body OrderItem orderItem);

    @PUT("products")
    Call<Void> updateProducts(@Body OrderItemWrapper orderItemWrapper);
}
