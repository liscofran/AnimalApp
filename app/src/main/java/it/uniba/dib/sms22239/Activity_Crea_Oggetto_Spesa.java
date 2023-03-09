package it.uniba.dib.sms22239;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class Activity_Crea_Oggetto_Spesa extends AppCompatActivity {
    EditText inputData, inputNome, inputPrezzo, inputQuantita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_oggetto_spesa);

<<<<<<< HEAD
        String idAnimal = getIntent().getStringExtra("ANIMAL_CODE");

=======
>>>>>>> origin/main
        inputData = findViewById(R.id.add_item_date);
        inputNome = findViewById(R.id.add_item_name);
        inputPrezzo = findViewById(R.id.add_item_price);
        inputQuantita = findViewById(R.id.add_item_quantity);

        // Imposta la Toolbar come action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
                startActivity(new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_Home.class));
=======
                Intent intent = new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_Home.class);
                startActivity(intent);
>>>>>>> origin/main
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
=======
                startActivity(new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_Segnalazioni_Offerte.class));
>>>>>>> origin/main
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
=======
                startActivity(new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_Animali.class));
>>>>>>> origin/main
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
=======
                startActivity(new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_QRcode.class));
>>>>>>> origin/main
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Crea_Oggetto_Spesa.this, Preference.class));
            }
        });

        findViewById(R.id.add_item_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Oggetto_Spesa og = new Oggetto_Spesa();
                String nome = inputNome.getText().toString();
                Double prezzo = Double.parseDouble(inputPrezzo.getText().toString());
                int quantita = Integer.parseInt(inputQuantita.getText().toString());
                String datatmp = inputData.getText().toString();
                String data = "";
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

<<<<<<< HEAD
                og.writeNewOggetto(og, nome,prezzo,quantita,data, idAnimal);
                startActivity(new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_Spese.class));
=======
                og.writeNewOggetto(og, nome,prezzo,quantita,data);
                startActivity(new Intent(Activity_Crea_Oggetto_Spesa.this, Spese.class));
>>>>>>> origin/main
            }
        });

    }
}
