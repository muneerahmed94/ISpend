package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
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

public class HomeFragment extends Fragment {

    UserLocalStore userLocalStore;
    NavigationView navigationView;
    FragmentTransaction fragmentTransaction;

    private HomeFragment myContext;
    ListView listViewData;

    ProgressDialog progressDialog;
    Button buttonSetupBudget;

    View viewGlobal;
    private WelcomeActivity myContext1;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    @Override
    public void onAttach(Activity activity) {
        myContext1=(WelcomeActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_home, container, false);
        viewGlobal = view;
        view.setBackgroundColor(Color.WHITE);

        userLocalStore = new UserLocalStore(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");

        new BackgroundTaskBarGraph(userLocalStore.getLoggedInUser().email, view).execute();

        drawerLayout = (DrawerLayout) myContext1.findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(myContext1, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        navigationView = (NavigationView) myContext1.findViewById(R.id.navigationView);

        buttonSetupBudget = (Button) view.findViewById(R.id.buttonSetupBudget);
        buttonSetupBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myContext1.getSupportActionBar().setTitle("Setup Budget");
                drawerLayout.closeDrawers();
                navigationView.setCheckedItem(R.id.id_setup_budget);
                new BackgroundTaskGetBudget(userLocalStore.getLoggedInUser().email).execute();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (authenticate() == true) {
            doSomething();
        }
    }

    public boolean authenticate()
    {
        if(userLocalStore.getLoggedInUser() == null)
        {
            Intent loginIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(loginIntent);
            return false;
        }
        return true;
    }

    public void doSomething() {

    }

    class BackgroundTaskBarGraph extends AsyncTask<Void, Void, HashMap<String, String>> {

        View view;
        String email;

        public BackgroundTaskBarGraph(String email, View view) {
            this.email = email;
            this.view = view;
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
                ValueAdapter valueAdapterData = new ValueAdapter(getContext(), R.layout.value_layout);

                listViewData = (ListView) view.findViewById(R.id.listViewData);
                listViewData.setAdapter(valueAdapterData);

                Value value1 = new Value("", "Budget", "Spends", "Remaining");
                valueAdapterData.add(value1);


                Value value2 = new Value("Food", hm.get("TFood"), hm.get("Food"), new Integer(Integer.parseInt(hm.get("TFood")) - Integer.parseInt(hm.get("Food"))).toString());
                valueAdapterData.add(value2);

                Value value3 = new Value("Entertainment", hm.get("TEntertainment"), hm.get("Entertainment"), new Integer(Integer.parseInt(hm.get("TEntertainment")) - Integer.parseInt(hm.get("Entertainment"))).toString());
                valueAdapterData.add(value3);

                Value value4 = new Value("Electronics", hm.get("TElectronics"), hm.get("Electronics"), new Integer(Integer.parseInt(hm.get("TElectronics")) - Integer.parseInt(hm.get("Electronics"))).toString());
                valueAdapterData.add(value4);

                Value value5 = new Value("Fashion", hm.get("TFashion"), hm.get("Fashion"), new Integer(Integer.parseInt(hm.get("TFashion")) - Integer.parseInt(hm.get("Fashion"))).toString());
                valueAdapterData.add(value5);

                Value value6 = new Value("Other", hm.get("TOther"), hm.get("Other"), new Integer(Integer.parseInt(hm.get("TOther")) - Integer.parseInt(hm.get("Other"))).toString());
                valueAdapterData.add(value6);

                Integer budgetTotal = Integer.parseInt(hm.get("TFood")) + Integer.parseInt(hm.get("TEntertainment")) + Integer.parseInt(hm.get("TElectronics")) + Integer.parseInt(hm.get("TFashion")) + Integer.parseInt(hm.get("TOther"));
                Integer spendsTotal = Integer.parseInt(hm.get("Food")) + Integer.parseInt(hm.get("Entertainment")) + Integer.parseInt(hm.get("Electronics")) + Integer.parseInt(hm.get("Fashion")) + Integer.parseInt(hm.get("Other"));

                Value value7 = new Value("Total", budgetTotal.toString(), spendsTotal.toString(), new Integer(budgetTotal - spendsTotal).toString());
                valueAdapterData.add(value7);

            }
            else {
                Toast.makeText(getContext(), "unable to retrieve budget", Toast.LENGTH_SHORT).show();
            }
        }
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
            server_url = "http://ispend-jntuhceh.rhcloud.com/budget/get_budget.php";

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
                    total = jResponse.getString("Total");

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


                FragmentManager fragManager = myContext1.getSupportFragmentManager();
                fragmentTransaction = fragManager.beginTransaction();

                myContext1.getSupportActionBar().setTitle("Setup Budget");

                SetupBudgetFragment setupBudgetFragment = new SetupBudgetFragment();
                setupBudgetFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainContainer, setupBudgetFragment);
                fragmentTransaction.commit();

            }
            else {
                Toast.makeText(getContext(), "unable to retrive budget", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
