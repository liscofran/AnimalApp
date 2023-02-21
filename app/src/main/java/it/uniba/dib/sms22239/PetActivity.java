package it.uniba.dib.sms22239;




import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miei_animali);


        Button aggiungiAnimaleButton = findViewById(R.id.add_animal_button);
        aggiungiAnimaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetActivity.this, RegistrazioneAnimale.class);
                intent.putExtra("ANIMAL_NAME", "Leone"); // sostituisci con il nome dell'animale dell'utente
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PetActivity.this, HomeActivity.class));
            }
        });
    }


}