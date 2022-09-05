package com.example.covidtrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtrackerapp.api.ApiUtilities;
import com.example.covidtrackerapp.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm, totalActive, totalRecovered, totalDeath, totalTests;
    private TextView TodayConfirm, todayRecovered, todayDeath, dateTv;
    private PieChart pieChart;

    private List<CountryData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();

        init();

        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getCountry().equals("Pakistan")) {
                        int confirm = Integer.parseInt(list.get(i).getCases());
                        int active = Integer.parseInt(list.get(i).getActive());
                        int recovered = Integer.parseInt(list.get(i).getRecovered());
                        int Death = Integer.parseInt(list.get(i).getDeaths());

                        totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                        totalActive.setText(NumberFormat.getInstance().format(active));
                        totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                        totalDeath.setText(NumberFormat.getInstance().format(Death));

                        totalDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                        totalConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                        totalRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                        totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                        setText(list.get(i).getUpdated());

                        pieChart.addPieSlice(new PieModel("Confirm", confirm, getResources().getColor(R.color.Yellow)));
                        pieChart.addPieSlice(new PieModel("Confirm", active, getResources().getColor(R.color.blue_pie)));
                        pieChart.addPieSlice(new PieModel("Confirm", recovered, getResources().getColor(R.color.green_pie)));
                        pieChart.addPieSlice(new PieModel("Confirm", Death, getResources().getColor(R.color.red_pie)));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setText(String updated) {
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        long milisecond = Long.parseLong(updated);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milisecond);

        dateTv.setText("Updated at " + format.format(calendar.getTime()));
    }

    private void init() {

        totalConfirm = findViewById(R.id.totalConfirm);
        totalActive = findViewById(R.id.totalActive);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalDeath = findViewById(R.id.totalDeaths);
        TodayConfirm = findViewById(R.id.todayConfirm);
        totalTests = findViewById(R.id.totaltests);
        totalRecovered = findViewById(R.id.todayRecovered);
        totalDeath = findViewById(R.id.todayDeaths);
        pieChart = findViewById(R.id.pieChart);
        dateTv = findViewById(R.id.date);


    }
}