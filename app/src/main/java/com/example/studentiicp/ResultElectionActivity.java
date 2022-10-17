package com.example.studentiicp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ResultElectionActivity extends AppCompatActivity {

    ArrayList presidentVote, vicePresidentVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String title = "", president_candidate1="", president_candidate2 = "", vice_president_candidate1="", vice_president_candidate2 = "",p1_count="",p2_count="",vp1_count="",vp2_count="";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_election);
        TextView name = findViewById(R.id.textViewTitle);
        TextView president1 = findViewById(R.id.textView4);
        TextView president2 = findViewById(R.id.textView5);
        TextView vicePresident1 = findViewById(R.id.textView2);
        TextView vicePresident2 = findViewById(R.id.textView3);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            title = extras.getString("title");
            president_candidate1 = extras.getString("president_candidate1");
            president_candidate2 = extras.getString("president_candidate2");
            vice_president_candidate1 = extras.getString("vice_president_candidate1");
            vice_president_candidate2 = extras.getString("vice_president_candidate2");
            p1_count = extras.getString("p1_count");
            p2_count = extras.getString("p2_count");
            vp1_count = extras.getString("vp1_count");
            vp2_count = extras.getString("vp2_count");
        }
        name.setText(title);
        president1.setText(president_candidate1);
        president2.setText(president_candidate2);
        vicePresident1.setText(vice_president_candidate1);
        vicePresident2.setText(vice_president_candidate2);

        HorizontalBarChart barChart = findViewById(R.id.barchart);
        getData(p1_count, p2_count);
        BarDataSet barDataSet = new BarDataSet(presidentVote, "Votes for President");
        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(new IntegerFormatter());
        createGraph(barChart, barData, barDataSet);

        HorizontalBarChart barChart2 = findViewById(R.id.barchart2);
        getData2(vp1_count, vp2_count);
        BarDataSet barDataSet2 = new BarDataSet(vicePresidentVote, "Votes for Vice President");
        BarData barData2 = new BarData(barDataSet2);
        barData2.setValueFormatter(new IntegerFormatter());
        createGraph(barChart2, barData2, barDataSet2);

    }

    public void createGraph(HorizontalBarChart barChart, BarData barData, BarDataSet barDataSet){
        barChart.setData(barData);
        barChart.getLegend().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setDrawLabels(false);
        barChart.getXAxis().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(10f);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0f);
    }

    private void getData(String p1_count, String p2_count){
        int president1 = Integer.parseInt(p1_count);
        int president2 = Integer.parseInt(p2_count);
        presidentVote = new ArrayList<>();
        presidentVote.add(new BarEntry(1f,president2));
        presidentVote.add(new BarEntry(2f,president1));
    }
    private void getData2(String vp1_count, String vp2_count){
        int president1 = Integer.parseInt(vp1_count);
        int president2 = Integer.parseInt(vp2_count);
        vicePresidentVote = new ArrayList<>();
        vicePresidentVote.add(new BarEntry(1f,president2));
        vicePresidentVote.add(new BarEntry(2f,president1));
    }


    public class IntegerFormatter extends ValueFormatter {
        private DecimalFormat mFormat;

        public IntegerFormatter() {
            mFormat = new DecimalFormat("###,##0");
        }

        @Override
        public String getBarLabel(BarEntry barEntry) {
            return mFormat.format(barEntry.getY());
        }
    }
}