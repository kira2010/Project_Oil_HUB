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

import java.text.SimpleDateFormat;
import java.util.List;

import edu.android.itwill.project_oil_hub.Control.DownloadUrlTask;
import edu.android.itwill.project_oil_hub.Control.ReadJson;
import edu.android.itwill.project_oil_hub.Model.LocalAverage;
import edu.android.itwill.project_oil_hub.R;

import static edu.android.itwill.project_oil_hub.Model.OilConstant.*;

public class AvgLocalActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    private ReadJson readJson;

    private Spinner spSiDo;
    private ArrayAdapter adapter;

    private TextView textDate, textGSavgPrice, textGSdiff, textDSavgPrice, textDSdiff, textLPGavgPrice,
            textLPGdiff, textPGSavgPrice, textPGSdiff, textLOavgPrice, textLOdiff;

    private List<LocalAverage> localAverageList;

    private String localCode;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AvgLocalActivity.class);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_avg);

        readJson = ReadJson.getInstance();

        spSiDo = findViewById(R.id.spSiDo);
        adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, SIDO);
        spSiDo.setAdapter(adapter);
        spSiDo.setOnItemSelectedListener(this);

        textDate = findViewById(R.id.textWeek);
        textGSavgPrice = findViewById(R.id.textGSavgPrice);
        textGSdiff = findViewById(R.id.textGSdiff);
        textDSavgPrice = findViewById(R.id.textDSavgPrice);
        textDSdiff = findViewById(R.id.textDSdiff);
        textLPGavgPrice = findViewById(R.id.textLPGavgPrice);
        textLPGdiff = findViewById(R.id.textLPGdiff);
        textPGSavgPrice = findViewById(R.id.textPGSavgPrice);
        textPGSdiff = findViewById(R.id.textPGSdiff);
        textLOavgPrice = findViewById(R.id.textLOavgPrice);
        textLOdiff = findViewById(R.id.textLOdiff);
    }

    public void researchLocalAvg(View view) {
        String url = getUrl();

        downloadUrlTask(url);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        localCode = SIDO_CODE[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private String getUrl() {
        StringBuilder builder = new StringBuilder();

        builder.append(SIDO_AVG_URL).append(OPINET_API_KEY).append(SIDO_VALUE).append(localCode);

        return builder.toString();
    }

    private void downloadUrlTask(String url) {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            Log.i(MainActivity.TAG, "연결된 네트워크 있음");

            DownloadUrlTask task = new DownloadUrlTask() {
                @Override
                protected void onPostExecute(String result) {
                    doReadJsonData(result);

                    initTable();
                }
            };

            task.execute(url);

        } else {
            Log.i(MainActivity.TAG, "연결된 네트워크 없음");
        }
    }
    private void doReadJsonData(String text) {
        localAverageList = readJson.read_LocalAverage_JSONData(text);
    }


    private void initTable() {
        long today = System.currentTimeMillis();
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일 기준");
        textDate.setText(sdfNow.format(today));

        for(LocalAverage avg : localAverageList) {
            String avgValue = avg.getAvgPrice();
            String diff = convertString(avg);

            if (avg.getProdNM().equals(PRODUCTS[0])) {
                textGSavgPrice.setText(avgValue);
                textGSdiff.setText(diff);
            }
            if (avg.getProdNM().equals(PRODUCTS[1])) {
                textDSavgPrice.setText(avgValue);
                textDSdiff.setText(diff);
            }
            if (avg.getProdNM().equals(PRODUCTS[3])) {
                textLPGavgPrice.setText(avgValue);
                textLPGdiff.setText(diff);
            }
            if (avg.getProdNM().equals(PRODUCTS[2])) {
                textPGSavgPrice.setText(avgValue);
                textPGSdiff.setText(diff);
            }
            if (avg.getProdNM().equals(PRODUCTS[4])) {
                textLOavgPrice.setText(avgValue);
                textLOdiff.setText(diff);
            }
        }
    }

    private String convertString(LocalAverage avg) {
        String vP = avg.getDiff();
        // -10.24

        String upDown = "+";
        String variation = "";
        if(vP.substring(0, 1).equals("-")) { // "+"
            upDown = MINUS;
            variation = vP.substring(1);
        } else { // "-"
            upDown = PLUS;
            variation = vP;
        }
        // upDown : ▲ || ▼

        // -10.24 -> 10.24
        Log.i(MainActivity.TAG, "variation : " + upDown + variation);

        StringBuilder builder = new StringBuilder();
        builder.append(upDown).append(variation); // ▲ || ▼ + 10.24

        // ▼10.24"

        return builder.toString();
    }
}
