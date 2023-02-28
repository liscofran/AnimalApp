package it.uniba.dib.sms22239;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothActivity";
    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private InputStream mmInputStream;
    private OutputStream mmOutputStream;
    private volatile boolean stopWorker;
    private Button mButton;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        mTextView = findViewById(R.id.text_view);
        mButton = findViewById(R.id.button);


    }
    @Override
    protected void onStart() {
        // Verifica se il dispositivo Android supporta il Bluetooth
        super.onStart();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Il dispositivo non supporta il Bluetooth", Toast.LENGTH_SHORT).show();


        }

        // Abilita il Bluetooth sul dispositivo Android se non è già abilitato
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Ricerca dei dispositivi Bluetooth accoppiati
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                // Seleziona il dispositivo Bluetooth desiderato
                if (device.getName().equals("NomeDispositivoBluetooth")) {
                    mmDevice = device;

                }
            }
        }

        // Crea un socket Bluetooth per la connessione
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmInputStream = mmSocket.getInputStream();
            mmOutputStream = mmSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Errore durante la connessione al dispositivo Bluetooth", e);
            Toast.makeText(this, "Errore durante la connessione al dispositivo Bluetooth", Toast.LENGTH_SHORT).show();

        }

        // Imposta il listener per il pulsante di invio
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Invia dati al dispositivo Bluetooth
                String message = "Hello, world!";
                try {
                    mmOutputStream.write(message.getBytes());
                } catch (IOException e) {
                    Log.e(TAG, "Errore durante l'invio dei dati al dispositivo Bluetooth", e);
                    Toast.makeText(BluetoothActivity.this, "Errore durante l'invio dei dati al dispositivo Bluetooth", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Avvia un thread per ricevere dati dal dispositivo Bluetooth
        final Handler handler = new Handler();
        stopWorker = false;
        final byte delimiter = 10; // Carattere newline (\n)
        Thread workerThread = new Thread(new Runnable() {
            public void run() {
                byte[] buffer = new byte[1024];
                int bytes;

                while (!stopWorker) {
                    try {
                        bytes = mmInputStream.read(buffer);
                        final String message = new String(buffer, 0, bytes);

                        handler.post(new Runnable() {
                            public void run() {
                                // Aggiorna l'interfaccia utente con il messaggio ricevuto
                                mTextView.setText(message);
                            }
                        });
                    } catch (IOException e) {
                        Log.e(TAG, "Errore durante la ricezione dei dati dal dispositivo Bluetooth", e);
                        Toast.makeText(BluetoothActivity.this, "Errore durante la ricezione dei dati dal dispositivo Bluetooth", Toast.LENGTH_SHORT).show();
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Chiude la connessione Bluetooth
        stopWorker = true;
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Errore durante la chiusura della connessione Bluetooth", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                // Il Bluetooth è stato attivato
            } else {
                // L'utente ha rifiutato l'attivazione del Bluetooth
                Toast.makeText(this, "Il Bluetooth è disabilitato, l'applicazione verrà chiusa", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
