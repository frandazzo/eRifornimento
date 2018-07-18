package it.noesis.erifornimento;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import it.noesis.erifornimento.model.Fattura;
import it.noesis.erifornimento.utils.CallbackContext;
import it.noesis.erifornimento.utils.Constants;

public class FatturaActivity extends AppCompatActivity implements CallbackContext<String>, NodataFragment.OnFragmentInteractionListener {

    private LinearLayout benzina;
    private LinearLayout metano;
    private LinearLayout gpl;
    private LinearLayout diesel;
    private LinearLayout targa;


    private TextView benzinaValue;
    private TextView metanoValue;
    private TextView gplValue;
    private TextView dieselValue;
    private TextView targaValue;

    private Fattura fattura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fattura);

        //inizializo l'istanza che conterrà i valori selezionati dall'utente
        fattura = initializeFatturaData(savedInstanceState);

        //inizializzo tutti i componeneti dell'interfaccia
        initInterface();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putString(Constants.DIALOG_TARGA, fattura.getTarga());

        outState.putDouble(Constants.DIALOG_BENZINA, fattura.getBenzina());
        outState.putDouble(Constants.DIALOG_DIESEL, fattura.getDiesel());
        outState.putDouble(Constants.DIALOG_GPL, fattura.getGpl());
        outState.putDouble(Constants.DIALOG_METANO, fattura.getMetano());



    }

    private Fattura initializeFatturaData(Bundle savedInstanceState) {
        Fattura f = new Fattura();

        if (savedInstanceState == null)
            return f;


        f.setTarga(savedInstanceState.getString(Constants.DIALOG_TARGA));

        f.setBenzina(savedInstanceState.getDouble(Constants.DIALOG_BENZINA));
        f.setDiesel(savedInstanceState.getDouble(Constants.DIALOG_DIESEL));
        f.setGpl(savedInstanceState.getDouble(Constants.DIALOG_GPL));
        f.setMetano(savedInstanceState.getDouble(Constants.DIALOG_METANO));

        return f;
    }

    private void initInterface() {

        targa = ((LinearLayout) findViewById(R.id.targa));
        benzina = ((LinearLayout) findViewById(R.id.benzina));
        gpl = ((LinearLayout) findViewById(R.id.gpl));
        diesel = ((LinearLayout) findViewById(R.id.diesel));
        metano = ((LinearLayout) findViewById(R.id.metano));

        benzinaValue =((TextView) findViewById(R.id.benzina_value));
        metanoValue =((TextView) findViewById(R.id.metano_value));
        gplValue =((TextView) findViewById(R.id.gpl_value));
        dieselValue =((TextView) findViewById(R.id.diesel_value));
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
        NodataFragment noData = new NodataFragment();

        f.beginTransaction().add(R.id.fragment_place, noData).commit();


        populateData();



    }

    private void populateData() {
        populateDoubleFileds(fattura.getBenzina(), benzinaValue);
        populateDoubleFileds(fattura.getMetano(), metanoValue);
        populateDoubleFileds(fattura.getGpl(), gplValue);
        populateDoubleFileds(fattura.getDiesel(), dieselValue);
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

                    fattura.setBenzina(value);
                    populateDoubleFileds(value, benzinaValue);
                    break;
                case  Constants.DIALOG_DIESEL:
                    Double value1 = 0d;
                    try
                    {
                        value1 = Double.parseDouble(returnData);
                    }catch (Exception e){

                    }

                    fattura.setDiesel(value1);
                    populateDoubleFileds(value1, dieselValue);
                    break;
                case  Constants.DIALOG_GPL:
                    Double value2 = 0d;
                    try
                    {
                        value2 = Double.parseDouble(returnData);
                    }catch (Exception e){

                    }

                    fattura.setGpl(value2);
                    populateDoubleFileds(value2, gplValue);
                    break;
                case  Constants.DIALOG_METANO:
                    Double value3 = 0d;
                    try
                    {
                        value3 = Double.parseDouble(returnData);
                    }catch (Exception e){

                    }

                    fattura.setMetano(value3);
                    populateDoubleFileds(value3, metanoValue);
                    break;
            }
    }

    private void populateFieldTarga(String returnData) {
        if (!TextUtils.isEmpty(returnData))
            targaValue.setText(returnData);
        else
            targaValue.setText("Nessuna targa impostata");
    }

    @Override
    public void onFragmentInteraction(String data) {
        Log.i(this.getClass().getName(), "on fragment interatcion log....");
    }
}
