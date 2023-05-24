package it.uniba.dib.sms22239.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.uniba.dib.sms22239.Models.Offerta;
import it.uniba.dib.sms22239.R;

public class RecyclerAdapterOfferta extends RecyclerView.Adapter<RecyclerAdapterOfferta.ViewHolder> {

    private List<Offerta> filteredList;

    public RecyclerAdapterOfferta(List<Offerta> filteredList, OnItemClickListener listener) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offerte_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Popolamento della vista con i dati dell'elemento corrispondente
        Offerta offerta = filteredList.get(position);
        holder.bind(offerta, position);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView name, oggetto;
        MaterialCardView material;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.logoIv);
            name = itemView.findViewById(R.id.titleTv);
            oggetto = itemView.findViewById(R.id.oggetto);
            material = itemView.findViewById(R.id.card3);
        }

        public void bind(Offerta offerta, int position) {
            name.setText(offerta.oggetto);
            oggetto.setText(offerta.descrizione);

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = mStorageRef.child("Offerte/" + offerta.idOfferta + ".jpg");

            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String url = uri.toString();
                Picasso.get().load(url)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess()
                            {

                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("TAG", "onError:" + e.getMessage());
                            }
                        });
            });

            material.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
