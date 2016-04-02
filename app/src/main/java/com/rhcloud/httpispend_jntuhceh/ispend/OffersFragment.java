package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class OffersFragment extends Fragment {

    Spinner spinnerCategory;
    ListView listViewOffers;

    String json_string, JSON_STRING;
    ProgressDialog progressDialog;
    JSONArray jsonArray;
    JSONObject jsonObject;

    TextView textViewCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_offers, container, false);

        String[] categories = {"Food", "Entertainment", "Electronics", "Fashion", "Other"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, categories);

        textViewCategory = (TextView) view.findViewById(R.id.textViewCategory);
        textViewCategory.setText("Showing offers for: Food");

        spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
        spinnerCategory.setAdapter(arrayAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(), arrayAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                new BackgroundTaskOffers(arrayAdapter.getItem(position)).execute();
                textViewCategory.setText("Showing offers for: " + arrayAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listViewOffers = (ListView) view.findViewById(R.id.listViewOffers);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        return view;
    }

    class BackgroundTaskOffers extends AsyncTask<Void, Void, String>
    {
        String json_url, category;

        public BackgroundTaskOffers(String category) {
            this.category = category;
        }

        @Override
        protected void onPreExecute() {
            json_url = "https://ispend-jntuhceh.rhcloud.com/offers/json_get_data.php";
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(os, "UTF-8")));
                String data = URLEncoder.encode("Category", "UTF-8") + "=" + URLEncoder.encode(category, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_STRING = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(WelcomeActivity.this, result, Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            json_string = result;

            displayOffers(json_string, category);
        }
    }

    public void displayOffers(String json_string, String category) {

        ArrayList<String> al = new ArrayList<>();

        try {
            jsonObject = new JSONObject(json_string);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String OfferID, Category, Offer;
            while(count < jsonArray.length())
            {
                JSONObject jo = jsonArray.getJSONObject(count);

                OfferID = jo.getString("OfferID");
                Category = jo.getString("Category");
                Offer = jo.getString("Offer");

                if(Category.equals(category)) {
                    al.add(Offer);
                }

                count++;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        String[] offers = new String[al.size()];
        offers = al.toArray(offers);
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, offers);
        listViewOffers.setAdapter(adapter);
    }
}
