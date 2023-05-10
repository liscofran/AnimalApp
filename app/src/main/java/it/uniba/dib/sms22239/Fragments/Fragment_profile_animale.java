package it.uniba.dib.sms22239.Fragments;

import static android.app.Activity.RESULT_OK;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import it.uniba.dib.sms22239.Activities.Activity_Calendario_Animale;
import it.uniba.dib.sms22239.Activities.Activity_Calendario_Veterinario;
import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Activities.Activity_Main;
import it.uniba.dib.sms22239.Activities.Activity_Multimedia;
import it.uniba.dib.sms22239.Activities.Activity_QRGenerate;
import it.uniba.dib.sms22239.Activities.Activity_Spese;
import it.uniba.dib.sms22239.R;

public class Fragment_profile_animale extends Fragment
{
    private TextView mNomeTextView, mrazzaTextView, msessoTextView, nomecognomeprop, statusTextView, casaluogoTextView,relazioneTextView;
    private String idUtente,idAnimal,relazione;
    String relazioneconidAnimale, nomeAnimaleRelazione;
    private ImageView profilo;
    public CircleImageView qrbutton, appre, shareButton;
    private static final int REQUEST_ENABLE_BT = 1, REQUEST_PERMISSION_BLUETOOTH = 2;
    protected ImageButton backBtn;
    DatabaseReference mDatabase2;

    public Fragment_profile_animale() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profile_animal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
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
        mrazzaTextView =  getView().findViewById(R.id.razza);
        msessoTextView =  getView().findViewById(R.id.sesso);
        backBtn = getView().findViewById(R.id.back);
        nomecognomeprop = getView().findViewById(R.id.nom_cogn_prop);
        statusTextView = getView().findViewById(R.id.status);
        casaluogoTextView  = getView().findViewById(R.id.luogo);
        relazioneTextView  = getView().findViewById(R.id.relazione);


        profilo = getView().findViewById(R.id.profile_image);
        qrbutton = getView().findViewById(R.id.qr_button);
        appre = getView().findViewById(R.id.appren_button);

        getView().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Elimina animale")
                        .setMessage("Sei sicuro di voler eliminare l'animale?")
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase.removeValue();
                                Toast.makeText(getActivity(), "Animale eliminato con successo!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Activity_Home.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Annulla", null)
                        .show();
            }
        });

        // Recupera i dati dal database e popola le viste
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("nome").getValue(String.class);
                String razza = dataSnapshot.child("razza").getValue(String.class);
                String sesso = dataSnapshot.child("sesso").getValue(String.class);
                idUtente = dataSnapshot.child("Id_utente").getValue(String.class);
                String status = dataSnapshot.child("prop").getValue(String.class);
                String luogo = dataSnapshot.child("luogo").getValue(String.class);
                relazione = dataSnapshot.child("relazione").getValue(String.class);
                relazioneconidAnimale = dataSnapshot.child("idAnimalerelazione").getValue(String.class);


                mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Animale").child(relazioneconidAnimale);

                mDatabase2.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        nomeAnimaleRelazione = dataSnapshot.child("nome").getValue(String.class);
                        relazioneTextView.setText("Relazione: " + relazione + " con " + nomeAnimaleRelazione);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                //set delle variabili recuperate al layout

                mNomeTextView.setText("Nome: " + name);
                mrazzaTextView.setText("Razza: " + razza);
                msessoTextView.setText("Sesso: " + sesso);
                statusTextView.setText("Status: " + status);
                casaluogoTextView.setText("Luogo: " + luogo);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Recupera i dati dal secondo riferimento al database e popola le viste
        mDatabase1.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nome = dataSnapshot.child(idUtente).child("nome").getValue(String.class);
                String cognome = dataSnapshot.child(idUtente).child("cognome").getValue(String.class);
                //set delle variabili recuperate al layout

                nomecognomeprop.setText("Proprietario: " + nome + " " + cognome);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Bottoni dell'animale
        appre.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Calendario_Animale.class);
                intent.putExtra("ANIMAL_CODE", idAnimal);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        qrbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), Activity_QRGenerate.class);
                intent.putExtra("ANIMAL_CODE",idAnimal);
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

        getView().findViewById(R.id.bluetooth_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null) {
                    Toast.makeText(getActivity(), "Bluetooth non supportato dal dispositivo in uso", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkBluetoothPermissions();
                if (!bluetoothAdapter.isEnabled())
                {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } if(bluetoothAdapter.isEnabled()){
                    // Crea l'intent da condividere
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra( "ANIMAL_CODE",idAnimal);


                    // Crea l'intent chooser per scegliere l'app Bluetooth
                    Intent chooserIntent = Intent.createChooser(shareIntent, "Condividi con...");
                    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Avvia l'activity chooser
                    startActivity(chooserIntent);
                }

            }
        });

        getView().findViewById(R.id.salute_button).setOnClickListener(new View.OnClickListener()
        {
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
                        "Nome: " + nome + "\n" +
                        "Razza: " + razza + "\n" +
                        "Sesso: " + sesso + "\n" +
                        "Proprietario: " + proprietario;

                // Crea l'intent per la condivisione
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        getView().findViewById(R.id.spese_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Spese.class);
                intent.putExtra("ANIMAL_CODE",idAnimal);
                startActivity(intent);            }
        });

        getView().findViewById(R.id.multimedia).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Multimedia.class);
                intent.putExtra("ANIMAL_CODE",idAnimal);
                startActivity(intent);            }
        });
    }
    private void checkBluetoothPermissions()
    {
        // Check for Bluetooth permissions
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.BLUETOOTH") != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.BLUETOOTH"}, REQUEST_PERMISSION_BLUETOOTH);
        } else {
            // Permission already granted, proceed with Bluetooth usage
            Toast.makeText(getActivity(), "Bluetooth pronto all'uso", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                // Bluetooth enabled, check for Bluetooth permissions
                checkBluetoothPermissions();
            } else {
                // Bluetooth not enabled, show message or handle accordingly
                Toast.makeText(getActivity(), "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}