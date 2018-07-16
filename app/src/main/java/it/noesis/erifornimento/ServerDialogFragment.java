package it.noesis.erifornimento;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import it.noesis.erifornimento.utils.Constants;

import static android.content.Context.MODE_PRIVATE;

public class ServerDialogFragment extends DialogFragment {

    private CallbackContext  mCallback;
    private String serverUrl;





    public interface CallbackContext{
        void onDialogDismiss(String serverUrl);
    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        try {
            mCallback = (CallbackContext)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement custom CallbackContext interface.");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        serverUrl = getArguments().getString(Constants.PREFERENCES_SERVER);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Server URL");
        builder.setMessage("Inserisci l'url del server");

        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setLeft(20);
        input.setRight(20);
        input.setText(serverUrl);
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mCallback.onDialogDismiss(input.getText().toString());

                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mCallback.onDialogDismiss("");
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
