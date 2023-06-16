package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Fragments.Fragment_Immagine;
import it.uniba.dib.sms22239.Fragments.Fragment_Testo;
import it.uniba.dib.sms22239.Fragments.Fragment_Video;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarEnte;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarProprietario;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarVeterinario;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Multimedia extends AppCompatActivity
{
    private String selectedItem,flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);

        autenticazione();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Spinner Tipo di Media
        Spinner spinner = findViewById(R.id.spinner1);
        spinner.setPrompt("");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.media_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
                switch(selectedItem)
                {
                    case "Immagine":
                        Fragment_Immagine immagineFragment = new Fragment_Immagine();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, immagineFragment)
                                .commit();
                        break;
                    case "Testo":
                        Fragment_Testo testoFragment = new Fragment_Testo();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, testoFragment)
                                .commit();
                        break;
                    case "Video":
                        Fragment_Video videoFragment = new Fragment_Video();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, videoFragment)
                                .commit();
                        break;
                    default:
                        onNothingSelected(parent);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String c1= getString(R.string.b5);

                Toast.makeText(Activity_Multimedia.this, c1, Toast.LENGTH_SHORT).show();
            }


        });
    }

    protected void autenticazione()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
        Query query = myRef.orderByChild("classe");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Recupera il valore dell'attributo "classe"
                String classe = snapshot.child("classe").getValue(String.class);


                // Verifica il valore dell'attributo "classe"
                if (classe.equals("Veterinario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbarVeterinario());
                    fragmentTransaction.commit();
                    flag = "veterinario";
                }
                else if(classe.equals("Proprietario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbarProprietario());
                    fragmentTransaction.commit();
                    flag = "proprietario";
                }
                else
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbarEnte());
                    fragmentTransaction.commit();
                    flag = "ente";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'evento di annullamento
                String c4= getString(R.string.a3);
                Log.e("Firebase", c4 + error.getMessage());
            }
        });
    }
}
