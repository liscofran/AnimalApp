package it.uniba.dib.sms22239.Activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.BluetoothReceiver;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbar;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbar1;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_QRcode extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    private CodeScanner mCodeScanner;
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private String qrCodeResult;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1001;
    private static final int REQUEST_ENABLE_BT = 1002;
    private BluetoothReceiver bluetoothReceiver;
    String flag;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        autenticazione();

        // Toolbar
        constraintLayout = findViewById(R.id.home_constraint_layout); // Importante per il tema
        loadSettings();

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
                if (bluetoothAdapter == null) {
                    Toast.makeText(getApplicationContext(), "Bluetooth non supportato dal dispositivo in uso", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verifica delle autorizzazioni Bluetooth
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                    // Richiedi le autorizzazioni necessarie
                    ActivityCompat.requestPermissions(Activity_QRcode.this,
                            new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                            REQUEST_BLUETOOTH_PERMISSIONS);
                    return;
                }

                if (!bluetoothAdapter.isEnabled()) {
                    // Il Bluetooth non è abilitato, richiedi all'utente di abilitarlo
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    // Avvia il ricevitore Bluetooth
                    bluetoothReceiver = new BluetoothReceiver();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(Intent.ACTION_SEND);
                    registerReceiver(bluetoothReceiver, intentFilter);
                }
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
        Intent intent = new Intent(Activity_QRcode.this, Activity_Animal_Profile.class);
        intent.putExtra("ANIMAL_CODE", qrCodeResult);
        Toast.makeText(Activity_QRcode.this, "QRcode scansionato con successo", Toast.LENGTH_SHORT).show();
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

    private void loadSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNightMode = sp.getBoolean("NIGHT", false);
        if (isNightMode) {
            constraintLayout.setBackgroundColor(Color.parseColor("#222222"));
        } else {
            constraintLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        String orientation = sp.getString("ORIENTATION", "false");
        if ("1".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        } else if ("2".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if ("3".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
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
            case REQUEST_BLUETOOTH_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Autorizzazioni Bluetooth ottenute, avvia il ricevitore Bluetooth
                    bluetoothReceiver = new BluetoothReceiver();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(Intent.ACTION_SEND);
                    registerReceiver(bluetoothReceiver, intentFilter);
                } else {
                    // Autorizzazioni Bluetooth negate, gestisci di conseguenza (es. mostra un messaggio di avviso)
                    Toast.makeText(getApplicationContext(), "Autorizzazioni Bluetooth negate", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            // Aggiungi altri blocchi 'case' per gestire altre richieste di permesso se necessario
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
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbar1());
                    fragmentTransaction.commit();
                    flag = "veterinario";
                }
                else
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbar());
                    fragmentTransaction.commit();
                    flag = "altro";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'evento di annullamento
                Log.e("Firebase", "Operazione annullata: " + error.getMessage());
            }
        });
    }
}
