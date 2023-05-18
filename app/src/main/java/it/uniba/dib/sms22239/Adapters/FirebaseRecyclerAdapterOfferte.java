package it.uniba.dib.sms22239.Adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import it.uniba.dib.sms22239.Models.Offerta;
import it.uniba.dib.sms22239.R;

public class FirebaseRecyclerAdapterOfferte extends FirebaseRecyclerAdapter<Offerta, FirebaseRecyclerAdapterOfferte.myViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public FirebaseRecyclerAdapterOfferte(@NonNull FirebaseRecyclerOptions<Offerta> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Offerta model) {

        holder.title.setText(model.oggetto);
        holder.oggetto.setText(model.descrizione);

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = mStorageRef.child("Offerte/" + model.idOfferta + ".jpg");

        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String url = uri.toString();
            Picasso.get().load(url)
                    .into(holder.imageView, new Callback() {
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

        holder.material.setOnClickListener(new View.OnClickListener() {
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
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offerte_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView title, oggetto;
        MaterialCardView material;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.logoIv);
            title = itemView.findViewById(R.id.titleTv);
            oggetto = itemView.findViewById(R.id.oggetto);
            material = itemView.findViewById(R.id.card3);

        }
    }

}
