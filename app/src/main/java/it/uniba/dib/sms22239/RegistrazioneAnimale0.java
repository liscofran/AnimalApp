package it.uniba.dib.sms22239;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;


public class RegistrazioneAnimale0 extends AppCompatActivity {
    EditText inputNome, inputRazza, inputData;
    RadioGroup inputSesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_animale_0);

        inputNome=findViewById(R.id.register_animal_name);
        inputRazza=findViewById(R.id.register_animal_species);
        inputData=findViewById(R.id.register_animal_birthdate);
        RadioGroup inputSesso = (RadioGroup) findViewById(R.id.register_animal_sex);
        final String[] sesso = new String[1];

        inputSesso.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the selected radio button from the group
                RadioButton radioButton1 = (RadioButton) group.findViewById(R.id.register_animal_male);
                if (radioButton1 != null) {
                    sesso[0] = "Maschio";
                }
                RadioButton radioButton2 = (RadioButton) group.findViewById(R.id.register_animal_female);
                if (radioButton2 != null) {
                    sesso[0] = "Femmina";
                }
            }
        });

        final Button[] generaAnimaleButton = {findViewById(R.id.register_animal_button)};
        generaAnimaleButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                Animale ani = new Animale();
                String nome = inputNome.getText().toString();
                String razza = inputRazza.getText().toString();
                String data = inputData.getText().toString();
                ani.writeNewAnimal(ani, nome, razza, sesso[0], data, currentUser.getUid());
                Intent intent = new Intent(RegistrazioneAnimale0.this, QRGenerate.class);
                intent.putExtra("ANIMAL_NAME", "Leone"); // sostituisci con il nome dell'animale dell'utente
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrazioneAnimale0.this, HomeActivity.class));
            }
        });
    }

}