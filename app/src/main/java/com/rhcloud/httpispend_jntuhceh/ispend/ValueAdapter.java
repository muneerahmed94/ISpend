package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muneer on 06-03-2016.
 */
public class ValueAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public ValueAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Value object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ValueHolder valueHolder;
        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.value_layout, parent, false);
            valueHolder = new ValueHolder();
            valueHolder.textViewCategory = (TextView) row.findViewById(R.id.textViewCategory);
            valueHolder.textViewBudget = (TextView) row.findViewById(R.id.textViewBudget);
            valueHolder.textViewSpends = (TextView) row.findViewById(R.id.textViewSpends);
            valueHolder.textViewRemaining = (TextView) row.findViewById(R.id.textViewRemaining);
            row.setTag(valueHolder);
        }
        else
        {
            valueHolder = (ValueHolder) row.getTag();
        }

        Value value = (Value) this.getItem(position);
        valueHolder.textViewCategory.setText(value.getCategory());
        valueHolder.textViewBudget.setText(value.getBudget());
        valueHolder.textViewSpends.setText(value.getSpends());
        valueHolder.textViewRemaining.setText(value.getRemaining());


        if(value.getBudget().equals("Budget")) {
            valueHolder.textViewBudget.setTypeface(null, Typeface.BOLD);
        }

        if(value.getSpends().equals("Spends")) {
            valueHolder.textViewSpends.setTypeface(null, Typeface.BOLD);
        }

        if(value.getRemaining().equals("Remaining")) {
            valueHolder.textViewRemaining.setTypeface(null, Typeface.BOLD);
        }

        Double budget = 0.0,spends = 0.0, remaining = 0.0;

        boolean isLabel = true;

        if(!value.getBudget().equals("Budget") && !value.getSpends().equals("Spends")) {
            budget = Double.parseDouble(value.getBudget());
            spends = Double.parseDouble(value.getSpends());
            isLabel = false;
        }


        if(!isLabel) {
            valueHolder.textViewBudget.setTextColor(Color.parseColor("#337ab7"));


            if(spends > budget) {
                valueHolder.textViewSpends.setTextColor(Color.RED);
                valueHolder.textViewRemaining.setTextColor(Color.RED);
            }
            else if(spends > budget * 0.7) {
                valueHolder.textViewSpends.setTextColor(Color.parseColor("#f0ad4e"));
                valueHolder.textViewRemaining.setTextColor(Color.parseColor("#f0ad4e"));
            }
            else {
                valueHolder.textViewSpends.setTextColor(Color.parseColor("#689f38"));
                valueHolder.textViewRemaining.setTextColor(Color.parseColor("#689f38"));
            }
        }
        else {
            valueHolder.textViewBudget.setTextColor(Color.parseColor("#000000"));
            valueHolder.textViewSpends.setTextColor(Color.parseColor("#000000"));
            valueHolder.textViewRemaining.setTextColor(Color.parseColor("#000000"));
        }

        valueHolder.textViewCategory.setTextColor(Color.parseColor("#000000"));

        return row;
    }

    static class ValueHolder {
        TextView textViewCategory, textViewBudget, textViewSpends, textViewRemaining;
    }
}
