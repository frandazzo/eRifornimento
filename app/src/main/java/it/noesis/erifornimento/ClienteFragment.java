package it.noesis.erifornimento;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.noesis.erifornimento.model.Cliente;
import it.noesis.erifornimento.utils.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClienteFragment extends Fragment {


    private TextView txtRagSoc;
    private TextView txtiva;
    private TextView txtcf;
    private TextView txtnaz;
    private TextView txtind;
    private TextView txtpec;
    private TextView txtcode;
    private TextView txtqr;

    private Button btnCancel;
    private Button btnEdit;



    private OnClienteFragmentInteractionListener mListener;

    public ClienteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NodataFragment.OnFragmentInteractionListener) {
            mListener = (OnClienteFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnClienteFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        switch (requestCode){

            case Constants.ACTIVITY_FOR_RESULT_CLIENTE:
                if ( resultCode == Activity.RESULT_OK){

                    String result = intent.getExtras().get(Constants.DATA).toString();

                    //tento la conversione da json ad un oggetto java
                    try {
                        Cliente f = new ObjectMapper().readValue(result, Cliente.class);

                        mListener.onFragmentInteraction(f, result, false);

                    } catch (IOException e) {
                        mListener.onFragmentInteraction(null, result, true);
                    }



                }else if (resultCode == Activity.RESULT_CANCELED){
                    if (intent == null){
                        return;
                    }

                }

                break;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_cliente, container, false);

        final String data = getArguments().getString("data");

        txtRagSoc = ((TextView) view.findViewById(R.id.txtRagSoc));

        txtiva = ((TextView) view.findViewById(R.id.txtiva));
        txtcf = ((TextView) view.findViewById(R.id.txtcf));

        txtnaz = ((TextView) view.findViewById(R.id.txtnaz));
        txtind = ((TextView) view.findViewById(R.id.txtind));
        txtpec = ((TextView) view.findViewById(R.id.txtpec));
        txtcode = ((TextView) view.findViewById(R.id.txtcode));
        txtqr = ((TextView) view.findViewById(R.id.txtqr));

        btnCancel = (Button)view.findViewById(R.id.btnCancel);
        btnEdit = (Button)view.findViewById(R.id.btnEdit);

         Cliente cliente = null;
        try {
            cliente = new ObjectMapper().readValue(data, Cliente.class);
        } catch (IOException e) {
            e.printStackTrace();
        }



        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ClienteActivity.class);
                i.putExtra("data", data);
                startActivityForResult(i, Constants.ACTIVITY_FOR_RESULT_CLIENTE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SureToProceedDialogFragment frag = new SureToProceedDialogFragment();
                frag.show(getActivity().getFragmentManager(), "Question");

                //mListener.onCancelClienteData();
            }
        });


        txtRagSoc.setText(cliente != null?cliente.getAnag().getDenom():"");
        txtiva.setText(cliente != null?cliente.getAnag().getPiva():"");
        txtcf.setText(cliente != null?cliente.getAnag().getCf():"");
        txtnaz.setText(cliente != null?cliente.getAnag().getNaz():"");
        txtind.setText(cliente != null?cliente.getAnag().getDomFisc().toString():"");
        txtpec.setText(cliente != null?cliente.getSdi().getPec():"");
        txtcode.setText(cliente != null?cliente.getSdi().getCod():"");

        Date d = null;
        if (cliente != null)
            if (cliente.getDtGenQr() != null)
                d = cliente.getDtGenQr();

        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        txtqr.setText(d != null?f.format(d) :"");

        return view;
    }

    public interface OnClienteFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCancelClienteData();
        void onFragmentInteraction(Cliente cliente, String rawData, boolean isError);
    }

}

