package it.uniba.dib.sms22239.Fragments.Annunci;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import it.uniba.dib.sms22239.Activities.Annunci.Activity_Mie_Offerte;
import it.uniba.dib.sms22239.Models.Ente;
import it.uniba.dib.sms22239.Models.Offerta;
import it.uniba.dib.sms22239.Models.Proprietario;
import it.uniba.dib.sms22239.Models.Veterinario;
import it.uniba.dib.sms22239.R;


public class Fragment_Profilo_Offerta extends Fragment
{
    private FirebaseAuth mAuth;
    private TextView mDescrizioneTextView;
    private TextView mProvinciaTextView;
    private TextView mOggettoTextView;

    private static final int REQUEST_ENABLE_BT = 1;
    private TextView utente;
    private CircleImageView Immagineofferta;
    DatabaseReference mDatabase;
    DatabaseReference mDatabase1;
    String id_utente;
    String nomeEcognome;

    public Fragment_Profilo_Offerta() {
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
        return inflater.inflate(R.layout.fragment_profilo_offerta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String idOfferta = requireActivity().getIntent().getStringExtra("OFFERTA_CODE");

        FirebaseUser user = mAuth.getCurrentUser();

        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("Offerte").child(idOfferta);
        mDatabase1 = database.getInstance().getReference().child("User");

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Offerte/" + idOfferta + ".jpg");

        mDescrizioneTextView = getView().findViewById(R.id.offerta_descrizione);
        mProvinciaTextView =  getView().findViewById(R.id.offerta_provincia);
        mOggettoTextView =  getView().findViewById(R.id.oggetto);
        Immagineofferta = getView().findViewById(R.id.imageView2);
        utente = getView().findViewById(R.id.offerta_utente);

        getView().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c1 = getString(R.string.ofe_elimina);
                String c2 = getString(R.string.ofe_sicuro_elimina);
                String c3 = getString(R.string.conferma);
                String c4 = getString(R.string.annulla);
                String c5 = getString(R.string.ofe);
                new AlertDialog.Builder(getActivity())
                        .setTitle(c1)
                        .setMessage(c2)
                        .setPositiveButton(c3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase.removeValue();
                                Toast.makeText(getActivity(), c5, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Activity_Home.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton(c4, null)
                        .show();

            }
        });

        Button backBtn = getView().findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Mie_Offerte.class);
                startActivity(intent);
            }
        });

        // Recupera i dati dal database e popola le viste

        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                // Usa Picasso per caricare l'immagine nell'ImageView
                Picasso.get().load(imageUrl).into(Immagineofferta);
            }
        });

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    Offerta off = task.getResult().getValue(Offerta.class);

                    String descrizione = off.descrizione;
                    String provincia = off.provincia;
                    String oggetto = off.oggetto;
                    id_utente = off.uid;
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
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_Edit_Profilo_Offerta());
                fragmentTransaction.commit();
            }
        });
    }
}