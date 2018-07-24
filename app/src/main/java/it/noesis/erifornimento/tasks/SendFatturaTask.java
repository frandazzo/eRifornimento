package it.noesis.erifornimento.tasks;

import android.os.AsyncTask;

import it.noesis.erifornimento.model.Fattura;

public class SendFatturaTask extends AsyncTask<Fattura, Void, String> {

    private AsyncTaskCallbackContext<String> context;

    public SendFatturaTask(AsyncTaskCallbackContext<String> context ){
        this.context = context;
    }


    @Override
    protected String doInBackground(Fattura... fatturas) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return "ok";
    }

    @Override
    protected void onPostExecute(String s) {
        context.onPostExecute(s);
    }

    @Override
    protected void onPreExecute() {
        context.onPreExecute();
    }
}
