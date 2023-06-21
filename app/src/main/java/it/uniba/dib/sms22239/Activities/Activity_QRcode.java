package it.uniba.dib.sms22239.Activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import it.uniba.dib.sms22239.Activities.Animali.Activity_Profilo_Animale;
import it.uniba.dib.sms22239.BluetoothReceiver;
import it.uniba.dib.sms22239.Fragments.Enti.Fragment_Toolbar_Ente;
import it.uniba.dib.sms22239.Fragments.Proprietari.Fragment_Toolbar_Proprietario;
import it.uniba.dib.sms22239.Fragments.Veterinari.Fragment_Toolbar_Veterinario;
import it.uniba.dib.sms22239.R;

public class Activity_QRcode extends AppCompatActivity
{
    private CodeScanner mCodeScanner;
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private String qrCodeResult;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1001;
    private static final int REQUEST_ENABLE_BT = 1002;
    private BluetoothReceiver bluetoothReceiver;
    private String flag;
    private ImageButton back;
    private String c2,c3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        autenticazione();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        AppCompatImageButton bluetoothButton = findViewById(R.id.bluetooth_button);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter != null) {
                    if (!bluetoothAdapter.isEnabled()) {
                        if (ContextCompat.checkSelfPermission(Activity_QRcode.this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
                            // L'app ha l'autorizzazione, avvia l'intent di attivazione del Bluetooth
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        } else {
                            // L'app non ha l'autorizzazione, richiedi all'utente di concedere l'autorizzazione
                            ActivityCompat.requestPermissions(Activity_QRcode.this, new String[]{Manifest.permission.BLUETOOTH}, REQUEST_BLUETOOTH_PERMISSIONS);
                        }

                    }
                }


                // BroadcastReceiver per ricevere l'intent inviato tramite Bluetooth
                BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // Controlla l'azione dell'intent per identificare l'intent desiderato
                        if (intent.getAction().equals("collegamento")) {
                            // Estrai i dati dall'intent
                            String animalCode = intent.getStringExtra("ANIMAL_CODE");
                            Intent intent2 = new Intent(Activity_QRcode.this, Activity_Profilo_Animale.class);
                            intent2.putExtra("ANIMAL_CODE", animalCode);
                            Toast.makeText(Activity_QRcode.this, c2, Toast.LENGTH_SHORT).show();
                            startActivity(intent2);

                            // Unregister il BroadcastReceiver dopo aver completato le operazioni
                            unregisterReceiver(this);
                        }
                    }
                };
              }
        });

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> {
            qrCodeResult = result.getText();
            runOnUiThread(this::startAnimalActivity);
        });
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    public void startAnimalActivity() {
        c2= getString(R.string.qr1);
        Intent intent = new Intent(Activity_QRcode.this, Activity_Profilo_Animale.class);
        intent.putExtra("ANIMAL_CODE", qrCodeResult);
        Toast.makeText(Activity_QRcode.this, c2, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        if (bluetoothReceiver != null) {
            unregisterReceiver(bluetoothReceiver);
        }
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Autorizzazione concessa per la fotocamera, puoi procedere
                } else {
                    // Autorizzazione negata per la fotocamera, gestisci di conseguenza (es. mostra un messaggio di avviso o richiedi l'autorizzazione di nuovo)
                }
                break;
            }
            case REQUEST_BLUETOOTH_PERMISSIONS:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Autorizzazioni Bluetooth ottenute, avvia il ricevitore Bluetooth
                    bluetoothReceiver = new BluetoothReceiver();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("collegamento");
                    registerReceiver(bluetoothReceiver, intentFilter);
                }
                else {
                    // Autorizzazioni Bluetooth negate, gestisci di conseguenza (es. mostra un messaggio di avviso)
                    c3= getString(R.string.bt1);
                    Toast.makeText(getApplicationContext(), c3, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
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
