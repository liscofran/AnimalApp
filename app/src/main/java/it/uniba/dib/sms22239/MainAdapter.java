package it.uniba.dib.sms22239;

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

public class MainAdapter extends FirebaseRecyclerAdapter<Animale,MainAdapter.myViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public MainAdapter(@NonNull FirebaseRecyclerOptions<Animale> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Animale model) {

        holder.name.setText(model.nome);
        holder.razza.setText(model.razza);

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = mStorageRef.child("Animali/" + model.immagine);
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView name, razza;
        MaterialCardView material;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgId);
            name = itemView.findViewById(R.id.nameId);
            razza = itemView.findViewById(R.id.razza);
            material = itemView.findViewById(R.id.card);

        }
    }
}
