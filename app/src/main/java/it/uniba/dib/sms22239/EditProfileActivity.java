package it.uniba.dib.sms22239;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editEmail;
    private Button saveProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Collega i componenti dell'interfaccia utente con le variabili
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        saveProfileButton = findViewById(R.id.save_profile_button);


        // Imposta un listener di clic sul pulsante di salvataggio del profilo
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Salva i dati del profilo e torna all'activity precedente
                String newName = editName.getText().toString();
                String newEmail = editEmail.getText().toString();

                // Creazione del Bundle per passare la nuova email a UserProfileActivity
                Bundle bundle = new Bundle();
                bundle.putString("newEmail", newEmail);

                // Impostazione del risultato dell'Activity e chiusura dell'Activity
                Intent resultIntent = new Intent();
                resultIntent.putExtras(bundle);
                setResult(Activity.RESULT_OK, resultIntent);
                Intent intent = new Intent(EditProfileActivity.this, Profile_Activity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                // startActivity(new Intent(EditProfileActivity.this, Profile_Activity.class));
//
//                // Crea un intent per tornare all'activity precedente
//                Intent intent = new Intent();
//                intent.putExtra("name", newName);
//                intent.putExtra("email", newEmail);
//                setResult(RESULT_OK, intent);
//
//                // Lettura della nuova email dall'EditText
//                newEmail = editEmail.getText().toString();

                finish();
            }
        });
    }

}