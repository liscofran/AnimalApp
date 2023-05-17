package it.uniba.dib.sms22239;

import android.annotation.SuppressLint;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.uniba.dib.sms22239.Models.Oggetto_Spesa;

public class FirebaseRecyclerAdapterSpese extends FirebaseRecyclerAdapter<Oggetto_Spesa, FirebaseRecyclerAdapterSpese.My_View_Holder_Spesa> {


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    private OnItemClickListener listener;


    public FirebaseRecyclerAdapterSpese(@NonNull FirebaseRecyclerOptions<Oggetto_Spesa> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull My_View_Holder_Spesa holder, @SuppressLint("RecyclerView") int position, @NonNull Oggetto_Spesa model) {

        holder.nome.setText(model.nome);
        double prezzoDouble = Double.parseDouble(String.valueOf(model.prezzo));
        holder.prezzo.setText(String.valueOf(prezzoDouble));
        int quantitaInt = Integer.parseInt(String.valueOf(model.quantita));
        holder.quantita.setText(String.valueOf(quantitaInt));
        holder.dataAcquisto.setText(model.dataAcquisto);


        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position);

                    // Salva i dati del profilo e torna all'activity precedente
                    String oggettoId = model.id;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mDatabase = database.getInstance().getReference().child("Oggetti").child(oggettoId);

                    //prende i dati inseriti in input e gli assegna alle variabili temporanee
                    if (model.quantita != 0) {
                        int quant = --model.quantita;

                        //modifica e salva i dati sul database
                        mDatabase.child("quantita").setValue(quant);
                    }
                    //Chiedere a fabio per messaggio di errore
                }
            }
        });

        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position);

                    // Salva i dati del profilo e torna all'activity precedente
                    String oggettoId = model.id;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mDatabase = database.getInstance().getReference().child("Oggetti").child(oggettoId);

                    //prende i dati inseriti in input e gli assegna alle variabili temporanee
                    if (model.quantita != 99) {
                        int quant = ++model.quantita;

                        //modifica e salva i dati sul database
                        mDatabase.child("quantita").setValue(quant);
                    }


                }
            }
        });

        holder.btn_elimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ottiene l'oggetto da eliminare
                Oggetto_Spesa oggetto = getItem(position);
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
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());
                                Toast.makeText(view.getContext(), "Oggetto eliminato con successo!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Annulla", null)
                        .show();
            }
        });
    }




    @NonNull
    @Override
    public My_View_Holder_Spesa onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spesa_item, parent, false);
        return new My_View_Holder_Spesa(view);
    }

    class My_View_Holder_Spesa extends RecyclerView.ViewHolder {

        TextView nome, prezzo, quantita, dataAcquisto;
        Button btn_minus, btn_plus, btn_elimina;
        MaterialCardView material;


        public My_View_Holder_Spesa(@NonNull View itemView) {
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
    }
}







