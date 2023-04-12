package it.uniba.dib.sms22239.Activities;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creaofferta);

        inputOggetto = findViewById(R.id.oggettotext);
        inputProvincia = findViewById(R.id.provinciatext);
        inputDescrizione = findViewById(R.id.descrizione);

        allegato = findViewById(R.id.allegatoBtn);
        photoBtn = findViewById(R.id.photoBtn);
        submitBtn = findViewById(R.id.submitBtn);
        mImageView = findViewById(R.id.image_view);

        mStorageRef = FirebaseStorage.getInstance().getReference("Offerte");

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

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Registrazione_Offerte.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Registrazione_Offerte.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Registrazione_Offerte.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Registrazione_Offerte.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Registrazione_Offerte.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Registrazione_Offerte.this, Preference.class));
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

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(Activity_Registrazione_Offerte.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(off);
                }
                off.writeOfferta(off, oggetto, provincia, descrizione,checkProprietario,checkEnte,checkVeterinario);
                Intent intent = new Intent(Activity_Registrazione_Offerte.this, Activity_Segnalazioni_Offerte.class);
                intent.putExtra("OFFERTA_CODE", off.idOfferta);
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private Offerta uploadFile(Offerta offerta) {
        if (mImageUri != null) {
            long time = System.currentTimeMillis();
            offerta.immagine = time + "." + getFileExtension(mImageUri);
            StorageReference fileReference = mStorageRef.child(offerta.immagine);

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Activity_Registrazione_Offerte.this, "Upload successful", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
        return offerta;
    }
}