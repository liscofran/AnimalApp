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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

import it.uniba.dib.sms22239.BluetoothReceiver;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        // Toolbar
        constraintLayout = findViewById(R.id.home_constraint_layout); // Importante per il tema
        Toolbar toolbar = findViewById(R.id.toolbar);
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        toolbar2.setVisibility(View.GONE);
        // setSupportActionBar(toolbar);
        loadSettings();

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_QRcode.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_QRcode.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_QRcode.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_QRcode.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_QRcode.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_QRcode.this, Preference.class));
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
}
