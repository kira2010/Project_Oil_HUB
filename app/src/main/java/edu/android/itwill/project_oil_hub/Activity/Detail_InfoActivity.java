package edu.android.itwill.project_oil_hub.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.android.itwill.project_oil_hub.Control.DownloadUrlTask;
import edu.android.itwill.project_oil_hub.Control.ReadJson;
import edu.android.itwill.project_oil_hub.Model.Detail_Info;
import edu.android.itwill.project_oil_hub.Model.Oil_LowTop10;
import edu.android.itwill.project_oil_hub.Model.Price;
import edu.android.itwill.project_oil_hub.R;

import static edu.android.itwill.project_oil_hub.Model.OilConstant.*;

public class Detail_InfoActivity extends AppCompatActivity {

    private static final String KEY_LOWTOP10 = "key_lowTop10";

    private ReadJson readJson;

    private Detail_Info info;
    private Oil_LowTop10 lowTop10;

    private String gsId;

    private ImageView imageLogo, imageCarWash, imageMaintenance, imageCVS;

    private TextView textGSName, textAddress, textTelNumber,
            textPrimGasoline, textGasoline, textDiesel, textLampOil, textLPG,
            textPGTradeDate, textGSTradeDate, textDsTradeDate, textLOTradeDate, textLPGTradeDate;

    public static Intent newIntent(Context context, Oil_LowTop10 lowTop10) {
        Intent intent = new Intent(context, Detail_InfoActivity.class);

        intent.putExtra(KEY_LOWTOP10, lowTop10);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__info);

        readJson = ReadJson.getInstance();

        imageLogo = findViewById(R.id.imageLogo);
        imageCarWash = findViewById(R.id.imageCarWash);
        imageMaintenance = findViewById(R.id.imageMaintenance);
        imageCarWash = findViewById(R.id.imageCVS);

        textGSName = findViewById(R.id.textGSName);
        textAddress = findViewById(R.id.textAddress);
        textTelNumber = findViewById(R.id.textTelNumber);

        textPrimGasoline = findViewById(R.id.textPrimGasoline);
        textGasoline = findViewById(R.id.textGasoline);
        textDiesel = findViewById(R.id.textDiesel);
        textLampOil = findViewById(R.id.textLampOil);
        textLPG = findViewById(R.id.textLPG);

        textPGTradeDate = findViewById(R.id.textPGTradeDate);
        textGSTradeDate = findViewById(R.id.textGSTradeDate);
        textDsTradeDate = findViewById(R.id.textDsTradeDate);
        textLOTradeDate = findViewById(R.id.textLOTradeDate);
        textLPGTradeDate = findViewById(R.id.textLPGTradeDate);

        Intent intent = getIntent();
        lowTop10 = intent.getParcelableExtra(KEY_LOWTOP10);

        gsId = lowTop10.getGasStationCode();

        String url = getUrl();

        downloadUrlTask(url);
    }

    private String getUrl() {
        StringBuilder builder = new StringBuilder();

        builder.append(DETAIL_INFO_URL).append(OPINET_API_KEY)
                .append(ID_VALUE).append(gsId);

        return builder.toString();
    }

    private void downloadUrlTask(String url) {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i(MainActivity.TAG, "연결된 네트워크 있음");
            DownloadUrlTask task = new DownloadUrlTask() {
                @Override
                protected void onPostExecute(String result) {
                    readJsonData(result);

                    initActivityResource();
                }
            };
            task.execute(url);
        } else {
            Log.i(MainActivity.TAG, "연결된 네트워크 없음");
        }
    }

    private void readJsonData(String text) {
        Log.i(MainActivity.TAG, "JSON Data 가공");

        info = readJson.read_detailInfo_JsonData(text);

        Log.i(MainActivity.TAG, "JSON Data 해석 완료");
    }

    private void initActivityResource() {
        imageLogo.setImageResource(lowTop10.getImageLogo());
        textGSName.setText(lowTop10.getGasStationName());
        textAddress.setText(lowTop10.getOldAddress());
        textAddress.append("\n" + lowTop10.getNewAddress());
        textTelNumber.setText(info.getTelNumber());

        List<Price> prices = info.getList();

        for(int i = 0; i < prices.size(); i++) {
            for(int j = 0; j < PRODUCTS.length; j++) {
                String oilType = prices.get(i).getOilType();
                String price = prices.get(i).getOilPrice() + "원";

                Log.i(MainActivity.TAG, "거래날짜 : " + prices.get(i).getTradeDay());
                Log.i(MainActivity.TAG, "거래시간 : " + prices.get(i).getTradeTime());

                String tradeDate =
                    spAndUniteString(prices.get(i).getTradeDay(),
                                    prices.get(i).getTradeTime());

                if (oilType.equals("고급휘발유")) {
                    textPrimGasoline.setText(price);
                    textPGTradeDate.setText(tradeDate);
                } else if (oilType.equals("휘발유")) {
                    textGasoline.setText(price);
                    textGSTradeDate.setText(tradeDate);
                } else if (oilType.equals("경유")) {
                    textDiesel.setText(price);
                    textDsTradeDate.setText(tradeDate);
                } else if (oilType.equals("등유")) {
                    textLampOil.setText(price);
                    textLOTradeDate.setText(tradeDate);
                } else if (oilType.equals("LPG")) {
                    textLPG.setText(price);
                    textLPGTradeDate.setText(tradeDate);
                }
            }
        }

        if(info.isCar_wash_YN()) {
            imageCarWash.setImageResource(R.drawable.car_washing_yes);
        }
        if(info.isCvs_YN()) {
            imageCVS.setImageResource(R.drawable.store_yes);
        }
        if(info.isMaint_YN()) {
            imageMaintenance.setImageResource(R.drawable.tool_yes);
        }
    }

    private String spAndUniteString(String tDay, String tTime) {
        String year = tDay.substring(0, 4);
        Log.i(MainActivity.TAG, "year : " + year);
        String month = tDay.substring(4, 6);
        Log.i(MainActivity.TAG, "month : " + month);
        String day = tDay.substring(6);
        Log.i(MainActivity.TAG, "day : " + day);

        String hour = tTime.substring(0, 2);
        Log.i(MainActivity.TAG, "hour : " + hour);
        String minute = tTime.substring(2, 4);
        Log.i(MainActivity.TAG, "minute : " + minute);
        String second = tTime.substring(4);
        Log.i(MainActivity.TAG, "second : " + second);

        StringBuilder builder = new StringBuilder();
        builder.append(year).append("-").append(month).append("-").append(day)
        .append(" ").append(hour).append(":").append(minute).append(":").append(second);

        return builder.toString();
    }
}
