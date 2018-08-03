package it.noesis.erifornimento.tasks;

import android.os.AsyncTask;

import it.noesis.erifornimento.model.LoginTaskResult;
import it.noesis.erifornimento.model.User;

public class LoginTask extends AsyncTask<Void, Void, LoginTaskResult> {

    private AsyncTaskCallbackContext<LoginTaskResult> context;
    private final String username;
    private final String password;

    public LoginTask(AsyncTaskCallbackContext<LoginTaskResult> context, String username, String password){

        this.context = context;
        this.username = username;
        this.password = password;
    }

    @Override
    protected void onPostExecute(LoginTaskResult loginTaskResult) {


        context.onPostExecute(loginTaskResult);

    }

    @Override
    protected void onCancelled() {
        LoginTaskResult loginTaskResult = new LoginTaskResult();
        loginTaskResult.setError("Cancellato");
        context.onPostExecute(loginTaskResult);
    }

    @Override
    protected LoginTaskResult doInBackground(Void... voids) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LoginTaskResult loginTaskResult = new LoginTaskResult();
        if ("ciccillo".equals(username) && "password".equals(password)){

            loginTaskResult.setUser(new User());
            loginTaskResult.getUser().setToken("xxxxxxxxxxx");
            loginTaskResult.getUser().setName("Ciccio");
            loginTaskResult.getUser().setUsername("ciccillo");

        }else{
            loginTaskResult.setError("Username o password errati");
        }


        return loginTaskResult;
    }
}
