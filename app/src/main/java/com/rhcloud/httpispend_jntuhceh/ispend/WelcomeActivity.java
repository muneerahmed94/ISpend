package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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
import java.util.HashMap;

public class WelcomeActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;

    UserLocalStore userLocalStore;
    User user;
    TextView navigationDrawerHeaderName, navigationDrawerHeaderEmail;

    View navigationHeader;

    String JSON_STRING, json_string;

    String goToFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        progressDialog = new ProgressDialog(WelcomeActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");

        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, new HomeFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Home");

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.id_home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_home:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContainer, new HomeFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Home");
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_setup_budget:
                        getSupportActionBar().setTitle("Setup Budget");
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        goToFragment = "SetupBudgetFragment";
                        new BackgroundTaskGetBudget(userLocalStore.getLoggedInUser().email).execute();
                        break;

                    case R.id.id_purchase_item:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContainer, new PurchaseItemFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Purchase Item");
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_my_items:
                        new BackgroundTaskMyItems().execute();
                        getSupportActionBar().setTitle("My Items");
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_pie_chart:
                        getSupportActionBar().setTitle("Summary - Pie Chart");
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        goToFragment = "PieChartFragment";
                        new BackgroundTaskGetBudget(userLocalStore.getLoggedInUser().email).execute();
                        break;

                    case R.id.id_bar_graph:
                        getSupportActionBar().setTitle("Summary - Bar Graph");
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        new BackgroundTaskBarGraph(userLocalStore.getLoggedInUser().email).execute();
                        break;

                    case R.id.id_offers:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContainer, new OffersFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Offers");
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_logout:
                        userLocalStore.clearUserData();
                        userLocalStore.setUserLoggedIn(false);
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }

                return false;
            }
        });

        navigationHeader = navigationView.getHeaderView(0);
        navigationDrawerHeaderName = (TextView) navigationHeader.findViewById(R.id.navigationDrawerHeaderName);
        navigationDrawerHeaderEmail = (TextView) navigationHeader.findViewById(R.id.navigationDrawerHeaderEmail);
        navigationDrawerHeaderName.setText(user.name);
        navigationDrawerHeaderEmail.setText(user.email);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    class BackgroundTaskGetBudget extends AsyncTask<Void, Void, Budget> {

        String email;

        public BackgroundTaskGetBudget(String email) {
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Budget doInBackground(Void... params) {
            Budget returnedBudget;
            String server_url = "";
            switch (goToFragment) {
                case "SetupBudgetFragment":
                    server_url = "http://ispend-jntuhceh.rhcloud.com/budget/get_budget.php";
                    break;
                case "PieChartFragment":
                    server_url = "http://ispend-jntuhceh.rhcloud.com/piechart/get_budget.php";
                    break;
            }
            try
            {

                URL url = new URL(server_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("Email", email);
                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String response = IOUtils.toString(in, "UTF-8");
                JSONObject jResponse = new JSONObject(response);

                if (jResponse.length() == 0)
                {
                    returnedBudget = null;
                }
                else
                {
                    String food = jResponse.getString("Food");
                    String entertainment = jResponse.getString("Entertainment");
                    String electronics = jResponse.getString("Electronics");
                    String fashion = jResponse.getString("Fashion");
                    String other = jResponse.getString("Other");
                    String total = "";
                    switch (goToFragment) {
                        case "SetupBudgetFragment":
                            total = jResponse.getString("Total");
                            break;
                        case "PieChartFragment":
                            total = "0";
                            break;
                    }
                    returnedBudget = new Budget(email, food, entertainment, electronics, fashion, other, total);
                }

                return returnedBudget;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Budget returnedBudget) {
            progressDialog.dismiss();
            if(returnedBudget != null) {
                Bundle bundle = new Bundle();
                bundle.putString("Food", returnedBudget.food);
                bundle.putString("Entertainment", returnedBudget.entertainment);
                bundle.putString("Electronics", returnedBudget.electronics);
                bundle.putString("Fashion", returnedBudget.fashion);
                bundle.putString("Other", returnedBudget.other);
                bundle.putString("Total", returnedBudget.total);

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (goToFragment) {
                    case "SetupBudgetFragment":
                        SetupBudgetFragment setupBudgetFragment = new SetupBudgetFragment();
                        setupBudgetFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.mainContainer, setupBudgetFragment);
                        break;
                    case "PieChartFragment":
                        PieChartFragment pieChartFragment = new PieChartFragment();
                        pieChartFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.mainContainer, pieChartFragment);
                        break;
                }
                fragmentTransaction.commit();
            }
            else {
                Toast.makeText(getBaseContext(), "unable to retrive budget", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class BackgroundTaskMyItems extends AsyncTask<Void, Void, String>
    {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "https://ispend-jntuhceh.rhcloud.com/myitems/json_get_data.php";
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(os, "UTF-8")));
                String data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(userLocalStore.getLoggedInUser().email, "UTF-8");

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
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            json_string = result;
            progressDialog.dismiss();
            //Toast.makeText(WelcomeActivity.this, json_string, Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putString("json_string", json_string);
            MyItemsFragment myItemsFragment = new MyItemsFragment();
            myItemsFragment.setArguments(bundle);

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainContainer, myItemsFragment);
            fragmentTransaction.commit();
        }
    }

    class BackgroundTaskBarGraph extends AsyncTask<Void, Void, HashMap<String, String>> {

        String email;

        public BackgroundTaskBarGraph(String email) {
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... params) {
            String server_url = "http://ispend-jntuhceh.rhcloud.com/bargraph/index.php";
            HashMap<String, String> hm = new HashMap<String, String>();

            try
            {

                URL url = new URL(server_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("Email", email);
                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String response = IOUtils.toString(in, "UTF-8");
                JSONObject jResponse = new JSONObject(response);



                hm.put("Food",jResponse.getString("Food"));
                hm.put("Entertainment",jResponse.getString("Entertainment"));
                hm.put("Electronics",jResponse.getString("Electronics"));
                hm.put("Fashion",jResponse.getString("Fashion"));
                hm.put("Other",jResponse.getString("Other"));

                hm.put("TFood",jResponse.getString("TFood"));
                hm.put("TEntertainment",jResponse.getString("TEntertainment"));
                hm.put("TElectronics",jResponse.getString("TElectronics"));
                hm.put("TFashion",jResponse.getString("TFashion"));
                hm.put("TOther",jResponse.getString("TOther"));

                return hm;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hm) {
            progressDialog.dismiss();
            if(hm != null) {
                Bundle bundle = new Bundle();

                bundle.putString("Food", hm.get("Food"));
                bundle.putString("Entertainment", hm.get("Entertainment"));
                bundle.putString("Electronics", hm.get("Electronics"));
                bundle.putString("Fashion", hm.get("Fashion"));
                bundle.putString("Other", hm.get("Other"));

                bundle.putString("TFood", hm.get("TFood"));
                bundle.putString("TEntertainment", hm.get("TEntertainment"));
                bundle.putString("TElectronics", hm.get("TElectronics"));
                bundle.putString("TFashion", hm.get("TFashion"));
                bundle.putString("TOther", hm.get("TOther"));

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                BarGraphFragment barGraphFragment = new BarGraphFragment();
                barGraphFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainContainer, barGraphFragment);
                fragmentTransaction.commit();
            }
            else {
                Toast.makeText(getBaseContext(), "unable to retrive budget", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
