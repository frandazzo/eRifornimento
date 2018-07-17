package it.noesis.erifornimento.utils;

public interface CallbackContext<T> {
    void onDialogDismiss(T returnData, String dialogTag);
}
