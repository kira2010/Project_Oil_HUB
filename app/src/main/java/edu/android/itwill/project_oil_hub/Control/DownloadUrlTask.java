package edu.android.itwill.project_oil_hub.Control;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.android.itwill.project_oil_hub.Activity.MainActivity;

public class DownloadUrlTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        Log.i(MainActivity.TAG, "doInBackground() 실행");
        StringBuilder builder = new StringBuilder();

        HttpURLConnection connection = null;
        InputStream in = null;
        InputStreamReader reader = null;
        BufferedReader br = null;

        try {
            URL url = new URL(params[0]);
            Log.i(MainActivity.TAG, "URL 생성");

            connection = (HttpURLConnection) url.openConnection();
            Log.i(MainActivity.TAG, "connection 설정");

            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setRequestMethod("GET");

            connection.connect();
            Log.i(MainActivity.TAG, "서버 연결 성공");

            int responseCode = connection.getResponseCode();
            Log.i(MainActivity.TAG, "응답 코드 : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                reader = new InputStreamReader(in);
                br = new BufferedReader(reader);

                String line = br.readLine();
                while (line != null) {
                    builder.append(line);
                    line = br.readLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.i(MainActivity.TAG, "doInBackground() 종료");
        return builder.toString();
    }
}
