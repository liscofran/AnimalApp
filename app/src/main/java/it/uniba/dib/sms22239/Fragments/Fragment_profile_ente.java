package it.uniba.dib.sms22239.Fragments;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import it.uniba.dib.sms22239.Activities.Activity_Main;
import it.uniba.dib.sms22239.R;


public class Fragment_profile_ente extends Fragment
{
    private FirebaseAuth mAuth;
    private TextView mtipoTextView;
    private TextView msedelegaleTextView;
    private TextView mpiva;
    private TextView mragionesociale;
    private ImageView profilo;

    public Fragment_profile_ente() {
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
        return inflater.inflate(R.layout.fragment_profile_ente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Enti/" + user.getUid() + ".jpg");

        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

        mragionesociale = getView().findViewById(R.id.ente_ragione_sociale);
        mtipoTextView =  getView().findViewById(R.id.ente_tipo);
        msedelegaleTextView =  getView().findViewById(R.id.ente_sede_legale);
        mpiva = getView().findViewById(R.id.ente_piva);
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

        getView().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Elimina profilo")
                        .setMessage("Sei sicuro di voler eliminare il profilo?")
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase.removeValue();
                                user.delete();
                                Toast.makeText(getActivity(), "Ente eliminato con successo!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Activity_Main.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Annulla", null)
                        .show();
            }
        });

        Button backBtn = getView().findViewById(R.id.back);
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
                String ragsociale = dataSnapshot.child("ragione_sociale").getValue(String.class);
                String tipo = dataSnapshot.child("tipo").getValue(String.class);
                String sedelegale = dataSnapshot.child("sede_legale").getValue(String.class);
                String piva = dataSnapshot.child("p_iva").getValue(String.class);


                //set delle variabili recuperate al layout
                mragionesociale.setText("Ragione sociale: " + ragsociale);
                mtipoTextView.setText("Tipo: " + tipo);
                msedelegaleTextView.setText("Sede Legale: " + sedelegale);
                mpiva.setText("Partita IVA: " + piva);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        getView().findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_edit_ente_profile());
                fragmentTransaction.commit();
            }
        });

    }
}