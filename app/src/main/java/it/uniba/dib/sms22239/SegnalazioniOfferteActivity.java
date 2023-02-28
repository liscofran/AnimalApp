package it.uniba.dib.sms22239;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class SegnalazioniOfferteActivity extends AppCompatActivity {

    Button creasegnalazione;
    Button creaofferta;
    Button ricercasegnalazione;
    Button miesegnalazioni;

    String categoria;
    String oggetto;
    String provincia;
    String descrizione;
    //String visibile;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazioniofferte);

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SegnalazioniOfferteActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SegnalazioniOfferteActivity.this, Profile_Activity.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SegnalazioniOfferteActivity.this, RegistrazioneAnimale.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SegnalazioniOfferteActivity.this, QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SegnalazioniOfferteActivity.this, Preference.class));
            }
        });

        creasegnalazione = findViewById(R.id.creasegnalazione);

        creasegnalazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SegnalazioniOfferteActivity.this,RegistrazioneSegnalazione.class);
                startActivity(intent);
            }
        });

        creaofferta = findViewById(R.id.creaofferta);

        creaofferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SegnalazioniOfferteActivity.this,CreaOfferta.class);
                startActivity(intent);
            }
        });

        ricercasegnalazione = findViewById(R.id.ricercasegnalazioni);

        ricercasegnalazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SegnalazioniOfferteActivity.this,RicercaSegnalazione.class);
                startActivity(intent);
            }
        });

        miesegnalazioni = findViewById(R.id.miesegnalazioni);

        miesegnalazioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SegnalazioniOfferteActivity.this,SegnalazioneActivity.class);
                startActivity(intent);
            }
        });

    }

    public boolean isChangingConfigurations() {
        return false;
    }




}
