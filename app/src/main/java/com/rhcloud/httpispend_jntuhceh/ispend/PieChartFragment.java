package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class PieChartFragment extends Fragment {

    private static int[] COLORS = new int[] { Color.rgb(16, 150, 24), Color.rgb(51, 102, 204), Color.rgb(255, 153, 0), Color.rgb(220, 57, 18), Color.rgb(125, 28, 123) };

    private static double[] VALUES;

    private static String[] NAME_LIST = new String[] { "Food", "Entertainment", "Electronics", "Fashion", "Other"};

    private CategorySeries mSeries = new CategorySeries("");

    private DefaultRenderer mRenderer = new DefaultRenderer();

    private GraphicalView mChartView;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_pie_chart, container, false);
        view.setBackgroundColor(Color.WHITE);

        double food = Double.parseDouble(getArguments().getString("Food"));
        double entertainment = Double.parseDouble(getArguments().getString("Entertainment"));
        double electronics = Double.parseDouble(getArguments().getString("Electronics"));
        double fashion = Double.parseDouble(getArguments().getString("Fashion"));
        double other = Double.parseDouble(getArguments().getString("Other"));

        VALUES = new double[]{food, entertainment, electronics, fashion, other};

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.rgb(255, 255, 255));
        mRenderer.setChartTitleTextSize(30);
        mRenderer.setLabelsTextSize(20);
        mRenderer.setLegendTextSize(25);
        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        //mRenderer.setZoomButtonsVisible(true);
        mRenderer.setShowLabels(false);
        //mRenderer.setDisplayValues(true);
        mRenderer.setShowLabels(true);
        mRenderer.setLabelsColor(Color.rgb(0,0,0));
        mRenderer.setStartAngle(270);

        for (int i = 0; i < VALUES.length; i++) {
            mSeries.add(NAME_LIST[i] + " " + VALUES[i], VALUES[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
        }

        if (mChartView != null) {
            mChartView.repaint();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.chart);
            mChartView = ChartFactory.getPieChartView(getContext(), mSeries, mRenderer);
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);

            mChartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();

                    if (seriesSelection == null) {
                        Toast.makeText(getContext(), "No chart element was clicked", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),"Chart element data point index "+ (seriesSelection.getPointIndex()+1) + " was clicked" + " point value="+ seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mChartView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                    if (seriesSelection == null) {
                        Toast.makeText(getContext(),"No chart element was long pressed", Toast.LENGTH_SHORT);
                        return false;
                    }
                    else {
                        Toast.makeText(getContext(),"Chart element data point index "+ seriesSelection.getPointIndex()+ " was long pressed",Toast.LENGTH_SHORT);
                        return true;
                    }
                }
            });
            layout.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        }
        else {
            mChartView.repaint();
        }
    }
}
