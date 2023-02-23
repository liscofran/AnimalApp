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
    Spinner spinner1;
    String selectedItem;
    ImageButton backBtn;


    String categoria;
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

        //Spinner Categoria
        spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.reg_categoria, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
                switch (selectedItem) {
                    case "":
                        onNothingSelected(parent);
                        break;
                    case "Opzione 1":
                        Toast.makeText(CreaSegnalazione.this, "Hai selezionato Opzione 1", Toast.LENGTH_SHORT).show();
                        break;
                    case "Opzione 2":
                        Toast.makeText(CreaSegnalazione.this, "Hai selezionato Opzione 2", Toast.LENGTH_SHORT).show();
                        break;
                    case "Opzione 3":
                        Toast.makeText(CreaSegnalazione.this, "Hai selezionato Opzione 3", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CreaSegnalazione.this, "Non hai effettuato nessuna selezione nella Categoria", Toast.LENGTH_SHORT).show();
            }
        });

        //Spinner Provincia
        /* spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.reg_categoria, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
                switch (selectedItem) {
                    case "":
                        onNothingSelected(parent);
                        break;
                    case "Opzione 1":
                        Toast.makeText(CreaSegnalazione.this, "Hai selezionato Opzione 1", Toast.LENGTH_SHORT).show();
                        break;
                    case "Opzione 2":
                        Toast.makeText(CreaSegnalazione.this, "Hai selezionato Opzione 2", Toast.LENGTH_SHORT).show();
                        break;
                    case "Opzione 3":
                        Toast.makeText(CreaSegnalazione.this, "Hai selezionato Opzione 3", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CreaSegnalazione.this, "Non hai effettuato nessuna selezione nella Provincia", Toast.LENGTH_SHORT).show();
            }
        });*/

        //Spinner Oggetto
        //spinner3 = findViewById(R.id.spinner3);
        /*ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.reg_categoria, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
                switch (selectedItem) {
                    case "":
                        onNothingSelected(parent);
                        break;
                    case "Opzione 1":
                        Toast.makeText(CreaSegnalazione.this, "Hai selezionato Opzione 1", Toast.LENGTH_SHORT).show();
                        break;
                    case "Opzione 2":
                        Toast.makeText(CreaSegnalazione.this, "Hai selezionato Opzione 2", Toast.LENGTH_SHORT).show();
                        break;
                    case "Opzione 3":
                        Toast.makeText(CreaSegnalazione.this, "Hai selezionato Opzione 3", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CreaSegnalazione.this, "Non hai effettuato nessuna selezione nell'Oggetto", Toast.LENGTH_SHORT).show();
            }
        });*/

        final ImageButton generaSegnalazione = findViewById(R.id.submitBtn);
        generaSegnalazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                Segnalazione sgn = new Segnalazione();


                categoria = spinner1.getSelectedItem().toString();
                String oggetto = inputOggetto.getText().toString();
                String provincia = inputProvincia.getText().toString();
                String descrizione = inputDescrizione.getText().toString();
                checkProprietario = checkBox1.isChecked();
                checkEnte = checkBox2.isChecked();
                checkVeterinario = checkBox3.isChecked();


                sgn.writeSegnalazione(sgn, categoria, oggetto, provincia, descrizione,checkProprietario,checkEnte,checkVeterinario );
                Intent intent = new Intent(CreaSegnalazione.this, SegnalazioniActivity.class);
                startActivity(intent);
            }


        });
    }
}
