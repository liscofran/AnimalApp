package it.uniba.dib.sms22239.Activities.Animali;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.Calendar;

import it.uniba.dib.sms22239.Fragments.Enti.Fragment_Toolbar_Ente;
import it.uniba.dib.sms22239.Fragments.Proprietari.Fragment_Toolbar_Proprietario;
import it.uniba.dib.sms22239.Fragments.Veterinari.Fragment_Toolbar_Veterinario;
import it.uniba.dib.sms22239.Models.Oggetto_Spesa;
import it.uniba.dib.sms22239.R;

public class Activity_Crea_Oggetto_Spesa extends AppCompatActivity {

    String flag;
    EditText inputNome, inputPrezzo, inputQuantita;
    Button inputData;
    ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_oggetto_spesa);

        String idAnimal = getIntent().getStringExtra("ANIMAL_CODE");

        inputData = findViewById(R.id.add_item_date);
        inputNome = findViewById(R.id.add_item_name);
        inputPrezzo = findViewById(R.id.add_item_price);
        inputQuantita = findViewById(R.id.add_item_quantity);
        backbutton = findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_Spese.class);
                intent.putExtra("ANIMAL_CODE",idAnimal);
                startActivity(intent);
            }
        });

        //Bottone Data
        inputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

       autenticazione();

        findViewById(R.id.add_item_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c1= getString(R.string.aaaa);

                Oggetto_Spesa og = new Oggetto_Spesa();
                String nome = inputNome.getText().toString();
                Double prezzo = Double.parseDouble(inputPrezzo.getText().toString());
                int quantita = Integer.parseInt(inputQuantita.getText().toString());
                String data = inputData.getText().toString();

                og.writeNewOggetto(og, nome,prezzo,quantita,data, idAnimal);
                Toast.makeText(Activity_Crea_Oggetto_Spesa.this,c1, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_Crea_Oggetto_Spesa.this, Activity_Spese.class);
                intent.putExtra("ANIMAL_CODE",idAnimal);
                startActivity(intent);
            }
        });

    }

    private void showDatePickerDialog() {
        // Imposta la data di default sul giorno corrente
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Crea il DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Imposta la data selezionata nel Button
                        Button registraSpesaOggetto = findViewById(R.id.add_item_date);
                        registraSpesaOggetto.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
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
                else
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
                // Gestisci l'evento di annullamento
                String c4= getString(R.string.a3);
                Log.e("Firebase", c4 + error.getMessage());
            }
        });
    }
}
