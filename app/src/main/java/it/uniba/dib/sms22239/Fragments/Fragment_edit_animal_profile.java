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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import it.uniba.dib.sms22239.Activities.Activity_Animali;
import it.uniba.dib.sms22239.Activities.Activity_Registrazione_Animale;
import it.uniba.dib.sms22239.R;

public class Fragment_edit_animal_profile extends Fragment {

    private EditText mNomeTextView, mrazzaTextView, msessoTextView, mluogoTextView;
    private String selectedItem, idAnimal;
    private ImageView profilo;
    protected Uri mImageUri;
    protected ImageButton saveProfileButton, backBtn;
    protected CircleImageView editImage;
    protected StorageTask mUploadTask;
    private static final int PICK_IMAGE_REQUEST = 1;
    protected Spinner spinner;
    protected StorageReference imagesRef;

    public Fragment_edit_animal_profile()
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
        return inflater.inflate(R.layout.fragment_edit_profile_animal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Collegamento al Database
        idAnimal = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimal);

        // Collega i componenti dell'interfaccia con le variabili
        mNomeTextView = getView().findViewById(R.id.animal_nome);
        mrazzaTextView = getView().findViewById(R.id.razza);
        msessoTextView = getView().findViewById(R.id.sesso);
        mluogoTextView = getView().findViewById(R.id.luogo);

        saveProfileButton = getView().findViewById(R.id.salva);
        editImage = getView().findViewById(R.id.upload);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        profilo = getView().findViewById(R.id.profile_image);
        backBtn = getView().findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        //Spinner
        spinner = getView().findViewById(R.id.spinner);
        spinner.setPrompt("");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.proprieta_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedItem = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Scelta non valida", Toast.LENGTH_SHORT).show();
            }
        });

        //Storage
        imagesRef = FirebaseStorage.getInstance().getReference("Animali").child(idAnimal).child("ImmagineProfilo.jpg");
        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                // Usa Picasso per caricare l'immagine nell'ImageView
                Picasso.get().load(imageUrl).into(profilo);
            }
        });

        // Recupera i dati dal database e popola i campi
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //recupero dati e assegnazione alle variabili
                String name = dataSnapshot.child("nome").getValue(String.class);
                String razza = dataSnapshot.child("razza").getValue(String.class);
                String sesso = dataSnapshot.child("sesso").getValue(String.class);
                //String status = dataSnapshot.child("status").getValue(String.class);
                String luogo = dataSnapshot.child("luogo").getValue(String.class);

                //set delle variabili recuperate al layout
                mNomeTextView.setText(name);
                mrazzaTextView.setText(razza);
                msessoTextView.setText(sesso);
                mluogoTextView.setText(luogo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Imposta un listener di clic sul pulsante di salvataggio del profilo
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Salva i dati del profilo e torna all'activity precedente
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimal);

                //prende i dati inseriti in input e gli assegna alle variabili temporanee
                String newName = mNomeTextView.getText().toString();
                String newRazza = mrazzaTextView.getText().toString();
                String newSesso = msessoTextView.getText().toString();
                String newLuogo = mluogoTextView.getText().toString();

                //modifica e salva i dati anche sul database
                mDatabase.child("nome").setValue(newName);
                mDatabase.child("razza").setValue(newRazza);
                mDatabase.child("sesso").setValue(newSesso);
                mDatabase.child("luogo").setValue(newLuogo);
                mDatabase.child("prop").setValue(selectedItem);

                updateFile();
                Toast.makeText(getActivity(), "Modifica avvenuta con successo", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), Activity_Animali.class);
                startActivity(intent);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(profilo);
        }
    }

    private void updateFile()
    {
        // Se l'utente ha selezionato un'immagine, caricala nello storage
        if (mImageUri != null) {

            // Carica l'immagine nell'URI specificato
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
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimal);
                        mDatabase.child("immagine").setValue(downloadUri.toString());
                    }
                }
            });
        }
    }
}