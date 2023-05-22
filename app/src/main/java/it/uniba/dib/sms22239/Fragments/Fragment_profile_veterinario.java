package it.uniba.dib.sms22239.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import it.uniba.dib.sms22239.Adapters.Animal_View_Holder;
import it.uniba.dib.sms22239.Models.Proprietario;
import it.uniba.dib.sms22239.R;


public class Fragment_profile_veterinario extends Fragment {
    RelativeLayout relativeLayout;
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;

    private ImageView editProfileButton;

    private FirebaseRecyclerOptions<Proprietario> options;
    private FirebaseRecyclerAdapter<Proprietario, Animal_View_Holder> adapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase mDatabase;
    private TextView mNomeTextView;
    private TextView mCognomeTextView;
    private TextView mcodfiscaleTextView;
    private TextView memailTextView;
    private TextView mtitolostudioTextView;
    private ImageView profilo;


    public Fragment_profile_veterinario() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile_veterinario, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Veterinari/" + user.getUid() + ".jpg");

        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

        mNomeTextView = getView().findViewById(R.id.vet_nome);
        mCognomeTextView =  getView().findViewById(R.id.vet_cognome);
        mcodfiscaleTextView =  getView().findViewById(R.id.vet_codicefiscale);
        memailTextView =  getView().findViewById(R.id.vet_email);
        mtitolostudioTextView =  getView().findViewById(R.id.vet_titolostudio);
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

        // Recupera i dati dal database e popola le viste
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("nome").getValue(String.class);
                String cognome = dataSnapshot.child("cognome").getValue(String.class);
                String codfiscale = dataSnapshot.child("codice_fiscale").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String titoloStudio = dataSnapshot.child("titolo_studio").getValue(String.class);

                //set delle variabili recuperate al layout
                mNomeTextView.setText("Nome: " + name);
                mCognomeTextView.setText("Cognome: " + cognome);
                mcodfiscaleTextView.setText("Cod. Fiscale: " +codfiscale);
                memailTextView.setText("Email: " +email);
                mtitolostudioTextView.setText("Tit. studio: " +titoloStudio);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                                Toast.makeText(getActivity(), "Veterinario eliminato con successo!", Toast.LENGTH_SHORT).show();
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

        getView().findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_edit_veterinario());
                fragmentTransaction.commit();
            }
        });
    }
}