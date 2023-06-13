package it.uniba.dib.sms22239.Activities;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.uniba.dib.sms22239.Fragments.Fragment_toolbar;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbar1;
import it.uniba.dib.sms22239.Models.Offerta;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Registrazione_Offerte extends AppCompatActivity {

    EditText inputOggetto, inputProvincia, inputDescrizione;
    CheckBox proprietario, ente, veterinario;
    boolean checkProprietario, checkEnte, checkVeterinario;
    ImageButton allegato,photoBtn,submitBtn,backBtn2;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri mImageUri;
    ImageView mImageView;
    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;
    StorageTask mUploadTask;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creaofferta);

        autenticazione();

        inputOggetto = findViewById(R.id.oggettotext);
        inputProvincia = findViewById(R.id.provinciatext);
        inputDescrizione = findViewById(R.id.descrizione);

        allegato = findViewById(R.id.allegatoBtn);
        photoBtn = findViewById(R.id.photoBtn);
        submitBtn = findViewById(R.id.submitBtn);
        mImageView = findViewById(R.id.image_view);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("Offerte");

        allegato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Codice per aprire la fotocamera
                if (ContextCompat.checkSelfPermission(Activity_Registrazione_Offerte.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_Registrazione_Offerte.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                }
            }
        });


        backBtn2 = findViewById(R.id.back);

        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        proprietario = findViewById(R.id.check_box1);
        ente = findViewById(R.id.check_box2);
        veterinario = findViewById(R.id.check_box3);

        // Aggiungi un listener per gestire le checkbox
        proprietario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Esegui azioni in base allo stato della checkbox1
                if (isChecked) {
                    // la checkbox1 è stata selezionata
                } else {
                    // la checkbox1 è stata deselezionata
                }
            }
        });

        // Ripeti per le altre checkbox
        ente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Esegui azioni in base allo stato della checkbox2
                if (isChecked) {
                    // la checkbox2 è stata selezionata
                } else {
                    // la checkbox2 è stata deselezionata
                }
            }
        });

        veterinario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Esegui azioni in base allo stato della checkbox3
                if (isChecked) {
                    // la checkbox3 è stata selezionata
                } else {
                    // la checkbox3 è stata deselezionata
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                Offerta off = new Offerta();

                String oggetto = inputOggetto.getText().toString();
                String provincia = inputProvincia.getText().toString();
                String descrizione = inputDescrizione.getText().toString();
                checkProprietario = proprietario.isChecked();
                checkEnte = ente.isChecked();
                checkVeterinario = veterinario.isChecked();
                String c1= getString(R.string.ofs);
                off.writeOfferta(off, oggetto, provincia, descrizione,checkProprietario,checkEnte,checkVeterinario);
                Toast.makeText(Activity_Registrazione_Offerte.this, c1, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_Registrazione_Offerte.this, Activity_Segnalazioni_Offerte.class);
                intent.putExtra("OFFERTA_CODE", off.idOfferta);
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(Activity_Registrazione_Offerte.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(off);
                }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    private void uploadFile(Offerta offerta) {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(offerta.idOfferta+".jpg");

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String c2= getString(R.string.ups);
                            Toast.makeText(Activity_Registrazione_Offerte.this, c2, Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Activity_Registrazione_Offerte.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        }
                    });
        } else {
            String c3= getString(R.string.nf);
            Toast.makeText(this, c3, Toast.LENGTH_SHORT).show();
        }
    }

    protected void autenticazione()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
        Query query = myRef.orderByChild("classe");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Recupera il valore dell'attributo "classe"
                String classe = snapshot.child("classe").getValue(String.class);


                // Verifica il valore dell'attributo "classe"
                if (classe.equals("Veterinario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbar1());
                    fragmentTransaction.commit();
                    flag = "veterinario";
                }
                else
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbar());
                    fragmentTransaction.commit();
                    flag = "altro";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'evento di annullamento
                String c4= getString(R.string.a3);
                Log.e("Firebase", c4 + error.getMessage());
            }
        });
    }
}