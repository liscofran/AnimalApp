package it.uniba.dib.sms22239;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import it.uniba.dib.sms22239.Activities.Activity_Spese;
import it.uniba.dib.sms22239.Models.Oggetto_Spesa;

public class Main_Adapter_Spese extends FirebaseRecyclerAdapter<Oggetto_Spesa, Main_Adapter_Spese.My_View_Holder_Spesa> {


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    private OnItemClickListener listener;


    public Main_Adapter_Spese(@NonNull FirebaseRecyclerOptions<Oggetto_Spesa> options, OnItemClickListener listener) {
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
                    //Chidere a fabio per messaggio di errore
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
            material = itemView.findViewById(R.id.card4);


        }
    }
}







