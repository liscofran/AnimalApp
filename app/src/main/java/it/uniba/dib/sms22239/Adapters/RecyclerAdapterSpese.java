package it.uniba.dib.sms22239.Adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import it.uniba.dib.sms22239.Models.Oggetto_Spesa;
import it.uniba.dib.sms22239.R;

public class RecyclerAdapterSpese extends RecyclerView.Adapter<RecyclerAdapterSpese.ViewHolder> {

    private static List<Oggetto_Spesa> filteredList;

    public RecyclerAdapterSpese(List<Oggetto_Spesa> filteredList, OnItemClickListener listener) {
        this.filteredList = filteredList;
        this.listener = listener;
    }

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    private static OnItemClickListener listener = null;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creazione della vista per ogni elemento della lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spesa_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Popolamento della vista con i dati dell'elemento corrispondente
        Oggetto_Spesa oggetto = filteredList.get(position);
        holder.bind(oggetto, position);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nome, prezzo, quantita, dataAcquisto;
        Button btn_minus, btn_plus, btn_elimina;
        MaterialCardView material;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.item_name_textview);
            prezzo = itemView.findViewById(R.id.item_price_textview);
            quantita = itemView.findViewById(R.id.item_quantity_textview);
            dataAcquisto = itemView.findViewById(R.id.item_date_textview);

            btn_minus = itemView.findViewById(R.id.minus_button);
            btn_plus = itemView.findViewById(R.id.plus_button);
            btn_elimina = itemView.findViewById(R.id.elimina_button);
            material = itemView.findViewById(R.id.card4);
        }

        public void bind(Oggetto_Spesa oggetto, int position) {
            nome.setText(oggetto.nome);
            double prezzoDouble = Double.parseDouble(String.valueOf(oggetto.prezzo));
            prezzo.setText(String.valueOf(prezzoDouble));
            int quantitaInt = Integer.parseInt(String.valueOf(oggetto.quantita));
            quantita.setText(String.valueOf(quantitaInt));
            dataAcquisto.setText(oggetto.dataAcquisto);


            btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(position);

                        // Salva i dati del profilo e torna all'activity precedente
                        String oggettoId = oggetto.id;
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mDatabase = database.getInstance().getReference().child("Oggetti").child(oggettoId);

                        //prende i dati inseriti in input e gli assegna alle variabili temporanee
                        if (oggetto.quantita != 0) {
                            int quant = --oggetto.quantita;

                            //modifica e salva i dati sul database
                            mDatabase.child("quantita").setValue(quant);
                            filteredList.get(position).quantita = quant;
                            quantita.setText(String.valueOf(quant));
                        }
                        //Chiedere a fabio per messaggio di errore
                    }
                }
            });

            btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(position);

                        // Salva i dati del profilo e torna all'activity precedente
                        String oggettoId = oggetto.id;
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mDatabase = database.getInstance().getReference().child("Oggetti").child(oggettoId);

                        //prende i dati inseriti in input e gli assegna alle variabili temporanee
                        if (oggetto.quantita != 99) {
                            int quant = ++oggetto.quantita;

                            //modifica e salva i dati sul database
                            mDatabase.child("quantita").setValue(quant);
                            filteredList.get(position).quantita = quant;
                            quantita.setText(String.valueOf(quant));
                        }


                    }
                }
            });

            btn_elimina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Ottiene l'oggetto da eliminare
                    Oggetto_Spesa oggetto = filteredList.get(position);
                    // Ottiene l'id dell'oggetto
                    String oggettoId = oggetto.id;
                    //Alert Dialog elimina
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Elimina oggetto")
                            .setMessage("Sei sicuro di voler eliminare l'oggetto?")
                            .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference mDatabase = database.getInstance().getReference().child("Oggetti").child(oggettoId);
                                    // Elimina l'oggetto dal database
                                    mDatabase.removeValue();
                                    // Aggiorna l'adapter
                                    filteredList.remove(position);
                                    Toast.makeText(view.getContext(), "Oggetto eliminato con successo!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Annulla", null)
                            .show();
                }
            });
        }
    }
}
