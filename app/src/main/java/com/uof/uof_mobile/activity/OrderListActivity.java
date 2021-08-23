package com.uof.uof_mobile.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.adapter.OrderListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderListActivity extends AppCompatActivity {
    private AppCompatImageButton ibtnOrderlistBack;
    private OrderListAdapter orderListAdapter;
    private JSONArray orderlist;


    private final String tempJson =
            "{\"response_code\": \"0011\", \"message\": " +
            "[{\"date\": \"7/28\", " +
            "\"companyname\": \"버거킹\", " +
            "\"orderlist\": [{\"name\": \"와퍼\", \"count\": 3}, {\"name\": \"통새우와퍼\", \"count\": 2}]," +
            "\"price\": 23000}]}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);

        init();
    }

    private void init(){
        ibtnOrderlistBack = findViewById(R.id.ibtn_orderlist_back);

        try {
            orderlist = new JSONObject(tempJson).getJSONArray("message");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(OrderListActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        orderListAdapter = new OrderListAdapter();
        orderListAdapter.setJson(orderlist);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_orderlist_orderlistview);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(orderListAdapter);



        ibtnOrderlistBack.setOnClickListener(view -> {
            finish();
        });

    }
}