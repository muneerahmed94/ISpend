package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PurchaseItemFragment extends Fragment {

    EditText editTextItemName, editTextItemPrice;
    AutoCompleteTextView autoCompleteTextViewItemCategory;
    Button buttonPurchase;

    String[] categories = {"Food", "Entertainment", "Fashion", "Electronics", "Other"};
    UserLocalStore userLocalStore;

    String itemName, itemCategory, itemPrice, buyer;

    NavigationView navigationView;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userLocalStore = new UserLocalStore(getContext());
        View view = inflater.inflate(R.layout.fragment_purchase_item, container, false);
        view.setBackgroundColor(Color.WHITE);

        editTextItemName = (EditText) view.findViewById(R.id.editTextItemName);
        autoCompleteTextViewItemCategory = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextViewItemCategory);
        editTextItemPrice = (EditText) view.findViewById(R.id.editTextItemPrice);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.select_dialog_item, categories);
        autoCompleteTextViewItemCategory.setThreshold(1);
        autoCompleteTextViewItemCategory.setAdapter(arrayAdapter);

        buttonPurchase = (Button) view.findViewById(R.id.buttonPurchaseItem);
        buttonPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemName = editTextItemName.getText().toString();
                itemCategory = autoCompleteTextViewItemCategory.getEditableText().toString();
                itemPrice = editTextItemPrice.getText().toString();

                Purchase purchase = new Purchase(buyer, itemName, itemPrice, itemCategory);
                purchaseItem(purchase);
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
        buyer = userLocalStore.getLoggedInUser().email;
    }

    public void purchaseItem(Purchase purchase) {
        ServerRequests serverRequest = new ServerRequests(getContext());
        serverRequest.storePurchaseInBackground(purchase, new GetPurchaseCallback() {
            @Override
            public void done(Purchase returnedPurchase) {
                Toast.makeText(getContext(), "Item Purchased successfully", Toast.LENGTH_SHORT).show();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer, new HomeFragment());
                fragmentTransaction.commit();
                ((WelcomeActivity)getActivity()).getSupportActionBar().setTitle("Home");
                navigationView = (NavigationView) getActivity().findViewById(R.id.navigationView);
                navigationView.setCheckedItem(R.id.id_home);
            }
        });
    }
}
