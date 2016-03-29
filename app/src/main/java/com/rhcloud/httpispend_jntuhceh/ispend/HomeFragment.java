package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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

public class HomeFragment extends Fragment {

    UserLocalStore userLocalStore;
    NavigationView navigationView;
    FragmentTransaction fragmentTransaction;

    private HomeFragment myContext;
    ListView listViewData;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_home, container, false);
        view.setBackgroundColor(Color.WHITE);

        userLocalStore = new UserLocalStore(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");

        new BackgroundTaskBarGraph(userLocalStore.getLoggedInUser().email, view).execute();

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


                Value value2 = new Value("Food", hm.get("TFood"), hm.get("Food"), "3456");
                valueAdapterData.add(value2);

                Value value3 = new Value("Entertainment", hm.get("TEntertainment"), hm.get("Entertainment"), "3456");
                valueAdapterData.add(value3);

                Value value4 = new Value("Electronics", hm.get("TElectronics"), hm.get("Electronics"), "3456");
                valueAdapterData.add(value4);

                Value value5 = new Value("Fashion", hm.get("TFashion"), hm.get("Fashion"), "3456");
                valueAdapterData.add(value5);

                Value value6 = new Value("Other", hm.get("TOther"), hm.get("Other"), "3456");
                valueAdapterData.add(value6);

                Value value7 = new Value("Total", "00000", "00000", "00000");
                valueAdapterData.add(value7);

            }
            else {
                Toast.makeText(getContext(), "unable to retrieve budget", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
