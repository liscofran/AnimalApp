package it.uniba.dib.sms22239;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_Activity extends AppCompatActivity
{
    RelativeLayout relativeLayout;
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;

    private Button editProfileButton;

    private FirebaseRecyclerOptions<Proprietario> options;
    private FirebaseRecyclerAdapter<Proprietario, MyViewHolder> adapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase mDatabase;
   private TextView mNomeTextView;
   private TextView mCognomeTextView;
    private TextView mEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;


        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

        mNomeTextView = findViewById(R.id.user_nome);
        mCognomeTextView = findViewById(R.id.user_cognome);
        mEmailTextView = findViewById(R.id.user_email);

        // Recupera i dati dal database e popola le viste
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("nome").getValue(String.class);
                String cognome = dataSnapshot.child("cognome").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);

                //set delle variabili recuperate al layout
                mNomeTextView.setText(name);
                mCognomeTextView.setText(cognome);
                mEmailTextView.setText(email);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String newEmail = bundle.getString("newEmail");

            TextView userEmailTextView = findViewById(R.id.user_email);
            userEmailTextView.setText(newEmail);
        }

    }


}