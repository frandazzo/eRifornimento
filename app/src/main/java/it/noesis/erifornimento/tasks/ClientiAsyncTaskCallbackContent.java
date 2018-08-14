package it.noesis.erifornimento.tasks;

public interface ClientiAsyncTaskCallbackContent<T> {


    void onPreExecute();
    void onPostExecuteClientiTask(T t);


}