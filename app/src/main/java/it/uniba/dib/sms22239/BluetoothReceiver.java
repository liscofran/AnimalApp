package it.uniba.dib.sms22239;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            if (state == BluetoothAdapter.STATE_ON) {
                // Bluetooth è stato abilitato, puoi gestire l'azione successiva qui
            }
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
            // Dispositivo Bluetooth trovato, puoi gestire l'azione successiva qui
        } else if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
            // La scansione dei dispositivi Bluetooth è stata completata, puoi gestire l'azione successiva qui
        } else if (intent.getAction().equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
            int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
            if (mode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                // Il dispositivo è stato impostato come visibile per la scansione Bluetooth, puoi gestire l'azione successiva qui
            }
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
            // Dispositivo Bluetooth connesso, puoi gestire l'azione successiva qui
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
            // Dispositivo Bluetooth disconnesso, puoi gestire l'azione successiva qui
        } else if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
            // Avvio della scansione dei dispositivi Bluetooth, puoi gestire l'azione successiva qui
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_UUID)) {
            // UUID del dispositivo Bluetooth ottenuto, puoi gestire l'azione successiva qui
        } else if (intent.getAction().equals(BluetoothAdapter.ACTION_REQUEST_ENABLE)) {
            // L'utente ha risposto alla richiesta di abilitare il Bluetooth, puoi gestire l'azione successiva qui
        } else if (intent.getAction().equals(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)) {
            // L'utente ha risposto alla richiesta di rendere il dispositivo visibile per la scansione Bluetooth, puoi gestire l'azione successiva qui
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            // Stato di associazione del dispositivo Bluetooth cambiato, puoi gestire l'azione successiva qui
        } else if (intent.getAction().equals(Intent.ACTION_SEND)) {
            // Intent ricevuto tramite Bluetooth, puoi gestire l'azione successiva qui
            String idAnimal = intent.getStringExtra("ANIMAL_CODE");
            if (idAnimal != null) {
                // L'id dell'animale è stato ricevuto con successo, puoi utilizzarlo come desideri
            }
        }
    }

}
