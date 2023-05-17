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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import it.uniba.dib.sms22239.Models.Animale;

public class FirebaseRecyclerAdapterAnimale extends FirebaseRecyclerAdapter<Animale, FirebaseRecyclerAdapterAnimale.myViewHolder>
{
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final OnItemClickListener listener;

    public FirebaseRecyclerAdapterAnimale(@NonNull FirebaseRecyclerOptions<Animale> options, OnItemClickListener listener)
    {
        super(options);
        this.listener = listener;
    }

    @NonNull
    @Override
    public DatabaseReference getRef(int position)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        Query query = super.getRef(position)
                .orderByChild("nome")
                .equalTo(mUser.getUid());
        return query.getRef();
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Animale model)
    {
        holder.name.setText(model.nome);
        holder.razza.setText(model.razza);

        StorageReference imageRef = FirebaseStorage.getInstance().getReference("Animali").child(model.Id).child("ImmagineProfilo.jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(uri ->
        {
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.animale_item,parent,false);
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
