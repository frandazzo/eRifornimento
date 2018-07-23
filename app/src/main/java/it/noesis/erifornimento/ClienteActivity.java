package it.noesis.erifornimento;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import it.noesis.erifornimento.model.Anagrafica;
import it.noesis.erifornimento.model.Cliente;
import it.noesis.erifornimento.model.GeoFactory;
import it.noesis.erifornimento.model.Sdi;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;
import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

public class ClienteActivity extends AppCompatActivity {

    private Spinner spinnerNationality;
    private Spinner spinnerNation;
    private Spinner spinnerProvince;

    private EditText txtRagSoc;
    private EditText txtiva;
    private EditText txtcf;
    private EditText txtind;
    private EditText txtcom;
    private EditText txtcap;

    private EditText txtpec;
    private EditText txtcode;

    private Button btnOk;
    private Button btnCancel;

    private AwesomeValidation mAwesomeValidation;

    private Cliente cliente;

    private  GeoFactory geo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        mAwesomeValidation = new AwesomeValidation(UNDERLABEL);
        mAwesomeValidation.setContext(this);

        setupSpinners();
        setupTextEdits();

        retrieveClienteData();
        populateData();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeClickListeners();


        setupValidationRules();


    }

    private void setupValidationRules() {
        mAwesomeValidation.addValidation(this, R.id.txtRagSoc, RegexTemplate.NOT_EMPTY, R.string.emptyragione);
    }

    private void initializeClickListeners() {
        btnOk = ((Button) findViewById(R.id.btnOk));
        btnCancel = ((Button) findViewById(R.id.btnCancel));

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAwesomeValidation.clear();
                LoadClienteData();
                if (!mAwesomeValidation.validate()){

                    //mostro i campi dche non hanno passatyo la validazione
                    return;

                }

                try {
                    Intent intent = new Intent();
                    String json = new ObjectMapper().writeValueAsString(cliente);
                    intent.putExtra("data", json);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    Toast.makeText(ClienteActivity.this, "Errore nella serializzazione json dei dati inseriti: " + e.getMessage(), Toast.LENGTH_LONG );

                }



            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    private void LoadClienteData() {
        cliente.getAnag().setDenom(txtRagSoc.getText().toString());
        cliente.getAnag().setPiva(txtiva.getText().toString());
        cliente.getAnag().setCf(txtcf.getText().toString());

        cliente.getAnag().getDomFisc().setCap(txtcap.getText().toString());
        cliente.getAnag().getDomFisc().setCom(txtcom.getText().toString());
        cliente.getAnag().getDomFisc().setInd(txtind.getText().toString());

        cliente.getSdi().setPec(txtpec.getText().toString());
        cliente.getSdi().setCod(txtcode.getText().toString());

        cliente.getAnag().setNaz(getValueFromText(spinnerNationality.getSelectedItem().toString(), false));
        cliente.getAnag().getDomFisc().setNaz(getValueFromText(spinnerNation.getSelectedItem().toString(), false));
        cliente.getAnag().getDomFisc().setProv(getValueFromText(spinnerProvince.getSelectedItem().toString(), true));




    }



    private void populateData() {

        txtRagSoc.setText(cliente.getAnag().getDenom());
        txtiva.setText(cliente.getAnag().getPiva());
        txtcf.setText(cliente.getAnag().getCf());

        String nazionalita = cliente.getAnag().getNaz();
        int nazionalitaPosition = getPosition(nazionalita,false);
        if (nazionalitaPosition == -1)
            nazionalitaPosition = 122; //italia
        spinnerNationality.setSelection(nazionalitaPosition);


        txtcap.setText(cliente.getAnag().getDomFisc().getCap());
        txtcom.setText(cliente.getAnag().getDomFisc().getCom());
        txtind.setText(cliente.getAnag().getDomFisc().getInd());
        //indirizzo azienda  - nazione e provincia
        //in base al valore impostato devo recuperarne la posizione dell'elemento
        //selezionato all'interno dello spinner e selezionalrlo
        String provinciaAzienda = cliente.getAnag().getDomFisc().getProv();
        String nazioneAzienda = cliente.getAnag().getDomFisc().getNaz();

        int provinciaPosition = getPosition(provinciaAzienda,true);
        int nazionePosition = getPosition(nazioneAzienda,false);
        if (nazionePosition == -1)
            nazionePosition = 122; //italia
        spinnerNation.setSelection(nazionePosition);
        spinnerProvince.setSelection(provinciaPosition);

        txtpec.setText(cliente.getSdi().getPec());
        txtcode.setText(cliente.getSdi().getCod());

    }

    private int getPosition(String value, boolean forProvince) {

        List<String> dataIds = null;
        if (forProvince){
            dataIds = geo.getProvincesIds();
        }else
        {
            dataIds = geo.getNationsIds();
        }
        value = value.toUpperCase();

        //devo trovare a quale indice si trova l'elemento value
        int position = dataIds.indexOf(value);
        return position;
    }


    private String getValueFromText(String value, boolean forProvince) {

        List<String> dataIds = null;
        List<String> data = null;
        if (forProvince){
            dataIds = geo.getProvincesIds();
            data = geo.getProvinces();
        }else
        {
            dataIds = geo.getNationsIds();
            data = geo.getNations();
        }

        int position = data.indexOf(value);
        if (position == -1)
            return "";
        return dataIds.get(position);
    }

    private void retrieveClienteData() {
        try {
            cliente = getDataFromIntent();
        } catch (IOException e) {
            e.printStackTrace();
            cliente = new Cliente();
        }
    }

    private Cliente getDataFromIntent() throws IOException {

        String data = getIntent().getStringExtra("data");


        if (TextUtils.isEmpty(data))
            return new Cliente();

        return new ObjectMapper().readValue(data, Cliente.class);
    }

    private void setupTextEdits() {

        txtRagSoc = ((EditText) findViewById(R.id.txtRagSoc));
        txtRagSoc.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        txtiva = ((EditText) findViewById(R.id.txtiva));
        txtcf = ((EditText) findViewById(R.id.txtcf));
        txtcf.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        txtind = ((EditText) findViewById(R.id.txtind));
        txtind.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        txtcom = ((EditText) findViewById(R.id.txtcom));
        txtcom.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        txtcap = ((EditText) findViewById(R.id.txtcap));
        txtpec = ((EditText) findViewById(R.id.txtpec));
        txtcode = ((EditText) findViewById(R.id.txtcode));
        txtcode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

    }

    private void setupSpinners() {
         geo = new GeoFactory();

        spinnerNationality = (Spinner) findViewById(R.id.spnaz);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, geo.getNations());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNationality.setAdapter(adapter);
        spinnerNationality.setSelection(122);


        spinnerNation = (Spinner) findViewById(R.id.spnazs);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, geo.getNations());
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNation.setAdapter(adapter1);
        spinnerNation.setSelection(0);

        spinnerProvince = (Spinner) findViewById(R.id.spprov);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, geo.getProvinces());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(adapter2);
        spinnerProvince.setSelection(0);
    }
}
