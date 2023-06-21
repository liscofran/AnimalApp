package it.uniba.dib.sms22239.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import it.uniba.dib.sms22239.Activities.Activity_Segnalazione;
import it.uniba.dib.sms22239.Activities.Activity_Segnalazioni_Offerte;
import it.uniba.dib.sms22239.R;


public class Fragment_edit_segnalazione extends Fragment {

    private FirebaseAuth mAuth;
    private EditText mDescrizioneTextView;
    private EditText mProvinciaTextView;
    private EditText mOggettoTextView;
    private TextView utente;
    private CircleImageView Immaginesegnalazione;
    String id_utente;
    String nomeEcognome;

    private TextView edit;
    private String idSegnalazione;

    Uri mImageUri;
    StorageTask mUploadTask;

    private static final int PICK_IMAGE_REQUEST = 1;

    public Fragment_edit_segnalazione() {
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
        return inflater.inflate(R.layout.fragment_edit_segnalazione, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        idSegnalazione = requireActivity().getIntent().getStringExtra("SEGNALAZIONE_CODE");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference mDatabase;
        DatabaseReference mDatabase1;

        mDatabase = database.getInstance().getReference().child("Segnalazioni").child(idSegnalazione);
        mDatabase1 = database.getInstance().getReference().child("User");

        // Collega i componenti dell'interfaccia con le variabili
        EditText editDescrizione = getView().findViewById(R.id.segnalazione_descrizione);
        EditText editProvincia = getView().findViewById(R.id.segnalazione_provincia);
        EditText editOggetto = getView().findViewById(R.id.oggetto);
        TextView edit = getView().findViewById(R.id.edit);

        Button saveProfileButton = getView().findViewById(R.id.save_button);
        mDescrizioneTextView = getView().findViewById(R.id.segnalazione_descrizione);
        mProvinciaTextView = getView().findViewById(R.id.segnalazione_provincia);
        mOggettoTextView =  getView().findViewById(R.id.oggetto);
        Immaginesegnalazione = getView().findViewById(R.id.imageView2);
        utente = getView().findViewById(R.id.segnalazione_utente);
        Button backBtn = getView().findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Segnalazione.class);
                startActivity(intent);
            }
        });

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Segnalazioni/" + idSegnalazione + ".jpg");
        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                // Usa Picasso per caricare l'immagine nell'ImageView
                Picasso.get().load(imageUrl).into(Immaginesegnalazione);
            }
        });


        // Recupera i dati dal database e popola i campi
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //recupero dati e assegnazione alle variabili
                String descrizione = dataSnapshot.child("descrizione").getValue(String.class);
                String provincia = dataSnapshot.child("provincia").getValue(String.class);
                String oggetto = dataSnapshot.child("oggetto").getValue(String.class);
                id_utente = dataSnapshot.child("uid").getValue(String.class);
                //set delle variabili recuperate al layout
                mDescrizioneTextView.setText(descrizione);
                mProvinciaTextView.setText(provincia);
                mOggettoTextView.setText(oggetto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        mDatabase1.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String nome_utente = dataSnapshot.child(id_utente).child("nome").getValue(String.class);
                String cognome_utente = dataSnapshot.child(id_utente).child("cognome").getValue(String.class);
                nomeEcognome = nome_utente + " " + cognome_utente;
                utente.setText(nomeEcognome);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Imposta un listener di clic sul pulsante di salvataggio del profilo
        saveProfileButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Salva i dati del profilo e torna all'activity precedente
                DatabaseReference mDatabase = database.getInstance().getReference().child("Segnalazioni").child(idSegnalazione);

                //prende i dati inseriti in input e gli assegna alle variabili temporanee
                String newDescrizione = editDescrizione.getText().toString();
                String newProvincia = editProvincia.getText().toString();
                String newOggetto = editOggetto.getText().toString();

                //modifica e salva i dati anche sul database
                mDatabase.child("provincia").setValue(newProvincia);
                mDatabase.child("descrizione").setValue(newDescrizione);
                mDatabase.child("oggetto").setValue(newOggetto);
                updateFile(mDatabase);

                String c5= getString(R.string.c2);
                Toast.makeText(getActivity(), c5, Toast.LENGTH_LONG).show();

            }
        });
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(Immaginesegnalazione);
        }
    }
    private void updateFile(DatabaseReference mDatabase) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Segnalazioni/" + idSegnalazione + ".jpg");

        // Se l'utente ha selezionato un'immagine, caricala nello storage
        if (mImageUri != null)
        {
            // Crea un nome di file univoco per l'immagine
            String fileName = idSegnalazione + ".jpg";

            // Carica l'immagine nell'URI specificato
            imagesRef = storageRef.child("Segnalazioni/" + fileName);
            mUploadTask = imagesRef.putFile(mImageUri);

            // Aggiorna l'URL dell'immagine nel database
            StorageReference finalImagesRef = imagesRef;
            mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continua la catena delle promesse per ottenere l'URL dell'immagine appena caricata
                    return finalImagesRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        // Aggiorna l'URL dell'immagine nel database
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Segnalazioni").child(idSegnalazione);
                        mDatabase.child("immagine").setValue(downloadUri.toString());
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                        startActivity(getActivity().getIntent());
                        getActivity().overridePendingTransition(0, 0);
                    }
                }
            });
        }
        else
        {
            getActivity().finish();
            getActivity().overridePendingTransition(0, 0);
            startActivity(getActivity().getIntent());
            getActivity().overridePendingTransition(0, 0);
        }
    }
}