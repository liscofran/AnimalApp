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

import java.text.CharacterIterator;
import java.text.ParseException;
import java.text.StringCharacterIterator;


public class RegistrazioneAnimale0 extends AppCompatActivity {
    EditText inputNome, inputRazza, inputData;
    RadioGroup inputSesso;
    String sesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_animale_0);

        inputNome=findViewById(R.id.register_animal_name);
        inputRazza=findViewById(R.id.register_animal_species);
        inputData=findViewById(R.id.register_animal_birthdate);
        inputSesso = (RadioGroup) findViewById(R.id.register_animal_sex);
        ;

        inputSesso.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the selected radio button from the group
                RadioButton radioButton1 = (RadioButton) group.findViewById(R.id.register_animal_male);
                if (radioButton1 != null) {
                    sesso = "Maschio";
                }
                RadioButton radioButton2 = (RadioButton) group.findViewById(R.id.register_animal_female);
                if (radioButton2 != null) {
                    sesso = "Femmina";
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

                ani.writeNewAnimal(ani, nome, razza, currentUser.getUid(), sesso, data);
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