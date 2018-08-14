package it.noesis.erifornimento.tasks;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import it.noesis.erifornimento.model.Cliente;

public class FindClientiTask extends AsyncTask<String, Void, List<Cliente>> {

    private ClientiAsyncTaskCallbackContent<List<Cliente> > context;
    private final String userToken;
    private final String serverUrl;

    public FindClientiTask(ClientiAsyncTaskCallbackContent<List<Cliente>> context , String userToken, String serverUrl){
        this.context = context;
        this.userToken = userToken;
        this.serverUrl = serverUrl;
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
    protected List<Cliente> doInBackground(String... strings) {
        String targa = strings[0];

        URL url = null;
        HttpURLConnection urlConnection = null;
        List<Cliente> result= new ArrayList<Cliente>();
        InputStream stream = null;
        try {
            url = new URL(serverUrl + "/api/mobile/clienti?targa=" + URLEncoder.encode(targa, "UTF-8"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        // Create the urlConnection
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("x-auth", userToken);
            urlConnection.setDoInput(true);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }


        try {
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {

                return new ArrayList<>();
            }
            // Retrieve the response body as an InputStream.
            stream = urlConnection.getInputStream();
            String stringResult = "";
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                stringResult = readStream(stream);
                result =  new ObjectMapper().readValue(stringResult,new TypeReference<List<Cliente>>(){});
            }



        }catch (SocketTimeoutException exc){
            return null;
        } catch (IOException e) {
            return null;
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


    @Override
    protected void onPostExecute(List<Cliente> clienti) {
        context.onPostExecuteClientiTask(clienti);
    }

    @Override
    protected void onPreExecute() {
        context.onPreExecute();
    }
}
