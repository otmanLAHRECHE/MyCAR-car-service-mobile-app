package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.database.DatabaseHelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticsCarActivity extends AppCompatActivity {
    private TextView verifications,reparations,global_distance,avg_between_verif,avg_between_rep,verification_year,reparation_year;
    private BarChart barChart,maintenanceChart;
    private int current_year;
    public String[] quarters = new String[] { "Q1", "Q2", "Q3", "Q4","Q5" };
    public DatabaseHelper databaseHelper;
    private String[]s;
    private Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_car);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        car =(Car) intent.getSerializableExtra("car");

        Log.d("////////////////////////////////////////////car.id",car.getId());


        databaseHelper = new DatabaseHelper(getApplicationContext());

        global_distance = findViewById(R.id.detail_global_dist_car);
        verifications = findViewById(R.id.detail_vrif_car);
        reparations = findViewById(R.id.detail_repare_car);
        avg_between_verif = findViewById(R.id.detail_avg_verif_between_dist_car);
        avg_between_rep = findViewById(R.id.detail_avg_repare_between_dist_car);
        verification_year = findViewById(R.id.detail_avg_vrif_car);
        reparation_year = findViewById(R.id.detail_avg_repare_car);

        barChart = (BarChart) findViewById(R.id.chart_distance_global_car);
        barChart.setTouchEnabled(false);

        maintenanceChart = (BarChart) findViewById(R.id.chart_maintenance_global_car);
        maintenanceChart.setTouchEnabled(false);

        current_year = Calendar.getInstance().get(Calendar.YEAR);


        Statistics statistics = new Statistics();
        statistics.execute();

        Charts charts = new Charts();
        charts.execute();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class Statistics extends AsyncTask<Void, Void, int[]> {
        int [] values ={0,0,0,0,0,0,0};


        @Override
        protected int [] doInBackground(Void... voids) {

            int verifications = databaseHelper.getVerificationCountByCar(car.getId());
            int reparation = databaseHelper.getReparationsCountByCar(car.getId());
            double global_distance = databaseHelper.getGlobalDistanceByCar(car.getId());
            double avg_between_verifications = databaseHelper.getAvgDistanceBetweenVerificationBycar(car.getId());
            double avg_between_reparation = databaseHelper.getAvgDistanceBetweenReparationBycar(car.getId());
            double avg_verification = databaseHelper.getAvgVerificationYearByCar(car.getId());
            double avg_reparation = databaseHelper.getAvgReparationYearByCar(car.getId());

            values[0] = (int)global_distance;
            values[1]= (int)avg_between_verifications;
            values[2]=(int)avg_between_reparation;
            values[3]=verifications;
            values[4]=reparation;
            values[5]=(int)avg_verification;
            values[6]=(int)avg_reparation;
            return values;
        }

        @Override
        protected void onPostExecute(int [] result) {
            super.onPostExecute(result);


            global_distance.setText(String.valueOf(result[0]));
            avg_between_verif.setText(String.valueOf(result[1]));
            avg_between_rep.setText(String.valueOf(result[2]));
            verifications.setText(String.valueOf(result[3]));
            reparations.setText(String.valueOf(result[4]));
            verification_year.setText(String.valueOf(values[5]));
            reparation_year.setText(String.valueOf(values[6]));

        }
    }

    public class Charts extends AsyncTask<Void, Void, BarData[]>{
        String[] values ={"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
        BarData[] barData ={new BarData(),new BarData()};


        @Override
        protected BarData[] doInBackground(Void... voids) {
            int id_user = databaseHelper.getUserLogedInId();
            double curent_3 = databaseHelper.getYearDistanceByCar(car.getId(),current_year-3);
            double curent_2 = databaseHelper.getYearDistanceByCar(car.getId(),current_year-2);
            double curent_1 = databaseHelper.getYearDistanceByCar(car.getId(),current_year-1);
            double curent = databaseHelper.getYearDistanceByCar(car.getId(),current_year);
            double curent__1 = databaseHelper.getYearDistanceByCar(car.getId(),current_year+1);

            int verif_3 = databaseHelper.getVerificationCountYearByCar(car.getId(),current_year-3);
            int verif_2 =databaseHelper.getVerificationCountYearByCar(car.getId(),current_year-2);
            int verif_1 = databaseHelper.getVerificationCountYearByCar(car.getId(),current_year-1);
            int verif = databaseHelper.getVerificationCountYearByCar(car.getId(),current_year);
            int verif__1 = databaseHelper.getVerificationCountYearByCar(car.getId(),current_year+1);
            int rep_3 = databaseHelper.getReparationCountYearByCar(car.getId(),current_year-3);
            int rep_2 = databaseHelper.getReparationCountYearByCar(car.getId(),current_year-2);
            int rep_1= databaseHelper.getReparationCountYearByCar(car.getId(),current_year-1);
            int rep = databaseHelper.getReparationCountYearByCar(car.getId(),current_year);
            int rep__1 = databaseHelper.getReparationCountYearByCar(car.getId(),current_year+1);

            values[0] = String.valueOf(curent_3);
            values[1]= String.valueOf(curent_2);
            values[2]= String.valueOf(curent_1);
            values[3]= String.valueOf(curent);
            values[4]= String.valueOf(curent__1);
            values[5]= String.valueOf(verif_3);
            values[6]= String.valueOf(verif_2);
            values[7]= String.valueOf(verif_1);
            values[8]= String.valueOf(verif);
            values[9]= String.valueOf(verif__1);
            values[10]= String.valueOf(rep_3);
            values[11]= String.valueOf(rep_2);
            values[12]= String.valueOf(rep_1);
            values[13]= String.valueOf(rep);
            values[14]= String.valueOf(rep__1);

            s =values;

            List<BarEntry> distance_entries = new ArrayList<>();

            quarters[0]=String.valueOf(current_year-3);
            quarters[1]=String.valueOf(current_year-2);
            quarters[2]=String.valueOf(current_year-1);
            quarters[3]=String.valueOf(current_year);
            quarters[4]=String.valueOf(current_year+1);


            ValueFormatter formatter = new ValueFormatter() {

                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return quarters[(int) value];
                }

            };

            XAxis xAxis = barChart.getXAxis();
            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(true);
            xAxis.setValueFormatter(formatter);
            YAxis right1 = barChart.getAxisRight();
            right1.setEnabled(false);




            distance_entries.add(new BarEntry(0f, Float.valueOf(s[0])));
            distance_entries.add(new BarEntry(1f, Float.valueOf(s[1])));
            distance_entries.add(new BarEntry(2f, Float.valueOf(s[2])));
            distance_entries.add(new BarEntry(3f, Float.valueOf(s[3])));

            distance_entries.add(new BarEntry(4f, Float.valueOf(s[4])));

            BarDataSet set = new BarDataSet(distance_entries, "distance");

            BarData data = new BarData(set);
            data.setBarWidth(0.9f);



            List<BarEntry> ver_group = new ArrayList<>();
            List<BarEntry> rep_group = new ArrayList<>();



            ver_group.add(new BarEntry(2017,Float.valueOf(s[5])));
            rep_group.add(new BarEntry(2017,Float.valueOf(s[10])));
            ver_group.add(new BarEntry(2018,Float.valueOf(s[6])));
            rep_group.add(new BarEntry(2018,Float.valueOf(s[11])));
            ver_group.add(new BarEntry(2019,Float.valueOf(s[7])));
            rep_group.add(new BarEntry(2019,Float.valueOf(s[12])));
            ver_group.add(new BarEntry(2020,Float.valueOf(s[8])));
            rep_group.add(new BarEntry(2020,Float.valueOf(s[13])));
            ver_group.add(new BarEntry(2021,Float.valueOf(s[9])));
            rep_group.add(new BarEntry(2021,Float.valueOf(s[14])));


            BarDataSet verif_set = new BarDataSet(ver_group,"verifications");
            BarDataSet rep_set = new BarDataSet(rep_group,"reparations");


            verif_set.setColor(R.color.green_prime);
            rep_set.setColor(R.color.yellow);

            float barWidth = 0.45f;
            BarData data2 = new BarData(verif_set,rep_set);
            data2.setBarWidth(barWidth); // set the width of each bar


            YAxis right = maintenanceChart.getAxisRight();
            right.setEnabled(false);
            XAxis xAxis2 = maintenanceChart.getXAxis();

            xAxis2.setGranularity(1f);
            xAxis2.setCenterAxisLabels(true);
            xAxis2.setDrawGridLines(false);
            xAxis2.setDrawAxisLine(true);


            barData[0]=data;
            barData[1]=data2;



            return barData;
        }

        @Override
        protected void onPostExecute(BarData[] result) {
            super.onPostExecute(result);

            Description description = barChart.getDescription();
            description.setEnabled(true);
            description.setText("distance in year");
            barChart.setDrawGridBackground(false);
            barChart.setData(result[0]);
            barChart.setFitBars(true);
            barChart.invalidate();

            float groupSpace = 0.06f;
            float barSpace = 0.02f;
            Description description2 = maintenanceChart.getDescription();
            description2.setEnabled(true);
            description2.setText("maintenance in year");
            maintenanceChart.setDrawGridBackground(false);
            maintenanceChart.setData(result[1]);
            maintenanceChart.groupBars(2017, groupSpace, barSpace);
            maintenanceChart.setFitBars(true);
            maintenanceChart.invalidate();




        }
    }
}