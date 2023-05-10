package it.uniba.dib.sms22239.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

import it.uniba.dib.sms22239.Models.Ente;
import it.uniba.dib.sms22239.Models.Proprietario;
import it.uniba.dib.sms22239.R;
import it.uniba.dib.sms22239.Models.Veterinario;


public class Activity_Register extends AppCompatActivity {

    TextView alreadyHaveaccount;
    EditText inputEmail,inputPassword,inputCConformPassword;
    Button btnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    Spinner spinner;
    Boolean flag;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(it.uniba.dib.sms22239.R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveaccount=findViewById(it.uniba.dib.sms22239.R.id.alreadyHaveaccount);
        inputEmail=findViewById(it.uniba.dib.sms22239.R.id.inputEmail);
        inputPassword=findViewById(it.uniba.dib.sms22239.R.id.inputPassword);
        inputCConformPassword=findViewById(it.uniba.dib.sms22239.R.id.inputConformPassword);
        btnRegister=findViewById(it.uniba.dib.sms22239.R.id.btnRegister);
        progressDialog= new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        alreadyHaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(Activity_Register.this, Activity_Main.class));
            }
        });

        spinner = findViewById(it.uniba.dib.sms22239.R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.reg_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                flag = false;
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PerforAuth();
            }
        });
    }

    private void PerforAuth()
    {
        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();
        String confirmPassword=inputCConformPassword.getText().toString();
        flag = true;


        if(!email.matches(emailPattern))
        {
            inputEmail.setError("Enter Connext Email");
            inputEmail.requestFocus();
        } else if(password.isEmpty() || password.length()<6)
        {
            inputPassword.setError("Enter Proper Password");
        } else if(!password.equals(confirmPassword))
        {
            inputCConformPassword.setError("Password Not Match Both Field");
        } else
        {
            progressDialog.setMessage("Please Wait While Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful() && flag)
                    {
                        switch(selectedItem)
                        {
                            case "Proprietario":
                                Proprietario prop = new Proprietario();
                                prop.writeNewUser(prop, email, password, currentUser.getUid());
                                break;
                            case "Ente":
                                Ente ente = new Ente();
                                ente.writeNewUser(ente, email, password, currentUser.getUid());
                                break;
                            case "Veterinario":
                                Veterinario vet = new Veterinario();
                                vet.writeNewUser(vet, email, password, currentUser.getUid());
                                break;
                            default:
                                break;
                        }

                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(Activity_Register.this, "Completa il tuo profilo !", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Register.this, ""+task.getException(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void sendUserToNextActivity()
    {
        Intent intent = new Intent(Activity_Register.this, Activity_Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}