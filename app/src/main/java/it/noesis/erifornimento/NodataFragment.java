package it.noesis.erifornimento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import it.noesis.erifornimento.model.Cliente;
import it.noesis.erifornimento.utils.Constants;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NodataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NodataFragment extends Fragment {

    private static final int QR_SCANNER = 1;
    private static final int QR_SCANNER1 = 2;
    private static final int QR_SCANNER2 = 3;


    private OnFragmentInteractionListener mListener;
    private ImageView noDataImage;
    private ImageView noDataImage1;
    private ImageView noDataImage2;
    private ImageView edit;

//    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";


//    public void scanQR() {
//
//        try {
//
//            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
//
//            Intent intent = new Intent(ACTION_SCAN);
//
//            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//
//            startActivityForResult(intent, 0);
//
//        } catch (ActivityNotFoundException anfe) {
//
//            //on catch, show the download dialog
//
//            showDialog(NodataFragment.this.getActivity(), "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
//
//        }
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode){
            case QR_SCANNER:
            case QR_SCANNER1:
            case QR_SCANNER2:
                if ( resultCode == Activity.RESULT_OK){

                    String result = intent.getExtras().get(Constants.DATA).toString();

                    //tento la conversione da json ad un oggetto java
                    try {
                        Cliente f = new ObjectMapper().
                        readerFor(Cliente.class)
                        .readValue(result);

                        mListener.onFragmentInteraction(f, result, false);

                    } catch (IOException e) {
                        mListener.onFragmentInteraction(null, result, true);
                    }


                }else if (resultCode == Activity.RESULT_CANCELED){
                    if (intent == null){
                        return;
                    }
                    String result = intent.getExtras().get(Constants.ERROR).toString();
                    mListener.onFragmentInteraction(null, result, true);
                }

                break;



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



//    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
//
//        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
//
//        downloadDialog.setTitle(title);
//
//        downloadDialog.setMessage(message);
//
//        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
//
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//
//                try {
//
//                    act.startActivity(intent);
//
//                } catch (ActivityNotFoundException anfe) {
//
//
//
//                }
//
//            }
//
//        });
//
//        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//
//        });
//
//        return downloadDialog.show();
//
//    }



    public NodataFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView =  inflater.inflate(R.layout.fragment_nodata, container, false);


        noDataImage2 = ((ImageView) myFragmentView.findViewById(R.id.qrcode2));


        edit = ((ImageView) myFragmentView.findViewById(R.id.edit));


        noDataImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent a = new Intent(getActivity(), NisrulsNativeQrScannerActivity.class);
                startActivityForResult(a, QR_SCANNER2);


            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ClienteActivity.class);
                startActivityForResult(i, Constants.ACTIVITY_FOR_RESULT_CLIENTE);
            }
        });

        return myFragmentView;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Cliente cliente, String rawData, boolean isError);
    }
}
