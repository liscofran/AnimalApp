package it.uniba.dib.sms22239.Activities.Animali;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Random;

import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Fragments.Enti.Fragment_Toolbar_Ente;
import it.uniba.dib.sms22239.Fragments.Proprietari.Fragment_Toolbar_Proprietario;
import it.uniba.dib.sms22239.Fragments.Veterinari.Fragment_Toolbar_Veterinario;
import it.uniba.dib.sms22239.Models.Animale;
import it.uniba.dib.sms22239.R;

public class Activity_Registrazione_Animale extends AppCompatActivity
{

    String flag;
    private EditText inputNome, inputRazza, inputpatologie, inputprefcibo, inputLuogo;
    private String sesso, selectedItem;
    private Button inputData;
    private static final int PICK_IMAGE_REQUEST = 1;
    protected TextView generaAnimaleButton;
    private ProgressBar mProgressBar;
    protected Uri mImageUri;
    protected StorageTask mUploadTask;
    protected RadioGroup inputSesso;
    protected Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_animale);

        autenticazione();
        TextView mButtonChooseImage = findViewById(R.id.button_choose_image);
        mProgressBar = findViewById(R.id.progress_bar);
        ImageButton backBtn2 = findViewById(R.id.back);

        //Tasto Back
        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        //Bottone Scelta dell'immagine dalla galleria
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        //Scelta del sesso
        inputSesso = findViewById(R.id.register_animal_sex);
        inputSesso.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton radioButton1 = group.findViewById(R.id.register_animal_male);
                if (radioButton1.isChecked()) {
                    sesso = "Maschio";
                }
                RadioButton radioButton2 = group.findViewById(R.id.register_animal_female);
                if (radioButton2.isChecked()) {
                    sesso = "Femmina";
                }
            }
        });

        //Spinner per la scelta della proprietà
        spinner = findViewById(R.id.spinner);
        spinner.setPrompt("");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.proprieta_options, android.R.layout.simple_spinner_item);
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
                String c1= getString(R.string.b5);
                Toast.makeText(Activity_Registrazione_Animale.this, c1, Toast.LENGTH_SHORT).show();
            }
        });

        inputNome = findViewById(R.id.register_animal_name);
        inputRazza = findViewById(R.id.register_animal_species);
        inputpatologie = findViewById(R.id.register_animal_patologie);
        inputprefcibo = findViewById(R.id.register_animal_prefcibo);
        inputData = findViewById(R.id.register_animal_birthdate);
        inputLuogo = findViewById(R.id.register_animal_luogo);

        //Bottone Data
        inputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //Bottone per la creazione e registrazione nel db dell'animale
        generaAnimaleButton = findViewById(R.id.register_animal_button);
        generaAnimaleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Animale ani = new Animale();
                String nome = inputNome.getText().toString();
                String razza = inputRazza.getText().toString();
                String patologie = inputpatologie.getText().toString();
                String preferenzecibo = inputprefcibo.getText().toString();
                String data = inputData.getText().toString();
                String luogo = inputLuogo.getText().toString();
                String prop = selectedItem;

                long seed = System.currentTimeMillis(); // ottenere il tempo corrente
                Random random = new Random(seed); // creare un oggetto Random con il tempo come seme
                int tmp = Math.abs(random.nextInt()); // generare un numero casuale

                if (mUploadTask != null && mUploadTask.isInProgress())
                {
                    Toast.makeText(Activity_Registrazione_Animale.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    uploadFile(ani,tmp);
                }
                String c2= getString(R.string.an1);
                ani.writeNewAnimal(tmp, ani, nome, razza, patologie, preferenzecibo, currentUser.getUid(), sesso, data, prop, luogo);
                Toast.makeText(Activity_Registrazione_Animale.this, c2, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_Registrazione_Animale.this, Activity_Home.class);
                intent.putExtra("ANIMAL_CODE", ani.Id);
                startActivity(intent);
            }
        });
    }

    //Metodo per la gestione della scelta dalla galleria
    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //Metodo che gestisce il risultato ottenuto dalla scelta della foto nella galleria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

          // Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    //Estensione del file
    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void showDatePickerDialog() {
        // Imposta la data di default sul giorno corrente
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Crea il DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Imposta la data selezionata nell'Edit Text
                        Button registerAnimalBirthdateEditText = findViewById(R.id.register_animal_birthdate);
                        registerAnimalBirthdateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    //Upload dell'immagine sullo storage
    private void uploadFile(Animale animale, int id)
    {
        if (mImageUri != null)
        {
            animale.immagine = "ImmagineProfilo." + getFileExtension(mImageUri);
            //Riferimento Storage agli animali
            StorageReference fileReference = FirebaseStorage.getInstance().getReference("Animali").child(Integer.toString(id)).child(animale.immagine);

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
                            String c3= getString(R.string.ups);
                            Toast.makeText(Activity_Registrazione_Animale.this, c3, Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Activity_Registrazione_Animale.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        }
        else
        {                       String c4= getString(R.string.nf);

            Toast.makeText(this, c4, Toast.LENGTH_SHORT).show();
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
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_Toolbar_Veterinario());
                    fragmentTransaction.commit();
                    flag = "veterinario";
                }
                else if(classe.equals("Proprietario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_Toolbar_Proprietario());
                    fragmentTransaction.commit();
                    flag = "proprietario";
                }
                else
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_Toolbar_Ente());
                    fragmentTransaction.commit();
                    flag = "ente";
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