package edu.android.itwill.project_oil_hub.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import edu.android.itwill.project_oil_hub.Control.DownloadUrlTask;
import edu.android.itwill.project_oil_hub.Control.ReadJson;
import edu.android.itwill.project_oil_hub.Model.AvgLastWeek;
import edu.android.itwill.project_oil_hub.R;

import static edu.android.itwill.project_oil_hub.Model.OilConstant.*;

public class AvgLastWeekActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ReadJson readJson;

    private Spinner spSiDo;
    private TextView textWeek, textStartDay, textEndDay,
            textAvgLwGs, textAvgLwDs, textAvgLwLPG, textAvgLwPgs, textAvgLwLo;

    private String sidoCode;
    private List<AvgLastWeek> list;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AvgLastWeekActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avg_last_week);

        readJson = ReadJson.getInstance();

        spSiDo = findViewById(R.id.spSiDo);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, SIDO);
        spSiDo.setAdapter(adapter);

        spSiDo.setOnItemSelectedListener(this);

        textWeek = findViewById(R.id.textWeek);
        textStartDay = findViewById(R.id.textStartDay);
        textEndDay = findViewById(R.id.textEndDay);
        textAvgLwGs = findViewById(R.id.textAvgLwGs);
        textAvgLwDs = findViewById(R.id.textAvgLwDs);
        textAvgLwLPG = findViewById(R.id.textAvgLwLPG);
        textAvgLwPgs = findViewById(R.id.textAvgLwPgs);
        textAvgLwLo = findViewById(R.id.textAvgLwLo);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sidoCode = SIDO_CODE[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private String getUrl() {
        StringBuilder builder = new StringBuilder();

        builder.append(LAST_WEEK_AVG_URL).append(OPINET_API_KEY).append(SIDO_VALUE).append(sidoCode);

        return builder.toString();
    }

    public void researchAvgLW(View view) {
        String url = getUrl();

        downloadUrlTask(url);
    }

    private void downloadUrlTask(String url) {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i(MainActivity.TAG, "연결된 네트워크 있음");
            DownloadUrlTask task = new DownloadUrlTask() {
                @Override
                protected void onPostExecute(String result) {
                    read_AvgLwPrice_JSONData(result);

                    initWidget();
                }
            };

            task.execute(url);
        } else {
            Log.i(MainActivity.TAG, "연결된 네트워크 없음");
        }
    }

    private void read_AvgLwPrice_JSONData(String text) {
        list = readJson.read_AvgLastWeek_JSONData(text);
    }

    private void initWidget() {
        String week = sepAndUniWeek(list.get(0).getWeek());
        textWeek.setText(week);

        String startDate = sepAndUniDate(list.get(0).getStartDT());
        String endDate = sepAndUniDate(list.get(0).getEndDT());
        textStartDay.setText(startDate);
        textEndDay.setText(endDate);

        for(AvgLastWeek avgLastWeek : list) {
            String prodNM = avgLastWeek.getProdNM();
            String price = avgLastWeek.getAvgPrice();

            if(prodNM.equals("휘발유")) {
                textAvgLwGs.setText(price);
            }
            if(prodNM.equals("경유")) {
                textAvgLwDs.setText(price);
            }
            if(prodNM.equals("LPG")) {
                textAvgLwLPG.setText(price);
            }
            if(prodNM.equals("고급휘발유")) {
                textAvgLwPgs.setText(price);
            }
            if(prodNM.equals("등유")) {
                textAvgLwLo.setText(price);
            }
        }
    }

    private String sepAndUniWeek(String text) {
        String year = text.substring(0, 4);
        String month = text.substring(4, 6);
        String week = text.substring(6, 7);

        StringBuilder builder = new StringBuilder();

        builder.append(year).append("년 ").append(month).append("월 ").append(week).append("주차");

        return builder.toString();
    }

    private String sepAndUniDate(String text) {
        String year = text.substring(0, 4);
        String month = text.substring(4, 6);
        String day = text.substring(6);

        StringBuilder builder = new StringBuilder();

        builder.append(year).append("년 ").append(month).append("월 ").append(day).append("일");

        return builder.toString();
    }
}
