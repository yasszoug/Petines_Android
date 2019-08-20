package com.example.rishan.rishan.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rishan.rishan.Adapters.PurchaseHistoryAdapter;
import com.example.rishan.rishan.Model.Order;
import com.example.rishan.rishan.Model.OrderItem;
import com.example.rishan.rishan.Model.User;
import com.example.rishan.rishan.R;
import com.example.rishan.rishan.Services.OrderItemService;
import com.example.rishan.rishan.Services.OrderService;
import com.example.rishan.rishan.Services.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rishan on 5/24/2018.
 */

public class PurchaseHistoryFragment extends Fragment {

    String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        inflater = LayoutInflater.from(getContext());
        inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.purchase_history, container, false);
        getActivity().setTitle("Orders");


        final ListView listView = view.findViewById(R.id.purchaseList);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString("username", "");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final OrderItemService orderItem = retrofit.create(OrderItemService.class);

        Call<List<OrderItem>> call = orderItem.getPurchasedItems(username);

        call.enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                final List<OrderItem> orderItems = response.body();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UserService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                OrderService orderService = retrofit.create(OrderService.class);
                Call<List<Order>> call1 = orderService.getPurcahsedItems(username);

                call1.enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        List<Order> orders = response.body();
                        List<String> dates = new ArrayList<String>();

                        if(orders != null) {
                            for (Order o: orders) {
                                dates.add(o.getOrder_date());
                            }


                            PurchaseHistoryAdapter piAdapter = new PurchaseHistoryAdapter(getContext(), orderItems, dates);
                            listView.setAdapter(piAdapter);
                        }



                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable throwable) {
                        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();

            }
        });





        return view;

    }
}