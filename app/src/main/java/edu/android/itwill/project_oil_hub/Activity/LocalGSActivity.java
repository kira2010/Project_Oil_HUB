package edu.android.itwill.project_oil_hub.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.widget.TextView;

import edu.android.itwill.project_oil_hub.Control.DownloadUrlTask;
import edu.android.itwill.project_oil_hub.Control.Result_LowTop10_Lab;
import edu.android.itwill.project_oil_hub.Control.ReadJson;
import edu.android.itwill.project_oil_hub.Model.Oil_LowTop10;
import edu.android.itwill.project_oil_hub.Model.Result_LowTop10;
import edu.android.itwill.project_oil_hub.R;

import static edu.android.itwill.project_oil_hub.Model.OilConstant.*;

public class LocalGSActivity extends AppCompatActivity {

    class ResultAdapter
        extends Adapter<ViewHolder> {

        class ResultViewHolder extends ViewHolder {
            private ImageView imageLogo;
            private TextView textGSName, textOilPrice;

            public ResultViewHolder(@NonNull View itemView) {
                super(itemView);

                imageLogo = itemView.findViewById(R.id.imageLogo);
                textGSName = itemView.findViewById(R.id.textGSName);
                textOilPrice = itemView.findViewById(R.id.textOilPrice);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(LocalGSActivity.this);
            View itemView = inflater.inflate(R.layout.gas_station_item, viewGroup, false);
            ResultViewHolder holder = new ResultViewHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            ResultViewHolder holder = (ResultViewHolder) viewHolder;

            Oil_LowTop10 oilLowTop10 = lab.getResultLowTop10().getList().get(i);

            holder.imageLogo.setImageResource(oilLowTop10.getImageLogo());
            holder.textGSName.setText(oilLowTop10.getGasStationName());
            holder.textOilPrice.setText(oilLowTop10.getPrice() + "원");

            holder.itemView.setOnClickListener(v -> {
                Intent intent = Detail_InfoActivity.newIntent(LocalGSActivity.this, oilLowTop10);

                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return lab.getResultLowTop10().getList().size();
        }
    }

    private Spinner spOilType, spSiDo, spSiGunGu;
    private String oilType, siGunGuCode;

    private ArrayAdapter siDoAdapter, siGunGuAdapter;

    private Result_LowTop10 resultLowTop10;
    private Result_LowTop10_Lab lab;

    private ReadJson readJson;

    private RecyclerView recyclerView;
    private ResultAdapter adapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LocalGSActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MainActivity.TAG, "LocalGSActivity 실행");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_gs);

        lab = Result_LowTop10_Lab.getInstance();
        readJson = ReadJson.getInstance();

        recyclerView = findViewById(R.id.recyclerView01);

        spOilType = findViewById(R.id.spOilType);
        spSiDo = findViewById(R.id.spSido);
        spSiGunGu = findViewById(R.id.spSigungu);

        spOilType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                oilType = PRODUCT_CODES[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        siDoAdapter =
            new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, SIDO);
        spSiDo.setAdapter(siDoAdapter);

        spSiDo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] siGunGu = changeSiGunGu(position);

                siGunGuAdapter =
                    new ArrayAdapter(LocalGSActivity.this, android.R.layout.simple_expandable_list_item_1, siGunGu);
                spSiGunGu.setAdapter(siGunGuAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSiGunGu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] siGunGuCodes = getSiGunGuCode(spSiDo.getSelectedItemPosition());
                siGunGuCode = siGunGuCodes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String getUrl() {
        StringBuilder builder = new StringBuilder();

        builder.append(LOW_TOP10_URL).append(OPINET_API_KEY)
                .append(PRODCD_VALUE).append(oilType)
                .append(AREA_VALUE).append(siGunGuCode);

        return builder.toString();
    }

    private void doDownloadUrlTask(String url) {
        Log.i(MainActivity.TAG, "doDownloadUrlTask() 실행");
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            DownloadUrlTask task = new DownloadUrlTask() {
                @Override
                protected void onPostExecute(String result) {
                    Log.i(MainActivity.TAG, "결과 : " + result);
                    doReadJsonData(result);

                    initRecyclerView();
                    changeRecyclerView();
                }
            };
            task.execute(url);

            Log.i(MainActivity.TAG, "doDownloadUrlTask() 종료");
        }
    }

    public void researchLowTop10(View view) {
        String url = getUrl();

        Log.i(MainActivity.TAG, "URL : " + url);

        doDownloadUrlTask(url);
    }

    private void doReadJsonData(String text) {
        Log.i(MainActivity.TAG, "doReadJsonData() 실행");
        Log.i(MainActivity.TAG, "text : " + text);

        resultLowTop10 = readJson.read_LowTop10_JsonData(text);

        lab.setResultLowTop10(resultLowTop10);
        Log.i(MainActivity.TAG, "doReadJsonData() 종료");
    }

    private String[] changeSiGunGu(int position) {
        String[] siGunGuArr = {};

        switch (position) {
            case 0:
                siGunGuArr = SEOUL;
                break;
            case 1:
                siGunGuArr = GYEONGGI;
                break;
            case 2:
                siGunGuArr = GANGWON;
                break;
            case 3:
                siGunGuArr = CHUNGBUK;
                break;
            case 4:
                siGunGuArr = CHUNGNAM;
                break;
            case 5:
                siGunGuArr = JEONBUK;
                break;
            case 6:
                siGunGuArr = JEONNAM;
                break;
            case 7:
                siGunGuArr = GYEONGBUK;
                break;
            case 8:
                siGunGuArr = GYEONGNAM;
                break;
            case 9:
                siGunGuArr = BUSAN;
                break;
            case 10:
                siGunGuArr = JEJU;
                break;
            case 11:
                siGunGuArr = DAEGU;
                break;
            case 12:
                siGunGuArr = INCHEON;
                break;
            case 13:
                siGunGuArr = GWANGJU;
                break;
            case 14:
                siGunGuArr = DAEJEON;
                break;
            case 15:
                siGunGuArr = ULSAN;
                break;
            case 16:
                siGunGuArr = SEJONG;
                break;
        }

        return siGunGuArr;
    }

    private String[] getSiGunGuCode(int position) {
        String[] siGunGuCodes = {};

        switch (position) {
            case 0:
                siGunGuCodes = SEOUL_CODE;
                break;
            case 1:
                siGunGuCodes = GYEONGGI_CODE;
                break;
            case 2:
                siGunGuCodes = GANGWON_CODE;
                break;
            case 3:
                siGunGuCodes = CHUNGBUK_CODE;
                break;
            case 4:
                siGunGuCodes = CHUNGNAM_CODE;
                break;
            case 5:
                siGunGuCodes = JEONBUK_CODE;
                break;
            case 6:
                siGunGuCodes = JEONNAM_CODE;
                break;
            case 7:
                siGunGuCodes = GYEONGBUK_CODE;
                break;
            case 8:
                siGunGuCodes = GYEONGNAM_CODE;
                break;
            case 9:
                siGunGuCodes = BUSAN_CODE;
                break;
            case 10:
                siGunGuCodes = JEJU_CODE;
                break;
            case 11:
                siGunGuCodes = DAEGU_CODE;
                break;
            case 12:
                siGunGuCodes = INCHEON_CODE;
                break;
            case 13:
                siGunGuCodes = GWANGJU_CODE;
                break;
            case 14:
                siGunGuCodes = DAEJEON_CODE;
                break;
            case 15:
                siGunGuCodes = ULSAN_CODE;
                break;
            case 16:
                siGunGuCodes = SEJONG_CODE;
                break;
        }

        return siGunGuCodes;
    }

    private void initRecyclerView() {
        Log.i(MainActivity.TAG, "initRecyclerView() 실행");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResultAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        Log.i(MainActivity.TAG, "initRecyclerView() 종료");
    }

    private void changeRecyclerView() {
        Log.i(MainActivity.TAG, "changeRecyclerView() 실행");
        adapter.notifyDataSetChanged();
        recyclerView.invalidate();
        Log.i(MainActivity.TAG, "changeRecyclerView() 종료");
    }
}
