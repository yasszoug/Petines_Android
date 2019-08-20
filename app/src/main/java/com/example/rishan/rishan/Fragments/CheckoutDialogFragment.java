package com.example.rishan.rishan.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rishan.rishan.Adapters.CartAdapter;
import com.example.rishan.rishan.Adapters.CheckoutCartAdapter;
import com.example.rishan.rishan.Model.Order;
import com.example.rishan.rishan.Model.OrderItem;
import com.example.rishan.rishan.Model.Product;
import com.example.rishan.rishan.Model.User;
import com.example.rishan.rishan.R;
import com.example.rishan.rishan.Services.OrderItemService;
import com.example.rishan.rishan.Services.OrderItemWrapper;
import com.example.rishan.rishan.Services.OrderService;
import com.example.rishan.rishan.Services.ProductsService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rishan on 5/28/2018.
 */

public class CheckoutDialogFragment extends AppCompatDialogFragment {

    Double price;
    int quantity;
    String id;

    List<OrderItem> userCart;
      String username;
        int uid;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

            price= bundle.getDouble("price");
            quantity= bundle.getInt("quantity");

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.checkout_card_details, null);
        TextView quantityText= view.findViewById(R.id.checkoutQuantityText);
        final GridView checkoutItems= view.findViewById(R.id.checkoutGrid);
        TextView priceText= view.findViewById(R.id.checkoutTotalText);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");


       sharedPreferences=getActivity().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
       uid=sharedPreferences.getInt("id",0);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OrderItemService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderItemService orderItemService = retrofit.create(OrderItemService.class);
        Call<List<OrderItem>> cart = orderItemService.getCartItems(username);
        cart.enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                userCart = response.body();

                CheckoutCartAdapter cca=new CheckoutCartAdapter(getContext(), userCart);
                checkoutItems.setAdapter(cca);



            }
            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable throwable) {
                Toast.makeText(getContext(),"Error! Please try again!", Toast.LENGTH_SHORT).show();

            }
        });


            quantityText.setText(String.valueOf(quantity));
            priceText.setText("$ "+ String.valueOf(price));

        builder.setView( view)
                .setTitle("Confirm Purchase")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Date d = Calendar.getInstance().getTime();
                        Toast.makeText(getActivity(), "Products successfully purchased", Toast.LENGTH_SHORT).show();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(OrderItemService.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        OrderItemService orderItemService = retrofit.create(OrderItemService.class);
                        Call<List<OrderItem>> nestedCall = orderItemService.getCartItems(username);

                        nestedCall.enqueue(new Callback<List<OrderItem>>() {
                            @Override
                            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                                List<OrderItem> orderItems = response.body();

                                Date d = new Date();
                                User user = new User();
                                user.setUsername(username);
                                user.setUid(uid);

                                Order order = new Order();
                                order.setComment("");
                                order.setOrder_date(d.toString());
                                order.setOrderItems(orderItems);
                                order.setStatus("purchased");
                                order.setUser(user);

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(ProductsService.BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                OrderService orderService = retrofit.create(OrderService.class);

                                OrderItemWrapper orderItemWrapper = new OrderItemWrapper();
                                orderItemWrapper.setOrderItemList(userCart);

                                Call<Void> addAsOrder = orderService.orderConfirmed(username,order);

                                addAsOrder.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {


                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(ProductsService.BASE_URL)
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();

                                        ProductsService productsService = retrofit.create(ProductsService.class);

                                        OrderItemWrapper orderItemWrapper = new OrderItemWrapper();
                                        orderItemWrapper.setOrderItemList(userCart);

                                        Call<Void> updateProducts = productsService.updateProducts(orderItemWrapper);

                                        updateProducts.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {

//                                                Toast.makeText(getActivity(), quantity+" Products Purchased!", Toast.LENGTH_LONG).show();
//                                                Fragment fragment= new PurchaseHistoryFragment();
//                                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                                                ft.replace(R.id.fragment_container, fragment);
//                                                ft.commit();

                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable throwable) {
                                                System.out.println("asd");
                                            }
                                        });

//
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable throwable) {
                                        System.out.println("asd");
                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call<List<OrderItem>> call, Throwable throwable) {

                            }
                        });




                    }

                });




        return builder.create();

    }


}
