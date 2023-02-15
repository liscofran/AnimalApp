package it.uniba.dib.sms22239;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile_Activity extends AppCompatActivity
{
    RelativeLayout relativeLayout;
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;

    private ImageView profileImage;
    private TextView profileName;
    private TextView profileEmail;
    private Button editProfileButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //con queste 2 righe si prende l'istanza attuale da firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        setContentView(R.layout.activity_profile);
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.name_text);
        profileEmail = findViewById(R.id.user_email);
        editProfileButton = findViewById(R.id.edit_profile_button);

        //se l'utente esiste , prende la mail dal database e la visualizza sul profilo
        if (currentUser != null) {
            String email = currentUser.getEmail();
            TextView emailTextView = findViewById(R.id.user_email);
            emailTextView.setText(email);
        }

        // Imposta l'immagine del profilo
        profileImage.setImageResource(R.drawable.eprofile);

        // Imposta un listener di clic sul pulsante di modifica profilo
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Activity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        Load_setting();

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile_Activity.this, HomeActivity.class));
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile_Activity.this, Profile_Activity.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile_Activity.this, Preference.class));
            }
        });

    }

    private void Load_setting() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        String orien = sp.getString("ORIENTATION", "false");
        if ("1".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        } else if ("2".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if ("3".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

    }

    private void logout() {
        mAuth.signOut();
        irMain();
    }

    private void irMain() {
        Intent intent = new Intent(Profile_Activity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(Profile_Activity.this, "Logout effettuato con successo", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onResume() {
        Load_setting();
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Recupera il Bundle restituito da EditProfileActivity
            Bundle bundle = data.getExtras();

            // Legge la nuova email dal Bundle
            String newEmail = bundle.getString("newEmail");

            // Aggiorna l'interfaccia utente con la nuova email
            profileEmail.setText(newEmail);

        }
    }


}