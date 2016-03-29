package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetupBudgetFragment extends Fragment {

    EditText editTextFood, editTextEntertainment, editTextElectronics, editTextFashion, editTextOther;
    TextView textViewFood, textViewEntertainment, textViewElectronics, textViewFashion, textViewOther;
    Float floatFood, floatEntertainment, floatElectronics, floatFashion, floatOther;

    TextView textViewTotalBudget;
    Button buttonSetupBudget;
    String stringFood, stringEntertainment, stringElectronics, stringFashion, stringOther, stringTotal;
    UserLocalStore userLocalStore;

    NavigationView navigationView;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setup_budget, container, false);
        view.setBackgroundColor(Color.WHITE);
        userLocalStore = new UserLocalStore(getContext());

        stringFood = getArguments().getString("Food");
        stringEntertainment = getArguments().getString("Entertainment");
        stringElectronics = getArguments().getString("Electronics");
        stringFashion = getArguments().getString("Fashion");
        stringOther = getArguments().getString("Other");
        stringTotal = getArguments().getString("Total");

        textViewFood = (TextView) view.findViewById(R.id.textViewFood);
        textViewEntertainment = (TextView) view.findViewById(R.id.textViewEntertainment);
        textViewElectronics = (TextView) view.findViewById(R.id.textViewElectronics);
        textViewFashion = (TextView) view.findViewById(R.id.textViewFashion);
        textViewOther = (TextView) view.findViewById(R.id.textViewOther);

        editTextFood = (EditText) view.findViewById(R.id.editTextFood);
        editTextEntertainment = (EditText) view.findViewById(R.id.editTextEntertainment);
        editTextElectronics = (EditText) view.findViewById(R.id.editTextElectronics);
        editTextFashion = (EditText) view.findViewById(R.id.editTextFashion);
        editTextOther = (EditText) view.findViewById(R.id.editTextOther);
        textViewTotalBudget = (TextView)view.findViewById(R.id.textViewTotalBudget);

        editTextFood.setText(stringFood);
        editTextEntertainment.setText(stringEntertainment);
        editTextElectronics.setText(stringElectronics);
        editTextFashion.setText(stringFashion);
        editTextOther.setText(stringOther);
        editTextFood.setText(stringFood);
        textViewTotalBudget.setText("Total Budget: " + stringTotal);

        buttonSetupBudget = (Button) view.findViewById(R.id.buttonSetupBudget);
        buttonSetupBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringFood = editTextFood.getText().toString();
                stringEntertainment = editTextEntertainment.getText().toString();
                stringElectronics = editTextElectronics.getText().toString();
                stringFashion = editTextFashion.getText().toString();
                stringOther = editTextOther.getText().toString();

                Integer total = Integer.parseInt(stringFood) + Integer.parseInt(stringEntertainment) + Integer.parseInt(stringElectronics) + Integer.parseInt(stringFashion) + Integer.parseInt(stringOther);
                String stringTotal = total.toString();

                Budget budget = new Budget(userLocalStore.getLoggedInUser().email, stringFood, stringEntertainment, stringElectronics, stringFashion, stringOther, stringTotal);
                storeBudget(budget);
                textViewTotalBudget.setText("Total Budget: " + stringTotal);
            }
        });

        highlightTextViewsOnEditTextFocus();
        onValueChangedEditTexts();

        return view;
    }

    private void onValueChangedEditTexts() {

        editTextFood.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(editTextFood.getText().toString().length() > 0) {
                    updateTotalBudget();
                }
                else {
                    updateTotalBudget();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        editTextEntertainment.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(editTextEntertainment.getText().toString().length() > 0) {
                    updateTotalBudget();
                }
                else {
                    updateTotalBudget();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        editTextElectronics.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(editTextElectronics.getText().toString().length() > 0) {
                    updateTotalBudget();
                }
                else {
                    updateTotalBudget();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        editTextFashion.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(editTextFashion.getText().toString().length() > 0) {
                    updateTotalBudget();
                }
                else {
                    updateTotalBudget();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        editTextOther.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(editTextOther.getText().toString().length() > 0) {
                    updateTotalBudget();
                }
                else {
                    updateTotalBudget();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void updateTotalBudget() {
        readInput();
        String res = "Total Budget: " + new Float(floatFood + floatEntertainment + floatElectronics + floatFashion + floatOther).toString();
        textViewTotalBudget.setText(res);
    }

    private void readInput() {
        if(editTextFood.getText().toString().length() > 0)
            floatFood = Float.parseFloat(editTextFood.getText().toString());
        else
            floatFood = (float)0;

        if(editTextEntertainment.getText().toString().length() > 0)
            floatEntertainment = Float.parseFloat(editTextEntertainment.getText().toString());
        else
            floatEntertainment = (float)0;

        if(editTextElectronics.getText().toString().length() > 0)
            floatElectronics = Float.parseFloat(editTextElectronics.getText().toString());
        else
            floatElectronics = (float)0;

        if(editTextFashion.getText().toString().length() > 0)
            floatFashion = Float.parseFloat(editTextFashion.getText().toString());
        else
            floatFashion = (float)0;

        if(editTextOther.getText().toString().length() > 0)
            floatOther = Float.parseFloat(editTextOther.getText().toString());
        else
            floatOther = (float)0;
    }

    private void highlightTextViewsOnEditTextFocus() {
        editTextFood.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewFood.setTextColor(Color.parseColor("#009688"));
                    textViewFood.setTypeface(null, Typeface.BOLD);
                } else {
                    textViewFood.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewFood.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        editTextEntertainment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    textViewEntertainment.setTextColor(Color.parseColor("#009688"));
                    textViewEntertainment.setTypeface(null,Typeface.BOLD);
                }
                else {
                    textViewEntertainment.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewEntertainment.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        editTextElectronics.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    textViewElectronics.setTextColor(Color.parseColor("#009688"));
                    textViewElectronics.setTypeface(null,Typeface.BOLD);
                }
                else {
                    textViewElectronics.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewElectronics.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        editTextFashion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    textViewFashion.setTextColor(Color.parseColor("#009688"));
                    textViewFashion.setTypeface(null,Typeface.BOLD);
                }
                else {
                    textViewFashion.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewFashion.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        editTextOther.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    textViewOther.setTextColor(Color.parseColor("#009688"));
                    textViewOther.setTypeface(null,Typeface.BOLD);
                }
                else {
                    textViewOther.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewOther.setTypeface(null, Typeface.NORMAL);
                }
            }
        });
    }

    public void storeBudget(Budget budget) {
        ServerRequests serverRequest = new ServerRequests(getContext());
        serverRequest.storeBudgetInBackground(budget, new GetBudgetCallback() {
            @Override
            public void done(Budget returnedBudget) {
                //Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
