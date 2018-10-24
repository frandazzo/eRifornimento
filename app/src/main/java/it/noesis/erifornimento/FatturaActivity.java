package it.noesis.erifornimento;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import it.noesis.erifornimento.model.Cliente;
import it.noesis.erifornimento.model.Fattura;
import it.noesis.erifornimento.tasks.AsyncTaskCallbackContext;
import it.noesis.erifornimento.tasks.ClienteAsyncTaskCallbackContent;
import it.noesis.erifornimento.tasks.SendFatturaTask;
import it.noesis.erifornimento.utils.CallbackContext;
import it.noesis.erifornimento.utils.Constants;

public class FatturaActivity extends AppCompatActivity implements  AsyncTaskCallbackContext<String> , CallbackContext<String>, NodataFragment.OnFragmentInteractionListener, ClienteFragment.OnClienteFragmentInteractionListener {

    private ImageView benzina;
    private ImageView metano;
    private ImageView gpl;
    private ImageView diesel;
    private LinearLayout targa;

    private LinearLayout progressLayout;
    private LinearLayout container;
    private LinearLayout supercontainer;

    private TextView totaleValue;
//    private TextView benzinaValue;
//    private TextView metanoValue;
//    private TextView gplValue;
//    private TextView dieselValue;
    private TextView targaValue;
    //private Toolbar mToolbar;

    private Fattura fattura;

    private ImageView pagaContanti;
    private ImageView pagaCard;

    private String getUserToken() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        return prefs.getString(Constants.PREFERENCES_USER_TOKEN, "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fattura);

//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("Nuova fattura");

        container = ((LinearLayout) findViewById(R.id.container));
        supercontainer = ((LinearLayout) findViewById(R.id.supercontainer));
        progressLayout = ((LinearLayout) findViewById(R.id.progressLayout));
        progressLayout.setVisibility(View.GONE);

        //inizializo l'istanza che conterrà i valori selezionati dall'utente
        fattura = initializeFatturaData(savedInstanceState);

        pagaContanti = ((ImageView) findViewById(R.id.pagacontanti));
        pagaCard = ((ImageView) findViewById(R.id.pagacard));


        final String serverUrl = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE).getString(Constants.PREFERENCES_SERVER,"");

        pagaContanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fattura.setTipoPagamento(0);
                new SendFatturaTask(FatturaActivity.this, getUserToken(), serverUrl).execute(fattura);

            }
        });
        pagaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fattura.setTipoPagamento(1);
                new SendFatturaTask(FatturaActivity.this, getUserToken(), serverUrl).execute(fattura);

            }
        });

        //inizializzo tutti i componeneti dell'interfaccia
        initInterface();
        enableSendFatturaButtons();
    }


    private boolean checkFatturaState(){
        if (fattura == null)
            return false;
        return fattura.isValid();
    }
    private void setSendFatturaButtonsImages() {
        if (pagaContanti.isEnabled() && pagaCard.isEnabled()) {
            pagaContanti.setImageResource(R.drawable.ic_contanti);
            pagaCard.setImageResource(R.drawable.ic_card);
        }
        else {
            pagaContanti.setImageResource(R.drawable.ic_contanti_disabled);
            pagaCard.setImageResource(R.drawable.ic_card_disabled);
        }
    }
    private void enableSendFatturaButtons(){
        pagaCard.setEnabled(checkFatturaState());
        pagaContanti.setEnabled(checkFatturaState());
        setSendFatturaButtonsImages();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putString(Constants.DIALOG_TARGA, fattura.getTarga());
        outState.putDouble(Constants.DIALOG_BENZINA, fattura.getBenzina());
        outState.putDouble(Constants.DIALOG_DIESEL, fattura.getDiesel());
        outState.putDouble(Constants.DIALOG_GPL, fattura.getGpl());
        outState.putDouble(Constants.DIALOG_METANO, fattura.getMetano());

        if (fattura.getCliente() != null){

            try {
                String json = new ObjectMapper().writeValueAsString(fattura.getCliente());
                outState.putString(Constants.CLIENTE, json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();

            }

        }


    }

    private Fattura initializeFatturaData(Bundle savedInstanceState) {
        Fattura f = new Fattura();

        //se l'attività main ha inviato un cliente lo inizializzo
        if (getIntent().getExtras()!= null){
            String clienteJson = getIntent().getExtras().getString(Constants.CLIENTE);
            try {
                Cliente c = new ObjectMapper().readValue(clienteJson, Cliente.class);
                f.setCliente(c);
                f.setTarga(c.getTarga());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        if (savedInstanceState == null)
            return f;


        f.setTarga(savedInstanceState.getString(Constants.DIALOG_TARGA));

        f.setBenzina(savedInstanceState.getDouble(Constants.DIALOG_BENZINA));
        f.setDiesel(savedInstanceState.getDouble(Constants.DIALOG_DIESEL));
        f.setGpl(savedInstanceState.getDouble(Constants.DIALOG_GPL));
        f.setMetano(savedInstanceState.getDouble(Constants.DIALOG_METANO));

        String c = savedInstanceState.getString(Constants.CLIENTE);
        if (!TextUtils.isEmpty(c)){
            try {
                Cliente cliente = new ObjectMapper().readValue(c, Cliente.class);
                f.setCliente(cliente);

            } catch (IOException e) {
                return f;
            }
        }


        return f;
    }

    private void initInterface() {

        targa = ((LinearLayout) findViewById(R.id.targa));
        benzina = ((ImageView) findViewById(R.id.benzina));
        gpl = ((ImageView) findViewById(R.id.gpl));
        diesel = ((ImageView) findViewById(R.id.diesel));
        metano = ((ImageView) findViewById(R.id.metano));


        totaleValue =((TextView) findViewById(R.id.totale_value));

//        benzinaValue =((TextView) findViewById(R.id.benzina_value));
//        metanoValue =((TextView) findViewById(R.id.metano_value));
//        gplValue =((TextView) findViewById(R.id.gpl_value));
//        dieselValue =((TextView) findViewById(R.id.diesel_value));
        targaValue =((TextView) findViewById(R.id.targa_value));


        targa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TargaDialogFragment frag = new TargaDialogFragment();

                Bundle args = new Bundle();
                args.putString(Constants.DIALOG_TARGA, fattura.getTarga());
                frag.setArguments(args);


                frag.show(getSupportFragmentManager(), Constants.DIALOG_TARGA);


            }
        });

        benzina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DoublePickerDialogFragment frag = new DoublePickerDialogFragment();

                Bundle args = new Bundle();
                args.putString(Constants.DIALOG_TYPE, Constants.DIALOG_BENZINA);
                args.putDouble(Constants.DIALOG_VALUE, fattura.getBenzina());
                frag.setArguments(args);


                frag.show(getSupportFragmentManager(), Constants.DIALOG_BENZINA);


            }
        });

        gpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DoublePickerDialogFragment frag = new DoublePickerDialogFragment();

                Bundle args = new Bundle();
                args.putString(Constants.DIALOG_TYPE, Constants.DIALOG_GPL);
                args.putDouble(Constants.DIALOG_VALUE, fattura.getGpl());
                frag.setArguments(args);


                frag.show(getSupportFragmentManager(), Constants.DIALOG_GPL);



            }
        });

        metano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DoublePickerDialogFragment frag = new DoublePickerDialogFragment();

                Bundle args = new Bundle();
                args.putString(Constants.DIALOG_TYPE, Constants.DIALOG_METANO);
                args.putDouble(Constants.DIALOG_VALUE, fattura.getMetano());
                frag.setArguments(args);


                frag.show(getSupportFragmentManager(), Constants.DIALOG_METANO);


            }
        });

        diesel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DoublePickerDialogFragment frag = new DoublePickerDialogFragment();

                Bundle args = new Bundle();
                args.putString(Constants.DIALOG_TYPE, Constants.DIALOG_DIESEL);
                args.putDouble(Constants.DIALOG_VALUE, fattura.getDiesel());
                frag.setArguments(args);

                frag.show(getSupportFragmentManager(), Constants.DIALOG_DIESEL);


            }
        });

        //imposto il fragment iniziale
        FragmentManager f =  getFragmentManager();
        if (fattura.getCliente() == null){

            NodataFragment noData = new NodataFragment();

            f.beginTransaction().replace(R.id.fragment_place, noData).commit();
        }else{
            ClienteFragment ff = new ClienteFragment();
            Bundle bundle = new Bundle();
            try {
                String json = new ObjectMapper().writeValueAsString(fattura.getCliente());
                bundle.putString("data", json);
                ff.setArguments(bundle);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }


            f.beginTransaction().replace(R.id.fragment_place, ff).commit();
        }



        populateData();



    }

    private void populateData() {
        populateDoubleFileds(fattura.calculateTotal(), totaleValue);
//        populateDoubleFileds(fattura.getMetano(), metanoValue);
//        populateDoubleFileds(fattura.getGpl(), gplValue);
//        populateDoubleFileds(fattura.getDiesel(), dieselValue);
        populateFieldTarga(fattura.getTarga());

    }

    private void populateDoubleFileds(Double value, TextView view) {
        if (value == null)
            view.setText("0,00 Euro");
        else
            view.setText(String.format("%s Euro", value.toString()));
    }




    @Override
    public void onDialogDismiss(String returnData, String dialogTag) {
            switch (dialogTag){
                case  Constants.DIALOG_TARGA:


                    //qui avvio un task asincrono per la verifica dell'esistenza
                    //nel database di una targa e quindi dei dati del cliente associato






                    fattura.setTarga(returnData);
                    populateFieldTarga(returnData);
                    break;
                case  Constants.DIALOG_BENZINA:
                    Double value = 0d;
                    try
                    {
                        value = Double.parseDouble(returnData);
                    }catch (Exception e){

                    }
                    if (value == 0d){
                        benzina.setImageResource(R.drawable.ic_benzina);
                    }else{
                        benzina.setImageResource(R.drawable.ic_benzina_checked);
                    }
                    fattura.setBenzina(value);
                    populateDoubleFileds(fattura.calculateTotal(), totaleValue);
                    break;
                case  Constants.DIALOG_DIESEL:
                    Double value1 = 0d;
                    try
                    {
                        value1 = Double.parseDouble(returnData);
                    }catch (Exception e){

                    }
                    if (value1 == 0d){
                        diesel.setImageResource(R.drawable.ic_diesel);
                    }else{
                        diesel.setImageResource(R.drawable.ic_diesel_checked);
                    }

                    fattura.setDiesel(value1);
                    populateDoubleFileds(fattura.calculateTotal(), totaleValue);
                    break;
                case  Constants.DIALOG_GPL:
                    Double value2 = 0d;
                    try
                    {
                        value2 = Double.parseDouble(returnData);
                    }catch (Exception e){

                    }
                    if (value2 == 0d){
                        gpl.setImageResource(R.drawable.ic_gpl);
                    }else{
                        gpl.setImageResource(R.drawable.ic_gpl_checked);
                    }

                    fattura.setGpl(value2);
                    populateDoubleFileds(fattura.calculateTotal(), totaleValue);
                    break;
                case  Constants.DIALOG_METANO:
                    Double value3 = 0d;
                    try
                    {
                        value3 = Double.parseDouble(returnData);
                    }catch (Exception e){

                    }

                    if (value3 == 0d){
                        metano.setImageResource(R.drawable.ic_metano);
                    }else{
                        metano.setImageResource(R.drawable.ic_metano_checked);
                    }

                    fattura.setMetano(value3);
                    populateDoubleFileds(fattura.calculateTotal(), totaleValue);
                    break;
            }

//        sendFattura.setEnabled(enableSendFatturaButton());
        enableSendFatturaButtons();


    }

    private void populateFieldTarga(String returnData) {
        if (!TextUtils.isEmpty(returnData))
            targaValue.setText(returnData);
        else
            targaValue.setText("Nessuna targa impostata");
    }

    @Override
    public void onCancelClienteData() {
        fattura.setCliente(null);
        FragmentManager f =  getFragmentManager();
        NodataFragment noData = new NodataFragment();

        f.beginTransaction().replace(R.id.fragment_place, noData).commit();
//        sendFattura.setEnabled(enableSendFatturaButton());
        enableSendFatturaButtons();

    }

    @Override
    public void onFragmentInteraction(Cliente cliente, String rawData, boolean isError) {


        if (isError){
            fattura.setCliente(null);
            Toast.makeText(this, "Codice QR non valido: " + rawData, Toast.LENGTH_LONG).show();
            return;
        }

        fattura.setCliente(cliente);
        FragmentManager f =  getFragmentManager();
        ClienteFragment frag = new ClienteFragment();

        Bundle bundle = new Bundle();
        try {
            bundle.putString("data", new ObjectMapper().writeValueAsString(cliente));
        } catch (JsonProcessingException e) {
            Toast.makeText(this, "Errore nel passaggio dati del cliente al fragmente: "+ e.getMessage(), Toast.LENGTH_LONG);
            return;
        }
        frag.setArguments(bundle);
        f.beginTransaction().replace(R.id.fragment_place, frag).commit();


//        sendFattura.setEnabled(enableSendFatturaButton());
        enableSendFatturaButtons();
    }

    @Override
    public void onPreExecute() {
        progressLayout.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);

    }



    @Override
    public void onPostExecute(String s) {
        progressLayout.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        params.gravity = Gravity.FILL;
//
//        supercontainer.setLayoutParams(params);

        if (s.equals("ok")){
            Intent i = new Intent();
            i.putExtra("data", s);
            setResult(RESULT_OK, i);
            finish();
            return;
        }

        Toast.makeText(FatturaActivity.this,s, Toast.LENGTH_LONG).show();
    }


//
}
