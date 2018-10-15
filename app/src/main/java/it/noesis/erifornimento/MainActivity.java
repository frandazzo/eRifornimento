package it.noesis.erifornimento;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncorti.slidetoact.SlideToActView;

import java.util.List;

import it.noesis.erifornimento.model.Cliente;
import it.noesis.erifornimento.tasks.AsyncTaskCallbackContext;
import it.noesis.erifornimento.tasks.ClienteAsyncTaskCallbackContent;
import it.noesis.erifornimento.tasks.ClientiAsyncTaskCallbackContent;
import it.noesis.erifornimento.tasks.FindClienteTask;
import it.noesis.erifornimento.tasks.FindClientiTask;
import it.noesis.erifornimento.tasks.PingTask;
import it.noesis.erifornimento.utils.CallbackContext;
import it.noesis.erifornimento.utils.Constants;

public class MainActivity extends AppCompatActivity implements ClienteAsyncTaskCallbackContent<Cliente>, ClientiAsyncTaskCallbackContent<List<Cliente>>, CallbackContext<String>, AsyncTaskCallbackContext<String> {

    private String serverUrl;

    //private Button btnFattura;
    //private Button btnServer;
    private LinearLayout progressLayout;
    private AppCompatImageView image;
    private SlideToActView sta;
    private View staContainer;
    private View questionlayout;

    EditText editText;
    Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        initInteface();

        if (getIntent().getExtras() != null){
            String loggedUser = getIntent().getExtras().getString(Constants.LOGGED_USERNAME);
            if ( loggedUser != null){
                Toast.makeText(this, "Benvenuto " + loggedUser, Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.menuserver:

                ServerDialogFragment frag = new ServerDialogFragment();
                Bundle args = new Bundle();
                args.putString(Constants.PREFERENCES_SERVER, getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE).getString(Constants.PREFERENCES_SERVER,""));
                frag.setArguments(args);
                frag.show(getSupportFragmentManager(), "Server");

                break;
            case R.id.menulogout:
                logout();
                Intent i = new Intent(this, Login2Activity.class);
                startActivity(i);
                finish();
                break;
        }

        return true;
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PREFERENCES_USER_TOKEN, "");
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);

        return true;
    }

    private void initInteface() {
       // btnFattura = ((Button) findViewById(R.id.btnFattura));
      //  btnServer = ((Button) findViewById(R.id.btnServer));
        progressLayout = ((LinearLayout) findViewById(R.id.progressLayout));
        image = ((AppCompatImageView) findViewById(R.id.image));
        sta = (SlideToActView) findViewById(R.id.example);
        staContainer = findViewById(R.id.sta_container);
        questionlayout = findViewById(R.id.question);

        editText = (EditText)findViewById(R.id.text);
        btnClear = (Button)findViewById(R.id.btn_clear);
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText(""); //clear edittext
            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!editText.getText().toString().equals("")) { //if edittext include text
                    btnClear.setVisibility(View.VISIBLE);

                } else { //not include text
                    btnClear.setVisibility(View.GONE);


                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {

                String clientiQuery = editText.getText().toString();
                if (TextUtils.isEmpty(clientiQuery)){
                    //se non ho inserito alcun parametro di ricerco avvio l'activity per la creazione di una nuova fattura
                    startNewFatturaActivity();
                    sta.resetSlider();
                    return;
                }
                final String serverUrl = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE).getString(Constants.PREFERENCES_SERVER,"");

                //altrimenti verifico se si tratta di una partita iva o di una targa
                //e chiamo i rispettivi task per effettuare una ricerca
                if (clientiQuery.length() == 11){
                    //sto ricercando per partita iva...
                    //new PingTask(MainActivity.this).execute(new String[]{serverUrl});
                   new FindClienteTask(MainActivity.this,getUserToken(), serverUrl, clientiQuery).execute();
                   return;
                }

                //sto ricercando per targa....
                new FindClientiTask(MainActivity.this,getUserToken(), serverUrl).execute(new String[]{clientiQuery});

            }
        });

        progressLayout.setVisibility(View.INVISIBLE);


    }

    private String getUserToken() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        return prefs.getString(Constants.PREFERENCES_USER_TOKEN, "");
    }

    private void startNewFatturaActivity() {
        Intent i = new Intent(MainActivity.this, FatturaActivity.class);
        startActivityForResult(i, 1);
    }

    private void startNewFatturaActivity(Cliente cliente) {
        Intent i = new Intent(MainActivity.this, FatturaActivity.class);

        if (cliente != null) {
            try {
                i.putExtra(Constants.CLIENTE, new ObjectMapper().writeValueAsString(cliente));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        startActivityForResult(i, 1);
    }

    private void startNewFatturaActivity(String clienteJson) {
        Intent i = new Intent(MainActivity.this, FatturaActivity.class);

        if (!TextUtils.isEmpty(clienteJson)) {

            i.putExtra(Constants.CLIENTE, clienteJson);

        }

        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        if (requestCode == 1){ //invio fattura

            if (resultCode == RESULT_OK){
                String result = data.getExtras().getString("data");
                Toast.makeText(this,"Invio effettuato con successo: " + result, Toast.LENGTH_LONG).show();
            }

        }else if (requestCode == 2){ //ricerca clienti
            if (resultCode == RESULT_OK && data != null){
                String result = data.getExtras().getString(Constants.CLIENTE);
                //qui  reindirizzo allactivity della fattura
                startNewFatturaActivity(result);
            }else {
                editText.setText("");
            }
        }
    }

//    private void CheckServerStatus() {
//        //devo verifcare se tra le shared resources cè quella relativa all'url delc server impostata
//        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
//        String server = prefs.getString(Constants.PREFERENCES_SERVER, "");
////        if (TextUtils.isEmpty(server)){
////            btnFattura.setEnabled(false);
////        }else{
////            btnFattura.setEnabled(true);
////        }
//    }


    private void saveUrl(String serverUrl) {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PREFERENCES_SERVER, serverUrl);
        editor.commit();
    }

    @Override
    public void onPreExecute() {
        ShowProogress();
    }

    @Override
    public void onPostExecuteClientiTask(List<Cliente> clientes) {
        hideprogress();
        startRicercaClientiActivity(clientes);
        sta.resetSlider();
        editText.setText("");


    }

    private void startRicercaClientiActivity(List<Cliente> clientes) {


        if (clientes != null && clientes.size() > 0) {
            startRicercaClienti(clientes);
        }else{
            startNewFatturaActivity();
        }


    }

    private void startRicercaClienti(List<Cliente> clientes) {
        Intent i = new Intent(MainActivity.this, ClientiSearchActivity.class);
        try {
            i.putExtra(Constants.CLIENTI_SEARCH, new ObjectMapper().writeValueAsString(clientes));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        startActivityForResult(i, 2);
    }

    @Override
    public void onPostExecuteClienteTask(Cliente cliente) {
        hideprogress();
        startNewFatturaActivity(cliente);
        sta.resetSlider();
        editText.setText("");
    }

    private void ShowProogress() {
        progressLayout.setVisibility(View.VISIBLE);
        staContainer.setVisibility(View.GONE);
        questionlayout.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
    }

    @Override
    public void onPostExecute(String s) {

        hideprogress();

        //se è una stringa vuota il risultato del ping allora
        //il server ha risposto altrimenti cè un errore
        if ("ok".equals(s)){
            sta.setLocked(false);
            sta.setText("Nuova fattura");
            Toast.makeText(MainActivity.this, "Server impostato correttamente",  Toast.LENGTH_SHORT).show();
            saveUrl(serverUrl);
        }else{
            sta.setLocked(true);
            sta.setText("Bloccato");
            Toast.makeText(MainActivity.this, "Ping al server non riuscito: " + s,  Toast.LENGTH_LONG).show();
            serverUrl = "";
        }

      //  CheckServerStatus();

    }

    private void hideprogress() {
        progressLayout.setVisibility(View.GONE);
        staContainer.setVisibility(View.VISIBLE);
        questionlayout.setVisibility(View.VISIBLE);
        image.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDialogDismiss(String returnData, String dialogTag) {
        serverUrl = returnData;
        if (TextUtils.isEmpty(returnData)){
            //CheckServerStatus();
            return;
        }


        new PingTask(this).execute(new String[]{returnData});
    }
}
