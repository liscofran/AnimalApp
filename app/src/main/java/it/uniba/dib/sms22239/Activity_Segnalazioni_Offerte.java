package it.uniba.dib.sms22239;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_Segnalazioni_Offerte extends AppCompatActivity {

    Button creasegnalazione;
    Button creaofferta;
    Button ricercasegnalazione;
    Button miesegnalazioni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazioniofferte);

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Segnalazioni_Offerte.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Segnalazioni_Offerte.this, Activity_Registrazione_Animale.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Segnalazioni_Offerte.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Segnalazioni_Offerte.this, Preference.class));
            }
        });

        creasegnalazione = findViewById(R.id.crea_animale);

        creasegnalazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Registrazione_Segnalazione.class);
                startActivity(intent);
            }
        });

        creaofferta = findViewById(R.id.creaofferta);

        creaofferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Crea_Offerta.class);
                startActivity(intent);
            }
        });

        ricercasegnalazione = findViewById(R.id.album_animali);

        ricercasegnalazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Ricerca_Segnalazione.class);
                startActivity(intent);
            }
        });

        miesegnalazioni = findViewById(R.id.miei_animali);

        miesegnalazioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Segnalazione.class);
                startActivity(intent);
            }
        });

    }
    public boolean isChangingConfigurations() {
        return false;
    }
}
