package it.noesis.erifornimento;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import it.noesis.erifornimento.tasks.AsyncTaskCallbackContext;
import it.noesis.erifornimento.tasks.PingTask;
import it.noesis.erifornimento.utils.CallbackContext;
import it.noesis.erifornimento.utils.Constants;

public class FirstAccessActivity extends AppCompatActivity implements CallbackContext<String>, AsyncTaskCallbackContext<String> {


    private Button btnServer;
    private LinearLayout progressLayout;
    private AppCompatImageView image;

    private String selectedServerUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access);
        initInteface();
    }

    private void initInteface() {

        btnServer = ((Button) findViewById(R.id.btnServer));
        progressLayout = ((LinearLayout) findViewById(R.id.progressLayout));
        image = ((AppCompatImageView) findViewById(R.id.image));




        btnServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ServerDialogFragment frag = new ServerDialogFragment();

                Bundle args = new Bundle();
                args.putString(Constants.PREFERENCES_SERVER, getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE).getString(Constants.PREFERENCES_SERVER,""));
                frag.setArguments(args);

                frag.show(getSupportFragmentManager(), "Server");

            }
        });

        progressLayout.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onDialogDismiss(String returnData, String dialogTag) {

        //se ho digitato  una url procedo a testare il ping
        if (TextUtils.isEmpty(returnData)){
            selectedServerUrl = "";
            return;
        }
        selectedServerUrl = returnData;
        new PingTask(this).execute(new String[]{returnData});
    }

    @Override
    public void onPreExecute() {
        progressLayout.setVisibility(View.VISIBLE);

        btnServer.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
    }

    private void saveUrl(String serverUrl) {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PREFERENCES_SERVER, serverUrl);
        editor.commit();
    }

    @Override
    public void onPostExecute(String s) {

        try{
            progressLayout.setVisibility(View.GONE);
            btnServer.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);


            //se il risultato del ping è una stringa vuota allora
            //vuol deire che ho pingato correttamenteil server
            if ("ok".equals(s)){
                //qui posso andare all'activity di login
                saveUrl(selectedServerUrl);


                //a questo punto devo decidere se andare lla schermata di login o alla main activity
                //in base alla presenza di un token
                String token = getUserToken();
                if (TextUtils.isEmpty(token)){
                    Intent mainIntent = new Intent(this,Login2Activity.class);
                    startActivity(mainIntent);
                    finish();
                    return;
                }

                Intent mainIntent = new Intent(this,MainActivity.class);
                startActivity(mainIntent);
                finish();
                return;
            }

            //mostro un messaggio di errore
            Toast.makeText(FirstAccessActivity.this, "Server irraggiungibile: " + s, Toast.LENGTH_LONG).show();
        }catch (Exception ex){

        }

    }

    private String getUserToken() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        return prefs.getString(Constants.PREFERENCES_USER_TOKEN, "");
    }
}
