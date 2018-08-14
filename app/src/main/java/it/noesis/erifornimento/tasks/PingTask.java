package it.noesis.erifornimento.tasks;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;

public class PingTask extends AsyncTask<String, Void, String> {


    private AsyncTaskCallbackContext<String> context;

    public PingTask(AsyncTaskCallbackContext<String> context){

        this.context = context;
    }


    @Override
    protected String doInBackground(String... strings) {

        URL url = null;
        HttpURLConnection urlConnection = null;
        String result= null;
        InputStream stream = null;
        try {
            url = new URL(strings[0] + "/api/mobile/ping");
        } catch (MalformedURLException e) {
            return e.getMessage();
        }

        // Create the urlConnection
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("x-auth", "XXXXXXXXXXXXXx");
            urlConnection.setDoInput(true);
        } catch (IOException e) {
            return "Error in opening connection: " + e.getMessage();
        }


        try {
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = urlConnection.getInputStream();

            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = readStream(stream);
            }



        }catch (SocketTimeoutException exc){
            return "Timeout opening connection: " + exc.getMessage();
        } catch (IOException e) {
            return "Error executing request: " + e.getMessage();
        }finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;



    }

    public String readStream(InputStream stream) throws IOException {

        int maxReadSize = 1024;
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] rawBuffer = new char[maxReadSize];
        int readSize;
        StringBuffer buffer = new StringBuffer();
        while (((readSize = reader.read(rawBuffer)) != -1)) {
            if (readSize > maxReadSize) {
                readSize = maxReadSize;
            }
            buffer.append(rawBuffer, 0, readSize);
        }
        return buffer.toString();
    }


    @Override
    protected void onPostExecute(String s) {


        context.onPostExecute(s);

    }


    @Override
    protected void onPreExecute() {
         context.onPreExecute();
    }



}
