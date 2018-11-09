package edu.android.itwill.project_oil_hub.Control;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.android.itwill.project_oil_hub.Activity.MainActivity;
import edu.android.itwill.project_oil_hub.Model.AllAvgPrices;
import edu.android.itwill.project_oil_hub.Model.Avg7DaysPrices;
import edu.android.itwill.project_oil_hub.Model.AvgLastWeek;
import edu.android.itwill.project_oil_hub.Model.Detail_Info;
import edu.android.itwill.project_oil_hub.Model.LocalAverage;
import edu.android.itwill.project_oil_hub.Model.Oil_LowTop10;
import edu.android.itwill.project_oil_hub.Model.Price;
import edu.android.itwill.project_oil_hub.Model.Result_LowTop10;

import static edu.android.itwill.project_oil_hub.Model.OilConstant.*;

public class ReadJson {

    public static ReadJson instance = null;

    private ReadJson() {
        Log.i(MainActivity.TAG, "ReadJson 컨트롤러 생성");
    }

    public static ReadJson getInstance() {
        if (instance == null) {
            instance = new ReadJson();
        }

        return instance;
    }

    public Result_LowTop10 read_LowTop10_JsonData(String text) {
        Result_LowTop10 result_lowTop10 = null;

        try {
            JSONObject resultObject = new JSONObject(text);

            JSONObject oilObject = resultObject.getJSONObject(VAR_RESULT);

            List<Oil_LowTop10> list = new ArrayList<>();
            JSONArray jsonArray = oilObject.getJSONArray(VAR_OIL);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                String gasStationCode = object.getString(VAR_UNI_ID);
                int price = object.getInt(VAR_PRICE);
                String brand = object.getString(VAR_POLL_DIV_CD);

                String brandName = "";
                int brandImage = 0;

                for (int j = 0; j < POLL_DIV_CD.length; j++) {
                    if (brand.equals(POLL_DIV_CD[j])) {
                        brandName = BRANDS[j];
                        brandImage = BRANDIMAGES[j];
                        break;
                    }
                }

                String gasStationName = object.getString(VAR_OS_NM);
                String oldAddress = object.getString(VAR_VAN_ADR);
                String newAddress = object.getString(VAR_NEW_ADR);

                Oil_LowTop10 oil =
                        new Oil_LowTop10(gasStationCode, price, brandName,
                                gasStationName, oldAddress, newAddress, brandImage);
                Log.i(MainActivity.TAG, "Oil_LowTop10 객체 생성 완료");

                list.add(oil);
                Log.i(MainActivity.TAG, "Oil_LowTop10 객체 리스트 추가");
            }

            result_lowTop10 = new Result_LowTop10(list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result_lowTop10;
    }

    public Detail_Info read_detailInfo_JsonData(String text) {
        Detail_Info info = null;

        try {
            JSONObject object = new JSONObject(text);
            JSONObject jsonObject = object.getJSONObject(VAR_RESULT);
            Log.i(MainActivity.TAG, "RESULT 분리 시작");

            JSONArray jsonArray = jsonObject.getJSONArray(VAR_OIL);
            Log.i(MainActivity.TAG, "OIL Array 분리 시작");

            JSONObject oilObject = jsonArray.getJSONObject(0);

            String telNumber = oilObject.getString(VAR_TEL);
            String lpg_YN = oilObject.getString(VAR_LPG_YN);

            boolean maint_YN = false;
            if(oilObject.getString(VAR_MAINT_YN).equals("Y")) {
                maint_YN = true;
            }
            boolean car_wash_YN = false;
            if(oilObject.getString(VAR_CAR_WASH_YN).equals("Y")) {
                car_wash_YN = true;
            }
            boolean cvs_YN = false;
            if(oilObject.getString(VAR_CVS_YN).equals("Y")) {
                cvs_YN = true;
            }

            JSONArray priceArray = oilObject.getJSONArray(VAR_OIL_PRICE);
            List<Price> priceList = new ArrayList<>();
            Log.i(MainActivity.TAG, "PRICE 분리 시작");
            for(int i = 0; i < priceArray.length(); i++) {
                JSONObject priceObject = priceArray.getJSONObject(i);

                String oil = priceObject.getString(VAR_PRODCD);
                String oilType = "";
                for(int j = 0; j < PRODUCT_CODES.length; j++) {
                    if(oil.equals(PRODUCT_CODES[j])) {
                        oilType = PRODUCTS[j];
                        break;
                    }
                }
                int oilPrice = priceObject.getInt(VAR_PRICE);

                String tradeDay = priceObject.getString(VAR_TRADE_DT);
                String tradeTime = priceObject.getString(VAR_TRADE_TM);

                Price price = new Price(oilType, oilPrice, tradeDay, tradeTime);

                priceList.add(price);
            }

            info = new Detail_Info(telNumber, lpg_YN,
                    maint_YN, car_wash_YN, cvs_YN, priceList);

            Log.i(MainActivity.TAG, "info : " + info.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

    public List<AllAvgPrices> read_AllAvgPrices_JSONData(String text) {
        List<AllAvgPrices> list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(text);
            JSONObject resultObject = jsonObject.getJSONObject(VAR_RESULT);

            JSONArray jsonArray = resultObject.getJSONArray(VAR_OIL);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                String tradeDT = object.getString(VAR_TRADE_DT);
                String prod = object.getString(VAR_PRODNM);
                String prodName = "";

                if (prod.equals("휘발유")) {
                    prodName = PRODUCTS[0];
                } else if (prod.equals("자동차용경유")) {
                    prodName = PRODUCTS[1];
                } else if (prod.equals("고급휘발유")) {
                    prodName = PRODUCTS[2];
                } else if (prod.equals("자동차용부탄")) {
                    prodName = PRODUCTS[3];
                } else if (prod.equals("실내등유")) {
                    prodName = PRODUCTS[4];
                }

                String avgPrice = object.getString(VAR_PRICE);
                String vari_Value = object.getString(VAR_DIFF);

                AllAvgPrices avgPrices = new AllAvgPrices(
                        tradeDT, prodName, avgPrice, vari_Value);

                Log.i(MainActivity.TAG, "avgPrices[" + i + "] : " + avgPrices.toString());

                list.add(avgPrices);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<LocalAverage> read_LocalAverage_JSONData(String text) {
        List<LocalAverage> list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(text);
            JSONObject resultObject = jsonObject.getJSONObject(VAR_RESULT);

            JSONArray jsonArray = resultObject.getJSONArray(VAR_OIL);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                String prod = object.getString(VAR_PRODCD);

                String prodNM = "";
                for(int j = 0; j < PRODUCT_CODES.length; j++) {
                    if (prod.equals(PRODUCT_CODES[j])) {
                        prodNM = PRODUCTS[j];
                        break;
                    }
                }

                String avgPrice = object.getString(VAR_PRICE);
                String diff = object.getString(VAR_DIFF);

                LocalAverage avg = new LocalAverage(prodNM, avgPrice, diff);
                Log.i(MainActivity.TAG, "avg : " + avg);
                list.add(avg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<AvgLastWeek> read_AvgLastWeek_JSONData(String text) {
        List<AvgLastWeek> list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(text);
            JSONObject resultObject = jsonObject.getJSONObject(VAR_RESULT);

            JSONArray jsonArray = resultObject.getJSONArray(VAR_OIL);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                String week = object.getString(VAR_WEEK);
                String startDT = object.getString(VAR_STA_DT);
                String endDT = object.getString(VAR_END_DT);
                String areaCD = object.getString(VAR_AREA_CD);
                String prodCD = object.getString(VAR_PRODCD);
                String prodNM = "";

                for(int j = 0; j < PRODUCTS.length; j++) {
                    if(prodCD.equals(PRODUCT_CODES[j])) {
                        prodNM = PRODUCTS[j];
                    }
                }

                String avgPrice = object.getString(VAR_PRICE);

                AvgLastWeek alw = new AvgLastWeek(week, startDT, endDT, areaCD, prodNM, avgPrice);
                list.add(alw);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Avg7DaysPrices> read_Avg7Days_JSONData(String text) {
        List<Avg7DaysPrices> list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(text);
            JSONObject resultObject = jsonObject.getJSONObject(VAR_RESULT);

            JSONArray jsonArray = resultObject.getJSONArray(VAR_OIL);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String date = object.getString(VAR_DATE);
                String avgPrice = object.getString(VAR_PRICE);

                Avg7DaysPrices avg7DaysPrices = new Avg7DaysPrices(date, avgPrice);
                list.add(avg7DaysPrices);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
