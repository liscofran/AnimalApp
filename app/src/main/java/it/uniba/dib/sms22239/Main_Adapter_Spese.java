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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
        holder.prezzo.setText((int) model.prezzo);
        holder.quantita.setText(model.quantita);

      holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });

        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position);
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

        TextView nome,prezzo,quantita, dataAcquisto;
        Button btn_minus,btn_plus;


        public My_View_Holder_Spesa(@NonNull View itemView) {
            super(itemView);
            nome=itemView.findViewById(R.id.add_item_name);
            prezzo=itemView.findViewById(R.id.add_item_price);
            quantita=itemView.findViewById(R.id.add_item_quantity);
            dataAcquisto=itemView.findViewById(R.id.add_item_date);

            btn_minus = itemView.findViewById(R.id.minus_button);
            btn_plus = itemView.findViewById(R.id.plus_button);



        }
    }



}



