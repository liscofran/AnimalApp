package it.uniba.dib.sms22239.Fragments.Enti;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import it.uniba.dib.sms22239.Models.Ente;
import it.uniba.dib.sms22239.R;


public class Fragment_Edit_Profilo_Ente extends Fragment {

    private FirebaseAuth mAuth;
    private TextView mragionesociale;
    //private TextView mtipoTextView;

    private TextView mpivaTextView;
    private TextView msedelegaleTextView;

    private Spinner spinner;

    private ImageView profilo;
    private CircleImageView editImage;
    Uri mImageUri;
    StorageTask mUploadTask;
    StorageReference mStorageRef;

    private static final int PICK_IMAGE_REQUEST = 1;

    String selectedItem;

    public Fragment_Edit_Profilo_Ente() {
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
        return inflater.inflate(R.layout.fragment_edit_profile_ente, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        spinner = getView().findViewById(R.id.spinner);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;
        mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

        // Collega i componenti dell'interfaccia con le variabili
        EditText editragsociale = getView().findViewById(R.id.ente_ragione_sociale);
        EditText editsedelegale = getView().findViewById(R.id.ente_sede_legale);
        EditText editpiva = getView().findViewById(R.id.ente_piva);


        spinner.setPrompt("");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ente_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        ImageView saveProfileButton = getView().findViewById(R.id.save_profile_button);
        mragionesociale = getView().findViewById(R.id.ente_ragione_sociale);
//        mtipoTextView = getView().findViewById(R.id.ente_tipo);
        msedelegaleTextView = getView().findViewById(R.id.ente_sede_legale);
        mpivaTextView = getView().findViewById(R.id.ente_piva);
        profilo = getView().findViewById(R.id.profile_image);
        editImage = getView().findViewById(R.id.upload);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Enti/" + user.getUid() + ".jpg");
        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                // Usa Picasso per caricare l'immagine nell'ImageView
                Picasso.get().load(imageUrl).into(profilo);
            }
        });
        // Recupera i dati dal database e popola i campi

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    Ente ent = task.getResult().getValue(Ente.class);

                    String ragsociale = ent.ragione_sociale;
                    String sedelegale = ent.sede_legale;
                    String piva = ent.p_iva;

                    //set delle variabili recuperate al layout
                    mragionesociale.setText(ragsociale);
                    msedelegaleTextView.setText(sedelegale);
                    mpivaTextView.setText(piva);
                }
            }
        });

        //modificare l'immagine
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

                //prende i dati inseriti in input e gli assegna alle variabili temporanee
                String newRagsociale = editragsociale.getText().toString();
                String newtipo = selectedItem;
                String newsedelegale = editsedelegale.getText().toString();
                String newpiva = editpiva.getText().toString();

                //modifica e salva i dati anche sul database
                mDatabase.child("ragione_sociale").setValue(newRagsociale);
                mDatabase.child("tipo").setValue(newtipo);
                mDatabase.child("sede_legale").setValue(newsedelegale);
                mDatabase.child("p_iva").setValue(newpiva);

                updateFile(user);

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

            Picasso.get().load(mImageUri).into(profilo);
        }
    }

    private void updateFile(FirebaseUser user) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Enti/" + user.getUid() + ".jpg");

        // Se l'utente ha selezionato un'immagine, caricala nello storage
        if (mImageUri != null) {
            // Crea un nome di file univoco per l'immagine
            String fileName = user.getUid() + ".jpg";

            // Carica l'immagine nell'URI specificato
            imagesRef = storageRef.child("Enti/" + fileName);
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