package it.noesis.erifornimento;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import it.noesis.erifornimento.utils.CallbackContext;
import it.noesis.erifornimento.utils.Constants;

public class TargaDialogFragment extends DialogFragment {

    private CallbackContext<String>  mCallback;
    private String targa;



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
        targa = getArguments().getString(Constants.DIALOG_TARGA);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Targa automobile");
        builder.setMessage("Inserisci la targa dell'automobile");

        final EditText input = new EditText(getActivity());

        FrameLayout container = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        input.setLayoutParams(params);
        container.addView(input);
        input.setSelectAllOnFocus(true);
        input.selectAll();
        input.setSingleLine();
        input.setText(targa);
        input.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        builder.setView(container);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                mCallback.onDialogDismiss(input.getText().toString(), TargaDialogFragment.this.getTag());

            }
        })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mCallback.onDialogDismiss("", TargaDialogFragment.this.getTag());

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

