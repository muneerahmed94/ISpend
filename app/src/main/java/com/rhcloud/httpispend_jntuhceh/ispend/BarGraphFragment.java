package com.rhcloud.httpispend_jntuhceh.ispend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.HashMap;

public class BarGraphFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bar_graph, container, false);
        view.setBackgroundColor(Color.WHITE);

        HashMap<String, String> hm = new HashMap<String, String>();

        hm.put("Food",getArguments().getString("Food"));
        hm.put("Entertainment",getArguments().getString("Entertainment"));
        hm.put("Electronics",getArguments().getString("Electronics"));
        hm.put("Fashion",getArguments().getString("Fashion"));
        hm.put("Other",getArguments().getString("Other"));

        hm.put("TFood",getArguments().getString("TFood"));
        hm.put("TEntertainment",getArguments().getString("TEntertainment"));
        hm.put("TElectronics",getArguments().getString("TElectronics"));
        hm.put("TFashion",getArguments().getString("TFashion"));
        hm.put("TOther", getArguments().getString("TOther"));

        BarChart chart = (BarChart) view.findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet(hm));
        chart.setData(data);
        chart.setDescription("Budget Summary");
        chart.animateXY(2000, 2000);
        chart.invalidate();

        return view;
    }

    private ArrayList<BarDataSet> getDataSet(HashMap<String, String> hm) {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        BarEntry v1e1 = new BarEntry(Float.parseFloat(hm.get("TFood")), 0); // Total Food
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(Float.parseFloat(hm.get("TEntertainment")), 1); // Total Entertainment
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(Float.parseFloat(hm.get("TElectronics")), 2); // Total Electronics
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(Float.parseFloat(hm.get("TFashion")), 3); // Total Fashion
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(Float.parseFloat(hm.get("TOther")), 4); // Total Other
        valueSet1.add(v1e5);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(Float.parseFloat(hm.get("Food")), 0); // Spent Food
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(Float.parseFloat(hm.get("Entertainment")), 1); // Spent Entertainment
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(Float.parseFloat(hm.get("Electronics")), 2); // Spent Electronics
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(Float.parseFloat(hm.get("Fashion")), 3); // Spent Fashion
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(Float.parseFloat(hm.get("Other")), 4); // Spent Other
        valueSet2.add(v2e5);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Budget");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Spends");
        barDataSet2.setColor(Color.rgb(255, 102, 0));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Food");
        xAxis.add("Entertainment");
        xAxis.add("Electronics");
        xAxis.add("Fashion");
        xAxis.add("Other");
        return xAxis;
    }
}
