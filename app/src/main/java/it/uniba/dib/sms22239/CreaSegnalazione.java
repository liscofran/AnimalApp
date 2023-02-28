package it.uniba.dib.sms22239;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.CharacterIterator;

public class CreaSegnalazione extends AppCompatActivity {

    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    String selectedItem;
    ImageButton backBtn;


    EditText inputOggetto, inputProvincia, inputDescrizione;
    boolean checkProprietario, checkEnte, checkVeterinario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creasegnalazione);

        inputOggetto = findViewById(R.id.oggettotext);
        inputProvincia = findViewById(R.id.provinciatext);
        inputDescrizione = findViewById(R.id.descrizione);

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreaSegnalazione.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreaSegnalazione.this, Profile_Activity.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreaSegnalazione.this, RegistrazioneAnimale.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreaSegnalazione.this, QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreaSegnalazione.this, Preference.class));
            }
        });

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        checkBox1 = findViewById(R.id.check_box1);
        checkBox2 = findViewById(R.id.check_box2);
        checkBox3 = findViewById(R.id.check_box3);

        // Aggiungi un listener per gestire le checkbox
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        final ImageButton generaSegnalazione = findViewById(R.id.submitBtn);
        generaSegnalazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                Segnalazione sgn = new Segnalazione();

                String oggetto = inputOggetto.getText().toString();
                String provincia = inputProvincia.getText().toString();
                String descrizione = inputDescrizione.getText().toString();
                checkProprietario = checkBox1.isChecked();
                checkEnte = checkBox2.isChecked();
                checkVeterinario = checkBox3.isChecked();


                sgn.writeSegnalazione(sgn, oggetto, provincia, descrizione,checkProprietario,checkEnte,checkVeterinario );
                Intent intent = new Intent(CreaSegnalazione.this, SegnalazioniActivity.class);
                startActivity(intent);
            }


        });
    }
}
