package com.example.chatchatapplication.Not_Activity;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SimpleHttpTask extends AsyncTask<String, Void, String> {
    private jsonBack delegate = null;
    private HttpURLConnection conn;

    public SimpleHttpTask(jsonBack delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected String doInBackground(String... json) {
        StringBuilder result = new StringBuilder();

        try {
            //HttpURLconnection methods
//            URL url = new URL(" http://172.20.10.5/Application-Real/CentralController.php");
//            URL url = new URL("http://192.168.43.54/Application-Real/CentralController.php");
//            URL url = new URL("http://192.168.43.143/Application-Real/CentralController.php");
            URL url = new URL("http://192.168.1.38/Application-Real/CentralController.php");
//            URL url = new URL("http://172.17.1.70/Application-Real/CentralController.php");

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("json", json[0]));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            int status_code = conn.getResponseCode();
            if (status_code == HttpURLConnection.HTTP_OK) {

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } else {
                String line;
                line = String.valueOf(status_code);
                result.append(line);
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return result.toString();
    }

    protected void onPostExecute(final String jsonString) {
        delegate.processFinish(jsonString);
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}