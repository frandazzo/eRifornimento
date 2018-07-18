package it.noesis.erifornimento;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import it.noesis.erifornimento.utils.Constants;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NodataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NodataFragment extends Fragment {

    private static final int QR_SCANNER = 1;
    private OnFragmentInteractionListener mListener;
    private ImageView noDataImage;


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
                if ( resultCode == Activity.RESULT_OK){

                    String result = intent.getExtras().get(Constants.DATA).toString();
                    Log.i(getActivity().getClass().getName(), result);

                }else if (resultCode == Activity.RESULT_CANCELED){
                    if (intent == null){
                        return;
                    }
                    String result = intent.getExtras().get(Constants.ERROR).toString();
                    Log.e(getActivity().getClass().getName(), result);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView =  inflater.inflate(R.layout.fragment_nodata, container, false);

        noDataImage = ((ImageView) myFragmentView.findViewById(R.id.qrcode));
        noDataImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(NodataFragment.this.getClass().getName(), "clikkkooooo");

                Intent a = new Intent(getActivity(), QrScannerActivity.class);
                startActivityForResult(a, QR_SCANNER);


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
        void onFragmentInteraction(String data);
    }
}
