package it.noesis.erifornimento;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.noesis.erifornimento.adapters.ClientiAdapter;
import it.noesis.erifornimento.model.Cliente;
import it.noesis.erifornimento.utils.Constants;

public class ClientiSearchActivity extends AppCompatActivity {

    private  String clientiJson;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clienti_search);

        mRecyclerView = (RecyclerView) findViewById(R.id.lista);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //in input alla activity ci devono essere una lista di clienti
        //precedentemente cercata
        if (getIntent().getExtras() != null){
            clientiJson = getIntent().getExtras().getString(Constants.CLIENTI_SEARCH);
        }else{
            clientiJson = savedInstanceState.getString(Constants.CLIENTI_SEARCH);
        }


        try {
            List<Cliente> clienti = new ObjectMapper().readValue(clientiJson,new TypeReference<List<Cliente>>(){});
            mAdapter = new ClientiAdapter(clienti);
        } catch (IOException e) {
            e.printStackTrace();
            mAdapter = new ClientiAdapter(new ArrayList<Cliente>());
        }
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putString(Constants.CLIENTI_SEARCH, clientiJson);


    }
}
