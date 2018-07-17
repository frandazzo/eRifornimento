package it.noesis.erifornimento;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import it.noesis.erifornimento.utils.CallbackContext;
import it.noesis.erifornimento.utils.Constants;

public class DoublePickerDialogFragment extends DialogFragment {

    private CallbackContext<String> mCallback;
    private String dialogType;
    private Double value;


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
        dialogType = getArguments().getString(Constants.DIALOG_TYPE);
        value = getArguments().getDouble(Constants.DIALOG_VALUE);

        if (value == null)
            value = 0d;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(String.format("Importo %s", dialogType));
        builder.setMessage("Inserisci l'importo");

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
        input.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(value.toString());
        input.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        builder.setView(container);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                mCallback.onDialogDismiss(input.getText().toString(), DoublePickerDialogFragment.this.getTag());

            }
        })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mCallback.onDialogDismiss("0", DoublePickerDialogFragment.this.getTag());

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}


