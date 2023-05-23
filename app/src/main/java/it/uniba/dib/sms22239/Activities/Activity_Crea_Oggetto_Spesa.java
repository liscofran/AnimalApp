package it.uniba.dib.sms22239.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Calendar;

import it.uniba.dib.sms22239.Models.Oggetto_Spesa;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Crea_Oggetto_Spesa extends AppCompatActivity {
    EditText inputNome, inputPrezzo, inputQuantita;
    Button inputData;
    ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_oggetto_spesa);

        String idAnimal = getIntent().getStringExtra("ANIMAL_CODE");

        inputData = findViewById(R.id.add_item_date);
        inputNome = findViewById(R.id.add_item_name);
        inputPrezzo = findViewById(R.id.add_item_price);
        inputQuantita = findViewById(R.id.add_item_quantity);

        backbutton = findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Bottone Data
        inputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Imposta la Toolbar come action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_Home.class));
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
                startActivity(new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_QRcode.class));
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
                String data = inputData.getText().toString();

                og.writeNewOggetto(og, nome,prezzo,quantita,data, idAnimal);
                Toast.makeText(Activity_Crea_Oggetto_Spesa.this,"Oggetto salvato con successo", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_Spese.class);
                intent.putExtra("ANIMAL_CODE",idAnimal);
                startActivity(intent);
            }
        });

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
                        // Imposta la data selezionata nel Button
                        Button registraSpesaOggetto = findViewById(R.id.add_item_date);
                        registraSpesaOggetto.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}
