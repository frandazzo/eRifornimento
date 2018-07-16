package it.noesis.erifornimento.tasks;

public interface AsyncTaskCallbackContext<T> {

        void onPreExecute();
        void onPostExecute(T t);

}
