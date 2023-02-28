package it.uniba.dib.sms22239;

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
        {
            Toast.makeText(this,"Inserisci Email...", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this,"Formato della mail non valida",Toast.LENGTH_SHORT).show();
        }else
        {
            recoverPassword();
        }
    }

    private void recoverPassword()
    {
        //visualizza Progress
        progressDialog.setMessage("Invio istruzioni di recupero password a " + email);
        progressDialog.show();

        //ottieni recupero
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //inviati
                progressDialog.dismiss();
                Toast.makeText(Activity_Reset_Password.this, "Istruzioni per il reset della password inviati a " + email, Toast.LENGTH_SHORT).show();
                Toast.makeText(Activity_Reset_Password.this,"Password cambiata con successo a " + email,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Activity_Reset_Password.this, Activity_Main.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //invio fallito
                progressDialog.dismiss();
                Toast.makeText(Activity_Reset_Password.this,"Invio delle istruzioni fallito" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
