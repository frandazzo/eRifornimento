package it.noesis.erifornimento;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import it.noesis.erifornimento.utils.CallbackContext;
import it.noesis.erifornimento.utils.Constants;

public class SureToProceedDialogFragment extends DialogFragment {

    private ClienteFragment.OnClienteFragmentInteractionListener mCallback;




    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        try {
            mCallback = (ClienteFragment.OnClienteFragmentInteractionListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement custom CallbackContext interface.");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Domanda");
        builder.setMessage("Sicuro di voler ricaricare i dati?");



        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                mCallback.onCancelClienteData();

            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //non fa nulla

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

