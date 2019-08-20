package com.example.rishan.rishan.Fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.rishan.rishan.Adapters.SIngleItem;
import com.example.rishan.rishan.Model.Product;
import com.example.rishan.rishan.R;
//import com.example.rishan.rishan.Services.ProdService;
import com.example.rishan.rishan.Services.ProductsService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rishan on 5/4/2018.
 */

public class HomeFragment extends Fragment {
    Context context;
    String name;
    double price;
    String image;
    int quantity;
    String selectedProduct;
    int uid;
    SIngleItem gridAdapter;
    View view;
    GridView gridView;
    List<Product> products;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.activity_home, container, false);

         gridView = view.findViewById(R.id.gridView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProductsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsService productsService = retrofit.create(ProductsService.class);
        Call<List<Product>> call = productsService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                products = response.body();
                gridAdapter = new SIngleItem(getActivity(), products);
                gridView.setAdapter(gridAdapter);


             }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridviewItemFragment f = new GridviewItemFragment();
                Product product= products.get(position);

                uid = product.getPid();
                Bundle bdl = new Bundle(4);
                bdl.putInt("uid", uid);

                f.setArguments(bdl);

                FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                ft.replace(R.id.fragment_container, f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return view;

    }

}
