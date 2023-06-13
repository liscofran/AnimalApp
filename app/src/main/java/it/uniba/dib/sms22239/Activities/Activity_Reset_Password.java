package it.uniba.dib.sms22239.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import it.uniba.dib.sms22239.R;
import it.uniba.dib.sms22239.databinding.ActivityResetBinding;

public class Activity_Reset_Password extends AppCompatActivity
{
    private ActivityResetBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Button btnEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityResetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        btnEmail=findViewById(R.id.btnEmail);

        //inizializzazione firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //inizializzazione/setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Aspetta..");
        progressDialog.setCanceledOnTouchOutside(false);

        //gestisci il click, torna indietro
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //gestisci il click, recupero password
        binding.btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private String email = "";
    private void validateData()
    {
        //ottieni dati p.es. email
        email = binding.inputEmail.getText().toString().trim();

        //valida i dati p.es non deve essere vuoto e in formato valido
        if(email.isEmpty())
        {       String c6= getString(R.string.gm2);
            Toast.makeText(this,c6, Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            String c7= getString(R.string.fme);
            Toast.makeText(this,c7,Toast.LENGTH_SHORT).show();
        }else
        {
            recoverPassword();
        }
    }

    private void recoverPassword()
    {               String c3= getString(R.string.inserisci_mail1);

        //visualizza Progress
        progressDialog.setMessage(c3 + email);
        progressDialog.show();

        //ottieni recupero
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //inviati
                progressDialog.dismiss();
                String c9= getString(R.string.rpw);
                String c10= getString(R.string.rpw2);
                Toast.makeText(Activity_Reset_Password.this, c9 + email, Toast.LENGTH_SHORT).show();
                Toast.makeText(Activity_Reset_Password.this,c10 + email,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Activity_Reset_Password.this, Activity_Main.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //invio fallito
                progressDialog.dismiss();
                String c11= getString(R.string.isf);
                Toast.makeText(Activity_Reset_Password.this,c11 + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
