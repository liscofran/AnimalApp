package it.uniba.dib.sms22239.Fragments.Annunci;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Models.Ente;
import it.uniba.dib.sms22239.Models.Proprietario;
import it.uniba.dib.sms22239.Models.Segnalazione;
import it.uniba.dib.sms22239.Models.Veterinario;
import it.uniba.dib.sms22239.R;


public class Fragment_Profilo_Segnalazione extends Fragment
{
    private FirebaseAuth mAuth;
    private TextView mDescrizioneTextView;
    private TextView mProvinciaTextView;
    private TextView mOggettoTextView;

    private static final int REQUEST_ENABLE_BT = 1;
    private TextView utente;
    private CircleImageView Immaginesegnalazione;
    String id_utente;
    String nomeEcognome;
    public double latitude,longitude;
        String id_utente_segnalazione;
        String tmp1;

        String tmp2;
    DatabaseReference mDatabase;
    DatabaseReference mDatabase1;

    public Fragment_Profilo_Segnalazione() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
            return inflater.inflate(R.layout.fragment_profilo_segnalazione, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String idSegnalazione = requireActivity().getIntent().getStringExtra("SEGNALAZIONE_CODE");

        FirebaseUser user = mAuth.getCurrentUser();

        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("Segnalazioni").child(idSegnalazione);
        mDatabase1 = database.getInstance().getReference().child("User");

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Segnalazioni/" + idSegnalazione + ".jpg");

        mDescrizioneTextView = getView().findViewById(R.id.segnalazione_descrizione);
        mProvinciaTextView =  getView().findViewById(R.id.segnalazione_provincia);
        mOggettoTextView =  getView().findViewById(R.id.oggetto);
        Immaginesegnalazione = getView().findViewById(R.id.imageView2);
        utente = getView().findViewById(R.id.segnalazione_utente);

        ImageView mapBtn=getView().findViewById(R.id.mapBtn);
        Button backBtn = getView().findViewById(R.id.back);

        getView().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.removeValue(); // rimuovi la tupla dal database Firebase
                String c10= getString(R.string.se23);
                Toast.makeText(getActivity(), c10, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Activity_Home.class);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                // Usa Picasso per caricare l'immagine nell'ImageView
                Picasso.get().load(imageUrl).into(Immaginesegnalazione);
                Immaginesegnalazione = getView().findViewById(R.id.imageView2);
            }
        });

        // Recupera i dati dal database e popola le viste
//        mDatabase.addValueEventListener(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                String descrizione = dataSnapshot.child("descrizione").getValue(String.class);
//                String provincia = dataSnapshot.child("provincia").getValue(String.class);
//                String oggetto = dataSnapshot.child("oggetto").getValue(String.class);
//
//                 latitude=dataSnapshot.child("latitude").getValue(double.class);
//                 longitude=dataSnapshot.child("longitude").getValue(double.class);
//
//                id_utente = dataSnapshot.child("uid").getValue(String.class);
//                //set delle variabili recuperate al layout
//                mDescrizioneTextView.setText(descrizione);
//                mProvinciaTextView.setText(provincia);
//                mOggettoTextView.setText(oggetto);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError)
//            {
//
//            }
//        });
//
//        mDatabase1.addValueEventListener(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                String nome_utente = dataSnapshot.child(id_utente).child("nome").getValue(String.class);
//                String cognome_utente = dataSnapshot.child(id_utente).child("cognome").getValue(String.class);
//                nomeEcognome = nome_utente + " " + cognome_utente;
//                utente.setText(nomeEcognome);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError)
//            {
//
//            }
//        });
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    Segnalazione sen = task.getResult().getValue(Segnalazione.class);

                    String descrizione = sen.descrizione;
                    String provincia = sen.provincia;
                    String oggetto = sen.oggetto;
                    id_utente = sen.uid;
                    mDatabase1 = FirebaseDatabase.getInstance().getReference().child("User").child(id_utente);
                    mDatabase1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()) {
                                Proprietario user = task.getResult().getValue(Proprietario.class);

                                if(user.classe.equals("Proprietario"))
                                {
                                    Proprietario prop = task.getResult().getValue(Proprietario.class);
                                    String nome_utente = prop.nome;
                                    String cognome_utente = prop.cognome;
                                    nomeEcognome = nome_utente + " " + cognome_utente;
                                    utente.setText(nomeEcognome);
                                }
                                else if(user.classe.equals("Ente"))
                                {
                                    Ente ente = task.getResult().getValue(Ente.class);
                                    String rag = ente.ragione_sociale;
                                    utente.setText(rag);
                                }
                                else
                                {
                                    Veterinario vet = task.getResult().getValue(Veterinario.class);
                                    String nome_utente = vet.nome;
                                    String cognome_utente = vet.cognome;
                                    nomeEcognome = nome_utente + " " + cognome_utente;
                                    utente.setText(nomeEcognome);
                                }

                            }
                        }
                    });

                    //set delle variabili recuperate al layout

                    mDescrizioneTextView.setText(descrizione);
                    mProvinciaTextView.setText(provincia);
                    mOggettoTextView.setText(oggetto);
                }
            }
        });

        getView().findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_Edit_Profilo_Segnalazione());
                fragmentTransaction.commit();
            }
        });

        mapBtn.setOnClickListener((new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String c1= getString(R.string.se22);
                String label = c1; // Nome del pin sulla mappa
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        }
        ));

    }
}