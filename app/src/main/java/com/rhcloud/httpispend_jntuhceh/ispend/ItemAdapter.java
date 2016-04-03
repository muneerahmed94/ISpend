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
public class ItemAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public ItemAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Items object) {
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
        ItemHolder itemHolder;

        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout, parent, false);
            itemHolder = new ItemHolder();
            itemHolder.textViewItemName = (TextView) row.findViewById(R.id.textViewItemName);
            itemHolder.textViewItemCategory = (TextView) row.findViewById(R.id.textViewItemCategory);
            itemHolder.textViewItemPrice = (TextView) row.findViewById(R.id.textViewItemPrice);
            row.setTag(itemHolder);
        }
        else
        {
            itemHolder = (ItemHolder) row.getTag();
        }

        Items items = (Items) this.getItem(position);
        itemHolder.textViewItemName.setText(items.getItemName());
        itemHolder.textViewItemCategory.setText(items.getItemCategory());
        itemHolder.textViewItemPrice.setText(items.getItemPrice());

        if(items.getItemName().equals("Name")) {
            itemHolder.textViewItemName.setTypeface(null, Typeface.BOLD);
            itemHolder.textViewItemName.setTextColor(Color.parseColor("#000000"));
        }
        if(items.getItemCategory().equals("Category")) {
            itemHolder.textViewItemCategory.setTypeface(null, Typeface.BOLD);
            itemHolder.textViewItemCategory.setTextColor(Color.parseColor("#000000"));
        }
        if(items.getItemPrice().equals("Price")) {
            itemHolder.textViewItemPrice.setTypeface(null, Typeface.BOLD);
            itemHolder.textViewItemPrice.setTextColor(Color.parseColor("#000000"));
        }

        return row;
    }

    static class ItemHolder
    {
        TextView textViewItemName, textViewItemCategory, textViewItemPrice;
    }
}
