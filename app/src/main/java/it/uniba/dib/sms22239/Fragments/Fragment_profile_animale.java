package it.uniba.dib.sms22239.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import it.uniba.dib.sms22239.Activities.Activity_Bluetooth;
import it.uniba.dib.sms22239.Activities.Activity_Multimedia;
import it.uniba.dib.sms22239.Activities.Activity_Spese;
import it.uniba.dib.sms22239.R;

public class Fragment_profile_animale extends Fragment {
    private TextView mNomeTextView;
    private TextView mrazzaTextView;
    private TextView msessoTextView;
    private String idUtente;
    private TextView midproprietarioTextView;
    private TextView nomecognomeprop;
    private TextView statusTextView;
    private TextView casaluogoTextView;
    private ImageView profilo;



    public Fragment_profile_animale() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profile_animal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        String idAnimal = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;
        DatabaseReference mDatabase1;

        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("Animale").child(idAnimal);
        mDatabase1 = database.getInstance().getReference().child("User");

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Animali/" + idAnimal + ".jpg");

       // midproprietarioTextView = getView().findViewById(R.id.cognome_veterinario);
        mNomeTextView = getView().findViewById(R.id.animal_nome);
        mrazzaTextView =  getView().findViewById(R.id.razza);
        msessoTextView =  getView().findViewById(R.id.sesso);
        ImageButton backBtn = getView().findViewById(R.id.back);
        nomecognomeprop = getView().findViewById(R.id.nom_cogn_prop);
        statusTextView = getView().findViewById(R.id.status);
        casaluogoTextView  = getView().findViewById(R.id.luogo);
        profilo = getView().findViewById(R.id.profile_image);

        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                // Usa Picasso per caricare l'immagine nell'ImageView
                Picasso.get().load(imageUrl).into(profilo);
                profilo = getView().findViewById(R.id.profile_image);
            }
        });
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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("nome").getValue(String.class);
                String razza = dataSnapshot.child("razza").getValue(String.class);
                String sesso = dataSnapshot.child("sesso").getValue(String.class);
                idUtente = dataSnapshot.child("Id_utente").getValue(String.class);
                String status = dataSnapshot.child("prop").getValue(String.class);
                String luogo = dataSnapshot.child("luogo").getValue(String.class);

                //set delle variabili recuperate al layout

                mNomeTextView.setText("Nome: " + name);
                mrazzaTextView.setText("Razza: " + razza);
                msessoTextView.setText("Sesso: " + sesso);
                statusTextView.setText("Status: " + status);
                casaluogoTextView.setText("Luogo: " + luogo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Recupera i dati dal database 2 e popola le viste
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


        getView().findViewById(R.id.modifica).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_edit_animal_profile());
                fragmentTransaction.commit();
            }
        });

        getView().findViewById(R.id.bluetooth_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Bluetooth.class));
            }
        });

        getView().findViewById(R.id.salute_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_salute_animale());
                fragmentTransaction.commit();
            }
        });

        ImageButton shareButton = view.findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recupera i dati dall'interfaccia utente
                String nome = mNomeTextView.getText().toString();
                String razza = mrazzaTextView.getText().toString();
                String sesso = msessoTextView.getText().toString();
                String proprietario = midproprietarioTextView.getText().toString();

                // Costruisci la stringa di testo per la condivisione
                String shareText = "Nome: " + nome + "\n" +
                        "Razza: " + razza + "\n" +
                        "Sesso: " + sesso + "\n" +
                        "Proprietario: " + proprietario;

                // Crea l'intent per la condivisione
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        getView().findViewById(R.id.spese_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Spese.class);
                intent.putExtra("ANIMAL_CODE",idAnimal);
                startActivity(intent);            }
        });

        getView().findViewById(R.id.multimedia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Multimedia.class);
                startActivity(intent);            }
        });
    }
}