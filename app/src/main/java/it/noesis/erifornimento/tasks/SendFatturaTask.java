package it.noesis.erifornimento.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import it.noesis.erifornimento.model.Cliente;
import it.noesis.erifornimento.model.Domicilio;
import it.noesis.erifornimento.model.Fattura;
import it.noesis.erifornimento.model.Sdi;
import it.noesis.erifornimento.utils.Constants;

public class SendFatturaTask extends AsyncTask<Fattura, Void, String> {

    private AsyncTaskCallbackContext<String> context;
    private final String userToken;
    private final String serverUrl;

    public SendFatturaTask(AsyncTaskCallbackContext<String> context , String userToken, String serverUrl){
        this.context = context;
        this.userToken = userToken;
        this.serverUrl = serverUrl;
    }



    @Override
    protected String doInBackground(Fattura... fatturas) {

        URL url = null;
        HttpURLConnection urlConnection = null;
        String result= null;
        InputStream stream = null;
        try {
            url = new URL(serverUrl + "/api/mobile/fatture");
        } catch (MalformedURLException e) {
            return e.getMessage();
        }

        // Create the urlConnection
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("x-auth", userToken);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput (true);
        } catch (IOException e) {
            return "Error in opening connection: " + e.getMessage();
        }


        try {
            urlConnection.connect();



            String json = new ObjectMapper().writeValueAsString(fatturas[0]);
            OutputStreamWriter out = new   OutputStreamWriter(urlConnection.getOutputStream());
            out.write(json);
            out.flush();
            out.close();



            int responseCode = urlConnection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                //throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = urlConnection.getInputStream();

            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = readStream(stream);
            }





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


       // return "ok";
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
