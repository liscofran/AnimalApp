package it.uniba.dib.sms22239;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Crea_Offerta extends AppCompatActivity {

    ImageButton backBtn2;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    Spinner spinner1;
    String selectedItem;

    String categoria;
    EditText inputOggetto, inputProvincia, inputDescrizione;
    boolean checkProprietario, checkEnte, checkVeterinario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creaofferta);

        inputOggetto = findViewById(R.id.oggettotext);
        inputProvincia = findViewById(R.id.provinciatext);
        inputDescrizione = findViewById(R.id.descrizione);

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Crea_Offerta.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Crea_Offerta.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Crea_Offerta.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Crea_Offerta.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Crea_Offerta.this, Preference.class));
            }
        });

        backBtn2 = findViewById(R.id.back);
        backBtn2.setOnClickListener(new View.OnClickListener() {
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
        spinner1.setPrompt("");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categoria_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
                switch(selectedItem)
                {
                    case "Categoria":
                        onNothingSelected(parent);
                        break;
                    case "Offro":
                        Toast.makeText(Activity_Crea_Offerta.this, "Hai selezionato Offro", Toast.LENGTH_SHORT).show();
                        break;
                    case "Cerco":
                        Toast.makeText(Activity_Crea_Offerta.this, "Hai selezionato Cerco", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        onNothingSelected(parent);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Activity_Crea_Offerta.this, "Scelta non valida", Toast.LENGTH_SHORT).show();
            }
        });

        final ImageButton generaOfferta = findViewById(R.id.submitBtn);
        generaOfferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                Offerta off = new Offerta();


                categoria = spinner1.getSelectedItem().toString();
                String oggetto = inputOggetto.getText().toString();
                String provincia = inputProvincia.getText().toString();
                String descrizione = inputDescrizione.getText().toString();
                checkProprietario = checkBox1.isChecked();
                checkEnte = checkBox2.isChecked();
                checkVeterinario = checkBox3.isChecked();


                off.writeOfferta(off, categoria, oggetto, provincia, descrizione,checkProprietario,checkEnte,checkVeterinario);
                Intent intent = new Intent(Activity_Crea_Offerta.this, Activity_Segnalazioni_Offerte.class);
                startActivity(intent);
            }


        });
    }
}
