package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Segnalazioni_Offerte extends AppCompatActivity {

    Button creasegnalazione;
    Button creaofferta;
    Button ricercasegnalazione;
    Button ricercaofferta;
    Button miesegnalazioni;
    Button mieofferte;

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
                startActivity(new Intent(Activity_Segnalazioni_Offerte.this, Activity_Animali.class));
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

        creasegnalazione = findViewById(R.id.crea_segnalazione);

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

        mieofferte = findViewById(R.id.mieofferte);

        mieofferte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Offerte.class);
                startActivity(intent);
            }
        });

        ricercasegnalazione = findViewById(R.id.ricerca_segnalazione);

        ricercasegnalazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Ricerca_Segnalazione.class);
                startActivity(intent);
            }
        });

        miesegnalazioni = findViewById(R.id.mie_segnalazioni);

        miesegnalazioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Segnalazione.class);
                startActivity(intent);
            }
        });

        ricercaofferta = findViewById(R.id.ricercaofferte);

        ricercaofferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Ricerca_Offerta.class);
                startActivity(intent);
            }
        });

    }
    public boolean isChangingConfigurations() {
        return false;
    }
}
