package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MyItemsFragment extends Fragment {

    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ItemAdapter itemAdapter;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_items, container, false);
        view.setBackgroundColor(Color.WHITE);

        json_string = getArguments().getString("json_string");
        itemAdapter = new ItemAdapter(getContext(), R.layout.row_layout);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(itemAdapter);

        Items items = new Items("Name", "Category", "Price");
        itemAdapter.add(items);

        try {
            jsonObject = new JSONObject(json_string);
            jsonArray = jsonObject.getJSONArray("server_response");

            int count = 0;
            String itemName, itemCategory, itemPrice;
            while(count < jsonArray.length())
            {
                JSONObject jo = jsonArray.getJSONObject(count);
                itemName = jo.getString("ItemName");
                itemCategory = jo.getString("ItemCategory");
                itemPrice = jo.getString("ItemPrice");
                Items item = new Items(itemName, itemCategory, itemPrice);
                itemAdapter.add(item);
                count++;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject jo = null;
                String purchaseTime = "";

                if(position > 0) {
                    try {
                        jo = jsonArray.getJSONObject(position-1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        purchaseTime = jo.getString("PurchaseTime");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getContext(), getDisplayDateTimeString(purchaseTime), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    String getDisplayDateTimeString(String inputDateTimeString) {
        String displayDateTimeString = "";

        DateFormat displayFormat = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm a");
        displayFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date inputDateTimeObject = new Date();
        try {
            inputDateTimeObject = df.parse(inputDateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        displayDateTimeString = displayFormat.format(inputDateTimeObject).toString();

        return displayDateTimeString;
    }
}
