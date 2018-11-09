package edu.android.itwill.project_oil_hub.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import edu.android.itwill.project_oil_hub.Control.DownloadUrlTask;
import edu.android.itwill.project_oil_hub.Control.ReadJson;
import edu.android.itwill.project_oil_hub.Model.Avg7DaysPrices;
import edu.android.itwill.project_oil_hub.R;

import static edu.android.itwill.project_oil_hub.Model.OilConstant.*;

public class Avg7DaysActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ReadJson readJson;

    private List<Avg7DaysPrices> list;
    private String oilType;

    private Spinner spOilType;
    private TextView text1stDay, text2ndDay, text3rdDay, text4thDay, text5thDay, text6thDay, text7thDay,
            text1stPrice, text2ndPrice, text3rdPrice, text4thPrice, text5thPrice, text6thPrice, text7thPrice;

    private List<TextView> textDays, textPrices;

    private LineChart lineChart;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, Avg7DaysActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avg7_days);

        readJson = ReadJson.getInstance();

        spOilType = findViewById(R.id.spOilType);
        spOilType.setOnItemSelectedListener(this);

        textDays = new ArrayList<>();
        text1stDay = findViewById(R.id.text1stDay);
        text2ndDay = findViewById(R.id.text2ndDay);
        text3rdDay = findViewById(R.id.text3rdDay);
        text4thDay = findViewById(R.id.text4thDay);
        text5thDay = findViewById(R.id.text5thDay);
        text6thDay = findViewById(R.id.text6thDay);
        text7thDay = findViewById(R.id.text7thDay);
        textDays.add(text1stDay);
        textDays.add(text2ndDay);
        textDays.add(text3rdDay);
        textDays.add(text4thDay);
        textDays.add(text5thDay);
        textDays.add(text6thDay);
        textDays.add(text7thDay);

        textPrices = new ArrayList<>();
        text1stPrice = findViewById(R.id.text1stPrice);
        text2ndPrice = findViewById(R.id.text2ndPrice);
        text3rdPrice = findViewById(R.id.text3rdPrice);
        text4thPrice = findViewById(R.id.text4thPrice);
        text5thPrice = findViewById(R.id.text5thPrice);
        text6thPrice = findViewById(R.id.text6thPrice);
        text7thPrice = findViewById(R.id.text7thPrice);
        textPrices.add(text1stPrice);
        textPrices.add(text2ndPrice);
        textPrices.add(text3rdPrice);
        textPrices.add(text4thPrice);
        textPrices.add(text5thPrice);
        textPrices.add(text6thPrice);
        textPrices.add(text7thPrice);

        lineChart = findViewById(R.id.chart);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        oilType = PRODUCT_CODES[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void researchAvg7Days(View view) {
        String url = getUrl();
        downloadUrlTask(url);
    }

    private String getUrl() {
        StringBuilder builder = new StringBuilder();

        builder.append(WEEK_DAY_AVG_URL).append(OPINET_API_KEY).append(PRODCD_VALUE).append(oilType);

        return builder.toString();
    }

    private void downloadUrlTask(String url) {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            Log.i(MainActivity.TAG, "연결된 네트워크 있음");
            DownloadUrlTask task = new DownloadUrlTask() {
                @Override
                protected void onPostExecute(String result) {
                    read_Avg7Days_JSONData(result);

                    initTable();
                    initChart();
                }
            };

            task.execute(url);
        } else {
            Log.i(MainActivity.TAG, "연결된 네트워크 없음");
        }
    }

    private void read_Avg7Days_JSONData(String text) {
        list = readJson.read_Avg7Days_JSONData(text);
    }

    private void initTable() {
        for(int i = 0; i < list.size(); i++) {
            textDays.get(i).setText(sepAndUniDate(list.get(i).getDate()));
            textPrices.get(i).setText(list.get(i).getAvgPrice());
        }
    }

    private void initChart() {
        float[] floats = {0f, 1f, 2f, 3f, 4f, 5f, 6f};

        List<Float> floatList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            Float chFloat = Float.parseFloat(list.get(i).getAvgPrice());
            floatList.add(chFloat);
        }

        List<Entry> entries = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            entries.add(new Entry(floats[i], floatList.get(i)));
        }

        String[] date = {
                spUniDate2(list.get(0).getDate()),
                spUniDate2(list.get(1).getDate()),
                spUniDate2(list.get(2).getDate()),
                spUniDate2(list.get(3).getDate()),
                spUniDate2(list.get(4).getDate()),
                spUniDate2(list.get(5).getDate()),
                spUniDate2(list.get(6).getDate())
        };

        IAxisValueFormatter formatter = (value, axis) -> {
            return date[(int) value];
        };

        LineDataSet lineDataSet = new LineDataSet(entries, "평균가");

        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleHoleColor(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextColor(Color.BLACK); // 레이블 색 설정
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK); // 레이블 색 설정

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false); //축의 레이블을 그리려면 true로 설정

        yRAxis.setDrawAxisLine(false); //축 옆의 선을 그려야하는지 여부 true로 설정

        yRAxis.setDrawGridLines(false); // 축의 그리드 선 그리기

        Description description = new Description();
        description.setText("최근 7일간 전국 일일 평균가");

        lineChart.setDoubleTapToZoomEnabled(false); // 차트 두번 탭하여 확대/축소
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();
    }

    private String sepAndUniDate(String text) {
        String year = text.substring(0, 4);
        String month = text.substring(4, 6);
        String day = text.substring(6);

        StringBuilder builder = new StringBuilder();

        builder.append(year).append("-").append(month).append("-").append(day);

        return builder.toString();
    }

    private String spUniDate2(String text) {
        String month = text.substring(4, 6);
        String day = text.substring(6);

        StringBuilder builder = new StringBuilder();

        builder.append(month).append(".").append(day);

        return builder.toString();
    }
}
