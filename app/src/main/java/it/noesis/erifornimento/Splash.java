package it.noesis.erifornimento;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import it.noesis.erifornimento.tasks.AsyncTaskCallbackContext;
import it.noesis.erifornimento.tasks.PingTask;
import it.noesis.erifornimento.utils.Constants;

public class Splash extends AppCompatActivity implements AsyncTaskCallbackContext<String> {


    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_splash);




        //per prima cosa verifico la presenza di un server impostato
        //se il server non è impostato allora redirigo magari in maniera ritardata all firstaccessactivity
        //in modo da definire l'url del server
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        String server = prefs.getString(Constants.PREFERENCES_SERVER, "");

        if (TextUtils.isEmpty(server)){
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Splash.this,FirstAccessActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
            return;
        }


        //altrimenti eseguo un ping al server per verificarne la connessione
        new PingTask(this).execute(new String[]{server});



    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(String result) {
        //se c'è un errore nella trasmissione dei dati redirigo alla first access activity
        //per fare reimpostare il server
        if (!TextUtils.isEmpty(result)){
            Intent mainIntent = new Intent(Splash.this,FirstAccessActivity.class);
            Splash.this.startActivity(mainIntent);
            Splash.this.finish();

            return;
        }
        //se non cè un errore  passo alla activity di login o alla main activity secondo che ci sia memorizzato un token

        String token = getUserToken();
        if (TextUtils.isEmpty(token)){
            Intent mainIntent = new Intent(Splash.this,Login2Activity.class);
            Splash.this.startActivity(mainIntent);
            Splash.this.finish();
            return;
        }

        Intent mainIntent = new Intent(Splash.this,MainActivity.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();

    }

    private String getUserToken() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        return prefs.getString(Constants.PREFERENCES_USER_TOKEN, "");
    }
}
