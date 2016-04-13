package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;

public class PurchaseItemFragment extends Fragment {

    EditText editTextItemName, editTextItemPrice;
    AutoCompleteTextView autoCompleteTextViewItemCategory;
    Button buttonPurchase;

    String[] categories = {"Food", "Entertainment", "Fashion", "Electronics", "Other"};
    UserLocalStore userLocalStore;

    String itemName, itemCategory, itemPrice, buyer;

    NavigationView navigationView;
    FragmentTransaction fragmentTransaction;

    TextView textViewItemName, textViewItemCategory, textViewItemPrice;
//    Spinner spinnerItemCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userLocalStore = new UserLocalStore(getContext());
        View view = inflater.inflate(R.layout.fragment_purchase_item, container, false);
        view.setBackgroundColor(Color.WHITE);

        textViewItemName = (TextView) view.findViewById(R.id.textViewItemName);
        textViewItemCategory = (TextView) view.findViewById(R.id.textViewItemCategory);
        textViewItemPrice = (TextView) view.findViewById(R.id.textViewItemPrice);

        editTextItemName = (EditText) view.findViewById(R.id.editTextItemName);
        autoCompleteTextViewItemCategory = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextViewItemCategory);
        editTextItemPrice = (EditText) view.findViewById(R.id.editTextItemPrice);

        final ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.select_dialog_item, categories);
        autoCompleteTextViewItemCategory.setThreshold(1);
        autoCompleteTextViewItemCategory.setAdapter(arrayAdapter);


//        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
//        spinnerItemCategory = (Spinner) view.findViewById(R.id.spinnerItemCategory);
//        spinnerItemCategory.setAdapter(spinnerAdapter);
//        spinnerItemCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), arrayAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        buttonPurchase = (Button) view.findViewById(R.id.buttonPurchaseItem);
        buttonPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemName = editTextItemName.getText().toString();
                itemCategory = autoCompleteTextViewItemCategory.getEditableText().toString();
                itemPrice = editTextItemPrice.getText().toString();

                if(validate()) {
                    Purchase purchase = new Purchase(buyer, itemName, itemPrice, itemCategory);
                    purchaseItem(purchase);
                }
            }
        });

        highlightTextViewsOnEditTextFocus();



        return view;
    }

    private void highlightTextViewsOnEditTextFocus() {
        editTextItemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewItemName.setTextColor(Color.parseColor("#689f38"));
                    textViewItemName.setTypeface(null, Typeface.BOLD);
                } else {
                    textViewItemName.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewItemName.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        autoCompleteTextViewItemCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewItemCategory.setTextColor(Color.parseColor("#689f38"));
                    textViewItemCategory.setTypeface(null, Typeface.BOLD);
                } else {
                    textViewItemCategory.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewItemCategory.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        editTextItemPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewItemPrice.setTextColor(Color.parseColor("#689f38"));
                    textViewItemPrice.setTypeface(null, Typeface.BOLD);
                } else {
                    textViewItemPrice.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewItemPrice.setTypeface(null, Typeface.NORMAL);
                }
            }
        });
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

    public boolean validate() {
        String itemName = editTextItemName.getText().toString();
        String itemCategory = autoCompleteTextViewItemCategory.getEditableText().toString();
        String itemPrice = editTextItemPrice.getText().toString();

        if(!isValidItemName(itemName)) {
            editTextItemName.setError("Enter item name");
            editTextItemName.requestFocus();
            return false;
        }

        if(!isValidItemCategory(itemCategory)) {
            autoCompleteTextViewItemCategory.setError("Enter valid item category");
            autoCompleteTextViewItemCategory.requestFocus();
            return false;
        }

        if(!isValidItemPrice(itemPrice)) {
            editTextItemPrice.setError("Enter valid item price");
            editTextItemPrice.requestFocus();
            return false;
        }

        return true;
    }

    public boolean isValidItemName(String itemName) {
        if(itemName != null && itemName.length() > 0) {
            return true;
        }
        return false;
    }

    public boolean isValidItemCategory(String itemCategory) {
        HashSet<String> hs = new HashSet<String>();
        for(String category : categories) {
            hs.add(category);
        }

        if(hs.contains(itemCategory)) {
            return true;
        }
        return false;
    }

    public boolean isValidItemPrice(String itemPrice) {
        if(itemName != null && itemName.length() > 0) {
            try {
                Float.parseFloat(itemPrice);
                return true;
            }catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
