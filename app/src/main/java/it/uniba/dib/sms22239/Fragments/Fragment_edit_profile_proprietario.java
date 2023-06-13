package it.uniba.dib.sms22239.Fragments;

import static android.app.Activity.RESULT_OK;

import static com.google.common.io.Files.getFileExtension;

import android.content.ContentResolver;
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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Activities.Activity_Profile_Proprietario_Ente;
import it.uniba.dib.sms22239.Activities.Activity_Registrazione_Segnalazione;
import it.uniba.dib.sms22239.Models.Proprietario;
import it.uniba.dib.sms22239.Models.Segnalazione;
import it.uniba.dib.sms22239.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_edit_profile_proprietario extends Fragment {

    private FirebaseAuth mAuth;
    private EditText editName;
    private EditText editCognome;
    private TextView editcodfiscale;

    private CircleImageView profilo;
    private CircleImageView editImage;
    Uri mImageUri;
    StorageTask mUploadTask;
    StorageReference mStorageRef;


    private static final int PICK_IMAGE_REQUEST = 1;

    public Fragment_edit_profile_proprietario() {
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
        return inflater.inflate(R.layout.fragment_edit_profile_proprietario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;
        mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

        // Collega i componenti dell'interfaccia con le variabili
        editName = getView().findViewById(R.id.user_nome);
        editCognome = getView().findViewById(R.id.user_cognome);
        editcodfiscale = getView().findViewById(R.id.user_codicefiscale);
        editImage = getView().findViewById(R.id.upload);
        profilo = getView().findViewById(R.id.profile_image);


        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Proprietari/" + user.getUid() + ".jpg");
        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                // Usa Picasso per caricare l'immagine nell'ImageView
                Picasso.get().load(imageUrl).into(profilo);
            }
        });


        ImageButton saveProfileButton = getView().findViewById(R.id.save_profile_button);
        Button backBtn = getView().findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Profile_Proprietario_Ente.class);
                startActivity(intent);
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
                String cognome = dataSnapshot.child("cognome").getValue(String.class);
                String codfiscale = dataSnapshot.child("codice_fiscale").getValue(String.class);

                //set delle variabili recuperate al layout
                editName.setText(name);
                editCognome.setText(cognome);
                editcodfiscale.setText(codfiscale);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
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
                DatabaseReference mDatabase = database.getInstance().getReference().child("User").child(user.getUid());
                Proprietario prp = new Proprietario();

                //prende i dati inseriti in input e gli assegna alle variabili temporanee
                String newName = editName.getText().toString();
                String newCognome = editCognome.getText().toString();
                String newCodfiscale = editcodfiscale.getText().toString();

                //modifica e salva i dati anche sul database
                mDatabase.child("cognome").setValue(newCognome);
                mDatabase.child("nome").setValue(newName);
                mDatabase.child("codice_fiscale").setValue(newCodfiscale);



                updateFile(user);

                Intent intent = new Intent(getActivity(), Activity_Home.class);
                String c5= getString(R.string.c2);

                Toast.makeText(getActivity(), c5, Toast.LENGTH_LONG).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(profilo);
        }
    }

    private void updateFile(FirebaseUser user) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Proprietari/" + user.getUid() + ".jpg");

        // Se l'utente ha selezionato un'immagine, caricala nello storage
        if (mImageUri != null) {
            // Crea un nome di file univoco per l'immagine
            String fileName = user.getUid() + ".jpg";

            // Carica l'immagine nell'URI specificato
            imagesRef = storageRef.child("Proprietari/" + fileName);
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
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
                        mDatabase.child("immagine").setValue(downloadUri.toString());
                    }
                }
            });
        }
    }

}
