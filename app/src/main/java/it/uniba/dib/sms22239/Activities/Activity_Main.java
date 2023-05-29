package it.uniba.dib.sms22239.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.uniba.dib.sms22239.R;

public class Activity_Main extends AppCompatActivity
{
    TextView createnewAccount;
    EditText inputEmail,inputPassword;
    Button btnLogin;
    TextView forgotPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageView btnGuest,btnGuest2,btnGuest3;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            Toast.makeText(Activity_Main.this, "Login eseguito per: " + currentUser.getEmail(),
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Activity_Main.this, Activity_Home.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createnewAccount=findViewById(R.id.createNewAccount);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnGuest = findViewById(R.id.btnGuest);
        btnGuest2 = findViewById(R.id.btnGuest2);
        btnGuest3 = findViewById(R.id.btnGuest3);
        forgotPassword=findViewById(R.id.forgotPassword);
        progressDialog= new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        createnewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Activity_Main.this, Activity_Register.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                perforLogin();
            }
        });

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                inputEmail.setText("proprietario@gmail.com");
                inputPassword.setText("proprietario");
                perforLogin();
            }
        });

        btnGuest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                inputEmail.setText("ente@gmail.com");
                inputPassword.setText("guestente");
                perforLogin();
            }
        });

        btnGuest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                inputEmail.setText("veterinario@gmail.com");
                inputPassword.setText("veterinario");
                perforLogin();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Main.this, Activity_Reset_Password.class);
                startActivity(intent);
            }
        });

    }

    private void perforLogin()
    {
        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();

        if(!email.matches(emailPattern))
        {
            inputEmail.setError("Inserisci un'email corretta:");
            inputEmail.requestFocus();
        } else if(password.isEmpty() || password.length()<6)
        {
            inputPassword.setError("Inserisci una password appropriata:");
        }else
        {
            progressDialog.setMessage("Per favore attendi durante il login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(Activity_Main.this, "Login avvenuto con successo!", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Main.this, ""+ task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity()
    {
        Intent intent = new Intent(Activity_Main.this, Activity_Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}