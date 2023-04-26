package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;


public class Activity_Animali extends AppCompatActivity
{
    TextView crea_animale;
    TextView miei_animali;
    TextView album_animali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animali);


        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Animali.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Animali.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Animali.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Animali.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Animali.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Animali.this, Preference.class));
            }
        });

        crea_animale = findViewById(R.id.crea_animale);

        crea_animale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Animali.this, Activity_Registrazione_Animale.class);
                startActivity(intent);
            }
        });

        miei_animali = findViewById(R.id.Miei_animali);

        miei_animali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Animali.this, Activity_Miei_Animali.class);
                startActivity(intent);
            }
        });

        album_animali = findViewById(R.id.album_animali);

        album_animali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Activity_Animali.this, Activity_Album_Animali.class);
                startActivity(intent);
            }
        });

    }

    public boolean isChangingConfigurations() {
        return false;
    }
}

