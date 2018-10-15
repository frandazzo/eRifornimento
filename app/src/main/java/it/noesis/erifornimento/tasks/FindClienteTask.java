package it.noesis.erifornimento.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import it.noesis.erifornimento.model.Anagrafica;
import it.noesis.erifornimento.model.Cliente;
import it.noesis.erifornimento.model.Domicilio;
import it.noesis.erifornimento.model.Sdi;

public class FindClienteTask extends AsyncTask<Void, Void, Cliente> {

    private ClienteAsyncTaskCallbackContent<Cliente> context;
    private final String userToken;
    private final String serverUrl;
    private String query;

    public FindClienteTask(ClienteAsyncTaskCallbackContent<Cliente> context , String userToken, String serverUrl, String query){
        this.context = context;
        this.userToken = userToken;
        this.serverUrl = serverUrl;
        this.query = query;
    }




    @Override
    protected Cliente doInBackground(Void... voids) {

        URL url = null;
        HttpURLConnection urlConnection = null;
        Cliente result= null;
        InputStream stream = null;
        try {
            url = new URL(serverUrl + "/api/mobile/clienti/" + query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        // Create the urlConnection
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer " + userToken);
            urlConnection.setDoInput(true);

        } catch (Exception e) {
            return null;
        }


        try {
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                return null;
            }
            // Retrieve the response body as an InputStream.
            stream = urlConnection.getInputStream();
            String stringResult = "";
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                stringResult = readStream(stream);
                result =  new ObjectMapper().readValue(stringResult, Cliente.class);
                Log.d(this.getClass().getName(), "doInBackground: " + stringResult);
            }



        }catch (SocketTimeoutException exc){
            exc.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
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

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        if (this.query.equals("01337380776")){
//            Cliente c = new Cliente();
//            c.setAnag(new Anagrafica());
//            c.setSdi(new Sdi());
//            c.setTarga("AK466AZ");
//            c.setDtGenQr(new Date());
//
//            c.getAnag().setNaz("IT");
//            c.getAnag().setCf("01337380776");
//            c.getAnag().setDenom("Noesis");
//            c.getAnag().setDomFisc(new Domicilio());
//            c.getAnag().getDomFisc().setProv("MT");
//            c.getAnag().getDomFisc().setCap("75100");
//            c.getAnag().getDomFisc().setCom("Matera");
//            c.getAnag().getDomFisc().setInd("Via gravina 39");
//            c.getAnag().getDomFisc().setNaz("ITALIA");
//            c.getAnag().setPiva("01337370776");
//
//
//            c.getSdi().setCod("66557788");
//            c.getSdi().setPec("noesis_srl@pec.it");
//
//            return c;
//        }
//        return null;
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
    protected void onPostExecute(Cliente cliente) {
        context.onPostExecuteClienteTask(cliente);
    }

    @Override
    protected void onPreExecute() {
        context.onPreExecute();
    }
}
