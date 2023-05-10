package it.uniba.dib.sms22239.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import it.uniba.dib.sms22239.R;

public class Fragment_profilo_animale_senza_modifica extends Fragment
{
    private TextView mNomeTextView,mrazzaTextView,msessoTextView,nomecognomeprop,mluogoTextView,mstatusTextView;
    private ImageView profilo;

    private String idUtente,idAnimale;

    public Fragment_profilo_animale_senza_modifica()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profilo_animale_senza_modifica, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        idAnimale = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");

        // Recupera il riferimento al database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimale);
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("User");

        //Recupera il riferimento allo Storage
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference("Animali").child(idAnimale).child("ImmagineProfilo.jpg");

        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                // Usa Picasso per caricare l'immagine nell'ImageView
                Picasso.get().load(imageUrl).into(profilo);
                profilo = getView().findViewById(R.id.profile_image);
            }
        });

        profilo = getView().findViewById(R.id.profile_image);
        nomecognomeprop = getView().findViewById(R.id.nom_cogn_prop);
        mNomeTextView = getView().findViewById(R.id.animal_nome);
        mrazzaTextView =  getView().findViewById(R.id.razza);
        msessoTextView =  getView().findViewById(R.id.sesso);
        mluogoTextView =  getView().findViewById(R.id.luogo);
        mstatusTextView =  getView().findViewById(R.id.status);

        ImageButton backBtn = getView().findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        // Recupera i dati dal database e popola le viste
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String nome = dataSnapshot.child("nome").getValue(String.class);
                String razza = dataSnapshot.child("razza").getValue(String.class);
                String sesso = dataSnapshot.child("sesso").getValue(String.class);
                String luogo = dataSnapshot.child("luogo").getValue(String.class);
                String status = dataSnapshot.child("prop").getValue(String.class);
                idUtente = dataSnapshot.child("Id_utente").getValue(String.class);

                //set delle variabili recuperate al layout
                mNomeTextView.setText("Nome:" + nome);
                mrazzaTextView.setText("Razza: " + razza);
                msessoTextView.setText("Sesso: " +sesso);
                mluogoTextView.setText("Luogo: " +luogo);
                mstatusTextView.setText("Status: " +status);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        // Recupera i dati dal secondo riferimento al database e popola le viste
        mDatabase1.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nome = dataSnapshot.child(idUtente).child("nome").getValue(String.class);
                String cognome = dataSnapshot.child(idUtente).child("cognome").getValue(String.class);

                //set delle variabili recuperate al layout

                nomecognomeprop.setText("Proprietario: " + nome + " " + cognome);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}