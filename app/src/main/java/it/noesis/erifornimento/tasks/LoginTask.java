package it.noesis.erifornimento.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import it.noesis.erifornimento.model.LoginCredentials;
import it.noesis.erifornimento.model.LoginTaskResult;
import it.noesis.erifornimento.model.User;
import it.noesis.erifornimento.utils.Constants;

public class LoginTask extends AsyncTask<Void, Void, LoginTaskResult> {

    private AsyncTaskCallbackContext<LoginTaskResult> context;
    private final String serverUrl;
    private final LoginCredentials credentials = new LoginCredentials();


    public LoginTask(AsyncTaskCallbackContext<LoginTaskResult> context, String username, String password, String serverUrl){

        this.context = context;
        this.serverUrl = serverUrl;
        credentials.setUsername(username);
        credentials.setPassword(password);
    }

    @Override
    protected void onPostExecute(LoginTaskResult loginTaskResult) {


        context.onPostExecute(loginTaskResult);

    }

    @Override
    protected void onCancelled() {
        LoginTaskResult loginTaskResult = new LoginTaskResult();
        loginTaskResult.setError("Cancellato");
        context.onPostExecute(loginTaskResult);
    }

    @Override
    protected LoginTaskResult doInBackground(Void... voids) {

        URL url = null;
        HttpURLConnection urlConnection = null;
        String result= null;
        InputStream stream = null;
        LoginTaskResult r = new LoginTaskResult();
        try {


            url = new URL(this.serverUrl + "/api/authenticate");
        } catch (MalformedURLException e) {

            LoginTaskResult res = new LoginTaskResult();
            res.setError(e.getMessage());

            return res;
        }

        // Create the urlConnection
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput (true);
        } catch (IOException e) {
            LoginTaskResult res = new LoginTaskResult();
            res.setError("Error in opening connection: " + e.getMessage());

            return res;
        }


        try {
            urlConnection.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", credentials.getUsername());
            jsonParam.put("password", credentials.getPassword());
            OutputStreamWriter out = new   OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonParam.toString());
            out.flush();
            out.close();





            int responseCode = urlConnection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = urlConnection.getInputStream();



            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = readStream(stream);

                JSONObject mainObject = new JSONObject(result);
                r.setError(mainObject.getString("error"));
                r.setUser(new User());
                if (TextUtils.isEmpty(r.getError())){

                    r.getUser().setToken(mainObject.getString("token"));
                    r.getUser().setUsername(mainObject.getString("role"));
                    r.getUser().setName(mainObject.getString("name"));

                }

            }





        } catch (IOException e) {
            LoginTaskResult res = new LoginTaskResult();
            res.setError("Error in executing connection: " + e.getMessage());

            return res;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
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


//        LoginTaskResult loginTaskResult = new LoginTaskResult();
//        if ("ciccillo".equals(username) && "password".equals(password)){
//
//            loginTaskResult.setUser(new User());
//            loginTaskResult.getUser().setToken("xxxxxxxxxxx");
//            loginTaskResult.getUser().setName("Ciccio");
//            loginTaskResult.getUser().setUsername("ciccillo");
//
//        }else{
//            loginTaskResult.setError("Username o password errati");
//        }


//        return loginTaskResult;
        return r;
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
}
