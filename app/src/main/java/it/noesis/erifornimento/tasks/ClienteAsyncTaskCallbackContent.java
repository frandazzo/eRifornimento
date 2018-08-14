package it.noesis.erifornimento.tasks;

public interface ClienteAsyncTaskCallbackContent<T> {


        void onPreExecute();
        void onPostExecuteClienteTask(T t);


}
