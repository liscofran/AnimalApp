package it.uniba.dib.sms22239;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RegistrazioneAnimale extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_animale);

        Button generaQRCodeButton = findViewById(R.id.genera_qr_code_button);
        generaQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrazioneAnimale.this, QRGenerate.class);
                intent.putExtra("ANIMAL_NAME", "Leone"); // sostituisci con il nome dell'animale dell'utente
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrazioneAnimale.this, HomeActivity.class));
            }
        });
    }

}