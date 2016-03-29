package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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

/**
 * Created by Muneer on 09-03-2016.
 */
public class ServerRequests {
    ProgressDialog progressDialog;
    Context context;

    public ServerRequests(Context context)
    {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallBack)
    {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchUserDataAsyncTask(User user, GetUserCallback userCallBack)
    {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, userCallBack).execute();
    }

    public void storePurchaseInBackground(Purchase purchase, GetPurchaseCallback purchaseCallback)
    {
        progressDialog.show();
        new StorePurchaseAsyncTask(purchase, purchaseCallback).execute();
    }

    public void storeBudgetInBackground(Budget budget, GetBudgetCallback budgetCallback)
    {
        progressDialog.show();
        new StoreBudgetAsyncTask(budget, budgetCallback).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, String>
    {
        User user;
        GetUserCallback userCallBack;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected String doInBackground(Void... params)
        {
            String register_url = "http://ispend-jntuhceh.rhcloud.com/register/index.php";
            try
            {
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(os, "UTF-8")));
                String data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(user.email, "UTF-8") + "&" +
                        URLEncoder.encode("Mobile", "UTF-8") + "=" + URLEncoder.encode(user.mobile, "UTF-8") + "&" +
                        URLEncoder.encode("Name", "UTF-8") + "=" + URLEncoder.encode(user.name, "UTF-8") + "&" +
                        URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode(user.password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if(result.equals("Registration Successfull"))
            {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                userCallBack.done(null);
            }
            else
            {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            }
        }
    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback userCallBack;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(Void... params)
        {
            User returnedUser;
            String login_url = "http://ispend-jntuhceh.rhcloud.com/login/index.php";
            try
            {

                URL url = new URL(login_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("Email", user.email).appendQueryParameter("Password", user.password);
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
                    returnedUser = null;
                }
                else
                {
                    String mobile = jResponse.getString("Mobile");
                    String name = jResponse.getString("Name");

                    returnedUser = new User(user.email, mobile, name, user.password);
                }

                return returnedUser;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            userCallBack.done(returnedUser);
        }
    }

    public class StorePurchaseAsyncTask extends AsyncTask<Void, Void, String>
    {
        Purchase purchase;
        GetPurchaseCallback purchaseCallback;

        public StorePurchaseAsyncTask(Purchase purchase, GetPurchaseCallback purchaseCallback) {
            this.purchase = purchase;
            this.purchaseCallback = purchaseCallback;
        }

        @Override
        protected String doInBackground(Void... params)
        {
            String register_url = "http://ispend-jntuhceh.rhcloud.com/purchaseitem/index.php";
            try
            {
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(os, "UTF-8")));
                String data = URLEncoder.encode("Buyer", "UTF-8") + "=" + URLEncoder.encode(purchase.buyer, "UTF-8") + "&" +
                        URLEncoder.encode("ItemName", "UTF-8") + "=" + URLEncoder.encode(purchase.itemName, "UTF-8") + "&" +
                        URLEncoder.encode("ItemPrice", "UTF-8") + "=" + URLEncoder.encode(purchase.itemPrice, "UTF-8") + "&" +
                        URLEncoder.encode("ItemCategory", "UTF-8") + "=" + URLEncoder.encode(purchase.itemCategory, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if(result.equals("Purchased!"))
            {
                Toast.makeText(context, "Item purchased successfully", Toast.LENGTH_LONG).show();
                purchaseCallback.done(null);
            }
            else
            {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            }
        }
    }

    public class StoreBudgetAsyncTask extends AsyncTask<Void, Void, String> {

        Budget budget;
        GetBudgetCallback budgetCallback;

        public StoreBudgetAsyncTask(Budget budget, GetBudgetCallback budgetCallback) {
            this.budget = budget;
            this.budgetCallback = budgetCallback;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {
            String register_url = "http://ispend-jntuhceh.rhcloud.com/budget/index.php";
            try
            {
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(os, "UTF-8")));
                String data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(budget.email, "UTF-8") + "&" +
                        URLEncoder.encode("Food", "UTF-8") + "=" + URLEncoder.encode(budget.food, "UTF-8") + "&" +
                        URLEncoder.encode("Entertainment", "UTF-8") + "=" + URLEncoder.encode(budget.entertainment, "UTF-8") + "&" +
                        URLEncoder.encode("Electronics", "UTF-8") + "=" + URLEncoder.encode(budget.electronics, "UTF-8") + "&" +
                        URLEncoder.encode("Fashion", "UTF-8") + "=" + URLEncoder.encode(budget.fashion, "UTF-8") + "&" +
                        URLEncoder.encode("Other", "UTF-8") + "=" + URLEncoder.encode(budget.other, "UTF-8") + "&" +
                        URLEncoder.encode("Total", "UTF-8") + "=" + URLEncoder.encode(budget.total, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if(result.equals("Budget set")) {
                Toast.makeText(context, "Budget updated", Toast.LENGTH_LONG).show();
                budgetCallback.done(null);
            }
            else {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
