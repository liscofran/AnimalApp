package it.uniba.dib.sms22239.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.bluetooth.BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import it.uniba.dib.sms22239.Activities.Activity_Calendario_Appuntamenti_Animale;
import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Activities.Activity_Multimedia;
import it.uniba.dib.sms22239.Activities.Activity_PrenotazioniAppuntamenti_Utente;
import it.uniba.dib.sms22239.Activities.Activity_QRGenerate;
import it.uniba.dib.sms22239.Activities.Activity_Spese;
import it.uniba.dib.sms22239.Models.Animale;
import it.uniba.dib.sms22239.Models.Ente;
import it.uniba.dib.sms22239.Models.Proprietario;
import it.uniba.dib.sms22239.R;

public class Fragment_profile_animale extends Fragment {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_DISCOVERABLE = 3;
    private static final int REQUEST_PERMISSION_BLUETOOTH = 2;
    private static final int DISCOVERABLE_DURATION = 300;
    private TextView mNomeTextView, mrazzaTextView, msessoTextView, nomecognomeprop, statusTextView, casaluogoTextView, relazioneTextView;
    private String idUtente, idAnimal, relazione;
    private BluetoothDevice selectedBluetoothDevice;
    String relazioneconidAnimale, nomeAnimaleRelazione;
    private String idRelazione;
    private ImageView profilo;
    public CircleImageView qrbutton, appre, shareButton;
    protected ImageButton backBtn;
    DatabaseReference mDatabase2;
    public Fragment_profile_animale() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_animal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        idAnimal = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");

        // Recupera il riferimento al database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimal);
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("User");


        //Recupera il riferimento allo Storage
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference("Animali").child(idAnimal).child("ImmagineProfilo.jpg");

        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                // Usa Picasso per caricare l'immagine nell'ImageView
                Picasso.get().load(imageUrl).into(profilo);
                profilo = getView().findViewById(R.id.profile_image);
            }
        });

        mNomeTextView = getView().findViewById(R.id.animal_nome);
        mrazzaTextView = getView().findViewById(R.id.razza);
        msessoTextView = getView().findViewById(R.id.sesso);
        backBtn = getView().findViewById(R.id.back);
        nomecognomeprop = getView().findViewById(R.id.nom_cogn_prop);
        statusTextView = getView().findViewById(R.id.status);
        casaluogoTextView = getView().findViewById(R.id.luogo);
        relazioneTextView = getView().findViewById(R.id.relazione);


        profilo = getView().findViewById(R.id.profile_image);
        qrbutton = getView().findViewById(R.id.qr_button);
        appre = getView().findViewById(R.id.appren_button);

        getView().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c1= getString(R.string.ea1);
                String c2= getString(R.string.ea2);
                String c3= getString(R.string.conferma);
                String c4= getString(R.string.annulla);
                String c5= getString(R.string.ea3);
                new AlertDialog.Builder(getActivity())


                        .setTitle(c1)
                        .setMessage(c2)
                        .setPositiveButton(c3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase.removeValue();
                                Toast.makeText(getActivity(), c5, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Activity_Home.class);
                                startActivity(intent);
                            }
                        })


                        .setNegativeButton(c4, null)
                        .show();
            }
        });

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    Animale ani = task.getResult().getValue(Animale.class);
                    idRelazione = ani.idAnimaleRelazione;
                    DatabaseReference mDatabase3 = FirebaseDatabase.getInstance().getReference().child("Animale").child(idRelazione);

                    if (!Objects.equals(ani.relazione, "Nessuna")) {

                        mDatabase3.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()) {
                                    Animale ani2 = task.getResult().getValue(Animale.class);
                                    //nomeAnimaleRelazione = dataSnapshot.child("nome").getValue(String.class);
                                    String c6= getString(R.string.rela);
                                    String c7= getString(R.string.con);
                                    relazioneTextView.setText(c6+":"+ ani.relazione + " " + c7 + " " + ani2.nome);
                                }
                            }
                        });

                    } else {
                        String c8= getString(R.string.nr);

                        relazioneTextView.setText(c8);
                    }
                    mDatabase1.child(ani.Id_utente).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()) {
                                Proprietario prop = task.getResult().getValue(Proprietario.class);
                                if (prop.classe.equals("Proprietario"))
                                {
                                //set delle variabili recuperate al layout
                                String c12= getString(R.string.proprietario);
                                nomecognomeprop.setText(c12+":" + prop.nome + " " + prop.cognome);
                                }
                                else {
                                    Ente ent = task.getResult().getValue(Ente.class);
                                    //set delle variabili recuperate al layout
                                    String c12= getString(R.string.proprietario);
                                    nomecognomeprop.setText(c12+":" + ent.ragione_sociale);
                                }
                            }
                        }
                    });

                //set delle variabili recuperate al layout
                String c9= getString(R.string.nome1);
                String c10= getString(R.string.razza);
                String c11= getString(R.string.sesso);
                String c13= getString(R.string.luogo);

                mNomeTextView.setText(c9+":" + ani.nome);
                mrazzaTextView.setText(c10+":"+ ani.razza);
                msessoTextView.setText(c11+":" + ani.sesso);
                statusTextView.setText("status:" + ani.prop);
                statusTextView.setText("status:" + ani.prop);
                casaluogoTextView.setText(c13+":" + ani.luogo);
                }
            }
        });

        //Bottoni dell'animale
        appre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_PrenotazioniAppuntamenti_Utente.class);
                intent.putExtra("ANIMAL_CODE", idAnimal);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        qrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_QRGenerate.class);
                intent.putExtra("ANIMAL_CODE", idAnimal);
                startActivity(intent);
            }
        });

        getView().findViewById(R.id.modifica).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_edit_animal_profile());
                fragmentTransaction.commit();
            }
        });

        getView().findViewById(R.id.salute_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_salute_animale());
                fragmentTransaction.commit();
            }
        });

        getView().findViewById(R.id.voidspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        shareButton = view.findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recupera i dati dall'interfaccia utente
                String nome = mNomeTextView.getText().toString();
                String razza = mrazzaTextView.getText().toString();
                String sesso = msessoTextView.getText().toString();
                String proprietario = nomecognomeprop.getText().toString();

                // Costruisci la stringa di testo per la condivisione
                String shareText =
                        nome + "\n " +
                                razza + "\n " +
                                sesso + "\n " +
                                proprietario;

                // Crea l'intent per la condivisione
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        getView().findViewById(R.id.spese_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Spese.class);
                intent.putExtra("ANIMAL_CODE", idAnimal);
                startActivity(intent);
            }
        });

        getView().findViewById(R.id.multimedia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Multimedia.class);
                intent.putExtra("ANIMAL_CODE", idAnimal);
                startActivity(intent);
            }
        });

        getView().findViewById(R.id.bluetooth_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null) {
                    String c14= getString(R.string.bt22);
                    Toast.makeText(getActivity(), c14, Toast.LENGTH_SHORT).show();
                    return;
                }
                checkBluetoothPermissions();
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    // Avvia la ricerca dei dispositivi Bluetooth
                    startBluetoothDiscovery();
                }
            }
        });

    }

    private void startBluetoothDiscovery() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            String c14= getString(R.string.bt22);
            Toast.makeText(getActivity(), c14, Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica se il Bluetooth è abilitato
        if (!bluetoothAdapter.isEnabled()) {
            String c15= getString(R.string.bt21);

            Toast.makeText(getActivity(), c15, Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica se la modalità di scoperta è già attiva
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH}, REQUEST_PERMISSION_BLUETOOTH);
            return;
        }
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            // Avvia la modalità di scoperta
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
            startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);
        } else {
            // La modalità di scoperta è già attiva, avvia la ricerca dei dispositivi
            startDeviceDiscovery();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth abilitato, avvia la ricerca dei dispositivi
                startBluetoothDiscovery();
            } else {
                String c16= getString(R.string.bt23);

                // Bluetooth non abilitato, mostra un messaggio o gestisci di conseguenza
                Toast.makeText(getActivity(), c16, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_DISCOVERABLE) {
            if (resultCode == DISCOVERABLE_DURATION) {
                // La modalità di scoperta è stata attivata con successo, avvia la ricerca dei dispositivi
                startDeviceDiscovery();
            } else {
                String c17= getString(R.string.bt24);

                // Modalità di scoperta non attivata, mostra un messaggio o gestisci di conseguenza
                Toast.makeText(getActivity(), c17, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void startDeviceDiscovery() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            String c17= getString(R.string.bt22);

            Toast.makeText(getActivity(), c17, Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica se il Bluetooth è abilitato
        if (!bluetoothAdapter.isEnabled()) {
            String c17= getString(R.string.bt21);

            Toast.makeText(getActivity(), c17, Toast.LENGTH_SHORT).show();
            return;
        }

        // Avvia la ricerca dei dispositivi
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH}, REQUEST_PERMISSION_BLUETOOTH);
            return;
        }
        bluetoothAdapter.startDiscovery();

        // Registra un BroadcastReceiver per ricevere le informazioni sui dispositivi trovati
        BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Un dispositivo è stato trovato
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Puoi ottenere ulteriori informazioni sul dispositivo come il nome e l'indirizzo MAC
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH}, REQUEST_PERMISSION_BLUETOOTH);
                        return;
                    }
                    String deviceName = device.getName();
                    String deviceAddress = device.getAddress();

                    // Fai qualcosa con le informazioni del dispositivo trovato
                    // ...

                    // Invia un intent con la stringa 'animalId'
                    Intent animalIdIntent = new Intent("collegamento");
                    animalIdIntent.putExtra("ANIMAL_CODE", idAnimal);
                    getActivity().startActivity(animalIdIntent);

                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    // La ricerca dei dispositivi è terminata
                    // Puoi eseguire ulteriori azioni dopo la fine della ricerca
                    // ...

                    // Assicurati di unregister il BroadcastReceiver dopo aver completato le operazioni
                    getActivity().unregisterReceiver(this);
                }
            }
        };

        // Registra il BroadcastReceiver per le azioni ACTION_FOUND e ACTION_DISCOVERY_FINISHED
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(discoveryReceiver, filter);
    }

    private void checkBluetoothPermissions() {
        // Check for Bluetooth permissions
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH}, REQUEST_PERMISSION_BLUETOOTH);
        } else {
            // Permission already granted, proceed with Bluetooth usage
            String c17= getString(R.string.bt25);

            Toast.makeText(getActivity(), c17, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_BLUETOOTH) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with Bluetooth usage
                Toast.makeText(getActivity(), "Bluetooth permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, show message or handle accordingly
                Toast.makeText(getActivity(), "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}