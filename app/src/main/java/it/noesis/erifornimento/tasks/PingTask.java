package it.noesis.erifornimento.tasks;

import android.os.AsyncTask;

public class PingTask extends AsyncTask<String, Void, String> {


    private AsyncTaskCallbackContext<String> context;

    public PingTask(AsyncTaskCallbackContext<String> context){

        this.context = context;
    }


    @Override
    protected String doInBackground(String... strings) {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (strings[0].length() > 3)
            return strings[0];

        return "";
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
