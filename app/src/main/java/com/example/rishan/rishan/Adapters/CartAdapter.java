package com.example.rishan.rishan.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rishan.rishan.Fragments.ViewCart;
import com.example.rishan.rishan.Model.Order;
import com.example.rishan.rishan.Model.OrderItem;
import com.example.rishan.rishan.Model.User;
import com.example.rishan.rishan.R;
import com.example.rishan.rishan.Services.OrderItemService;
import com.orm.SugarRecord;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rishan on 5/16/2018.
 */


public class CartAdapter extends ArrayAdapter<OrderItem> {
    Order cart;
    int totalQuantity;
    double totalPrice;
    String id;
    Context cxt;
    ViewCart viewCart = new ViewCart();
    ProgressBar pgsBar;

    public CartAdapter(@NonNull Context context, @NonNull List<OrderItem> objects) {
        super(context, R.layout.cart_item, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View view = layoutInflater.inflate(R.layout.cart_item, parent, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("uid", "");

        final Date date = Calendar.getInstance().getTime();

        final OrderItem c = getItem(position);

        TextView productName = view.findViewById(R.id.cartText);
        TextView productPrice = view.findViewById(R.id.cartPriceText);
        TextView productQuantity = view.findViewById(R.id.cartQuantity);
        ImageView productPhoto = view.findViewById(R.id.cartImage);
        Button removeButton = view.findViewById(R.id.removeButton);
        pgsBar = view.findViewById(R.id.pBar2);

        productName.setText("Item: " + c.getProduct().getProductName());
        productPrice.setText("$"  + String.valueOf(c.getQuantity()* Double.parseDouble(c.getProduct().getPrice())));
        productQuantity.setText("Quantity: " + String.valueOf(c.getQuantity()));

        Picasso.get()
                .load(c.getProduct().getProdImage())
                .placeholder(R.drawable.dress)
                .resize(200, 300)
                .into(productPhoto);

        pgsBar.setVisibility(View.GONE);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.setStatus("");
                remove(c);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(OrderItemService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                OrderItemService orderItemService = retrofit.create(OrderItemService.class);

                c.setStatus("");

                Call<OrderItem> call = orderItemService.deleteFromCart(c.getId());

                call.enqueue(new Callback<OrderItem>() {
                    @Override
                    public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                        Toast.makeText(getContext(), c.getProduct().getProductName() + " removed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<OrderItem> call, Throwable throwable) {
//                        Toast.makeText(getContext(),"Error! Please try again!", Toast.LENGTH_SHORT).show();

                    }

                });
                Toast.makeText(getContext(), c.getProduct().getProductName() + " removed!", Toast.LENGTH_SHORT).show();

            }
        });






        return view;

    }




}