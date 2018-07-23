package it.noesis.erifornimento;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import it.noesis.erifornimento.utils.CallbackContext;
import it.noesis.erifornimento.utils.Constants;

import static android.content.Context.MODE_PRIVATE;

public class ServerDialogFragment extends DialogFragment {

    private CallbackContext<String> mCallback;
    private String serverUrl;



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


        FrameLayout container = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        container.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        container.setFocusableInTouchMode(true);

        input.setLayoutParams(params);
        input.setHint(R.string.httpserver);
        container.addView(input);


        input.setSelectAllOnFocus(true);


        input.setSingleLine();


        input.setText(serverUrl);
        input.clearFocus();
        builder.setView(container);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mCallback.onDialogDismiss(input.getText().toString(), ServerDialogFragment.this.getTag());

                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mCallback.onDialogDismiss("", ServerDialogFragment.this.getTag());
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
