package it.uniba.dib.sms22239.Activities.Animali;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Fragments.Animali.Fragment_Profilo_Animale;
import it.uniba.dib.sms22239.Fragments.Animali.Fragment_Profilo_Animale_Senza_Modifica;
import it.uniba.dib.sms22239.Fragments.Enti.Fragment_Toolbar_Ente;
import it.uniba.dib.sms22239.Fragments.Proprietari.Fragment_Toolbar_Proprietario;
import it.uniba.dib.sms22239.Fragments.Veterinari.Fragment_Toolbar_Veterinario;
import it.uniba.dib.sms22239.Models.Animale;
import it.uniba.dib.sms22239.Models.Proprietario;
import it.uniba.dib.sms22239.R;

public class Activity_Profilo_Animale extends AppCompatActivity
{
    protected String id_utente;
    protected String id_utente_animale;
    String flag;
    String c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_animal);

        autenticazione();

        String idAnimale = getIntent().getStringExtra("ANIMAL_CODE");

        //Reference al Database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimale);
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Animale ani = task.getResult().getValue(Animale.class);
                    //recupero dati e assegnazione alle variabili
                    id_utente_animale = ani.Id_utente;

                    mDatabase1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                Proprietario prop = task.getResult().getValue(Proprietario.class);
                                //recupero dati e assegnazione alle variabili
                                id_utente = prop.userId;
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                //Se l'utente che sta accedendo al profilo dell'animale è il proprietario permette di accedere
                                //a modifiche e dati ulteriori

                                if (id_utente_animale.equals(id_utente)) {
                                    fragmentTransaction.replace(R.id.fragment_container, new Fragment_Profilo_Animale());
                                    fragmentTransaction.commit();
                                } else {
                                    fragmentTransaction.replace(R.id.fragment_container, new Fragment_Profilo_Animale_Senza_Modifica());
                                    fragmentTransaction.commit();
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    protected void autenticazione()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
        Query query = myRef.orderByChild("classe");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Recupera il valore dell'attributo "classe"
                String classe = snapshot.child("classe").getValue(String.class);


                // Verifica il valore dell'attributo "classe"
                if (classe.equals("Veterinario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_Toolbar_Veterinario());
                    fragmentTransaction.commit();
                    flag = "veterinario";
                }
                else if(classe.equals("Proprietario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_Toolbar_Proprietario());
                    fragmentTransaction.commit();
                    flag = "proprietario";
                }
                else if(classe.equals("Ente"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_Toolbar_Ente());
                    fragmentTransaction.commit();
                    flag = "ente";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                c1= getString(R.string.a3);
                // Gestisci l'evento di annullamento
                Log.e("Firebase", c1 + error.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}