package com.example.studentiicp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ResultPollActivity extends AppCompatActivity {

    List<PieEntry> pieEntryList;
    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String title = "", option1="", option2 = "", option3 = "", option4 = "", option1_count = "", option2_count = "", option3_count = "", option4_count = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_poll);
        TextView pollTitle = findViewById(R.id.textViewTitle);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            title = extras.getString("title");
            option1 = extras.getString("option1");
            option2 = extras.getString("option2");
            option3 = extras.getString("option3");
            option1_count = extras.getString("option1_count");
            option2_count = extras.getString("option2_count");
            option3_count = extras.getString("option3_count");
        }
        pollTitle.setText(title);
        pieEntryList = new ArrayList<>();
        setValues(option1, option2, option3, option4, option1_count, option2_count, option3_count, option4_count);
        pieChart = findViewById(R.id.chart);
        setUpChart();
    }

    private void setUpChart() {
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieData.setValueTextSize(12f);
        pieChart.setData(pieData);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setRotationEnabled(false);
        pieChart.getLegend().setWordWrapEnabled(true);
    }

    private void setValues(String option1, String option2, String option3, String option4, String option1_count, String option2_count, String option3_count, String option4_count) {
        int value1 = Integer.parseInt(option1_count);
        int value2 = Integer.parseInt(option2_count);
        int value3 = Integer.parseInt(option3_count);

        if (value1 > 0)
            pieEntryList.add(new PieEntry(value1, option1));
        if (value2 > 0)
            pieEntryList.add(new PieEntry(value2, option2));
        if (value3 > 0)
            pieEntryList.add(new PieEntry(value3, option3));
    }
}