package it.uniba.dib.sms22239;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class RegistrazioneAnimale extends AppCompatActivity {

    EditText inputNome, inputRazza, inputData;
    RadioGroup inputSesso;
    String sesso;

    private static final int PICK_IMAGE_REQUEST = 1;

    Button mButtonChooseImage;
    Button mButtonUpload;
    ImageView mImageView;
    ProgressBar mProgressBar;

    Uri mImageUri;

    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;

    StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_animale);

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.register_animal_button);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("Animali");

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        inputNome=findViewById(R.id.register_animal_name);
        inputRazza=findViewById(R.id.register_animal_species);
        inputData=findViewById(R.id.register_animal_birthdate);
        inputSesso = findViewById(R.id.register_animal_sex);

        inputSesso.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the selected radio button from the group
                RadioButton radioButton1 = (RadioButton) group.findViewById(R.id.register_animal_male);
                if (radioButton1.isChecked()) {
                    sesso = "Maschio";
                }
                RadioButton radioButton2 = (RadioButton) group.findViewById(R.id.register_animal_female);
                if (radioButton2.isChecked()) {
                    sesso = "Femmina";
                }
            }
        });

        final Button generaAnimaleButton = findViewById(R.id.register_animal_button);
        generaAnimaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                Animale ani = new Animale();
                String nome = inputNome.getText().toString();
                String razza = inputRazza.getText().toString();
                String datatmp = inputData.getText().toString();
                String data = "";
                String immagine = "";
                CharacterIterator it = new StringCharacterIterator(datatmp);

                while (it.current() != CharacterIterator.DONE)
                {
                    if(it.getIndex() == 4 || it.getIndex() == 6 )
                    {
                        data = data + "/";
                    }
                    data = data + it.current();
                    it.next();
                }

                ani.writeNewAnimal(ani, nome, razza, currentUser.getUid(), sesso, data, immagine);

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(RegistrazioneAnimale.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(ani);
                }
                Intent intent = new Intent(RegistrazioneAnimale.this, QRGenerate.class);
                intent.putExtra("ANIMAL_CODE", ani.Id);
                startActivity(intent);

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrazioneAnimale.this, HomeActivity.class));
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

            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(Animale animale) {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));


            animale.immagine = String.valueOf(fileReference);
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(RegistrazioneAnimale.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload("Animale " + animale.Id,
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrazioneAnimale.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}