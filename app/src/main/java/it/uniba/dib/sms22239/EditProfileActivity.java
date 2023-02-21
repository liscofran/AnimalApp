package it.uniba.dib.sms22239;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName;

    private EditText editCognome;

    private TextView mNomeTextView;
    private TextView mCognomeTextView;

    private Button saveProfileButton;

    private FirebaseAuth mAuth;

    private FirebaseDatabase userDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;

        mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

        // Collega i componenti dell'interfaccia con le variabili
        editName = findViewById(R.id.edit_name);
        editCognome = findViewById(R.id.edit_cognome);


        saveProfileButton = findViewById(R.id.save_profile_button);

        mNomeTextView = findViewById(R.id.edit_name);
        mCognomeTextView = findViewById(R.id.edit_cognome);

        // Recupera i dati dal database e popola i campi
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //recupero dati e assegnazione alle variabili
                String name = dataSnapshot.child("nome").getValue(String.class);
                String cognome = dataSnapshot.child("cognome").getValue(String.class);

                //set delle variabili recuperate al layout
                mNomeTextView.setText(name);
                mCognomeTextView.setText(cognome);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
             });

            // Imposta un listener di clic sul pulsante di salvataggio del profilo
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Salva i dati del profilo e torna all'activity precedente
                DatabaseReference mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

                //prende i dati inseriti in input e gli assegna alle variabili temporanee
                String newName = editName.getText().toString();
                String newCognome = editCognome.getText().toString();

                //modifica e salva i dati anche sul database
                mDatabase.child("cognome").setValue(newCognome);
                mDatabase.child("nome").setValue(newName);

                //intent per tornare alla schermata del profilo
                Intent intent = new Intent(EditProfileActivity.this, Profile_Activity.class);
                startActivity(intent);

                finish();
            }
        });
    }

}