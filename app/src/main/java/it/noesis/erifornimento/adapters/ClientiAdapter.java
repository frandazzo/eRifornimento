package it.noesis.erifornimento.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import it.noesis.erifornimento.R;
import it.noesis.erifornimento.model.Cliente;

public class ClientiAdapter extends RecyclerView.Adapter<ClientiAdapter.ViewHolder> {

    private List<Cliente> clienti;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextViewAzi;
        public TextView mTextViewPiva;
        public TextView mTextViewTarga;
        public View mLayout;
        public Cliente cliente;
        public Context context;
        public ViewHolder(View v,Context context) {
            super(v);
            this.context = context;
            mLayout = v;
            mTextViewAzi = v.findViewById(R.id.azi);
            mTextViewPiva = v.findViewById(R.id.piva);
            mTextViewTarga =  v.findViewById(R.id.targa);
        }
    }

    public ClientiAdapter(List<Cliente> clienti){
        this.clienti = clienti;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ClientiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_clienti, viewGroup, false);

        ViewHolder vh = new ViewHolder(v, viewGroup.getContext());
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final ClientiAdapter.ViewHolder viewHolder, int i) {
        viewHolder.cliente = clienti.get(i);
        viewHolder.mTextViewAzi.setText(clienti.get(i).getAnag().getDenom());
        viewHolder.mTextViewPiva.setText(clienti.get(i).getAnag().getPiva());
        viewHolder.mTextViewTarga.setText(clienti.get(i).getAnag().getCf());
        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(viewHolder.context, "Click on :" + viewHolder.mTextViewAzi.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return  clienti.size();
    }
}
