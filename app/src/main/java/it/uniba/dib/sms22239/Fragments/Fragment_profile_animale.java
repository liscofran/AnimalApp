package it.uniba.dib.sms22239.Fragments;

import static android.app.Activity.RESULT_OK;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import it.uniba.dib.sms22239.Activities.Activity_Multimedia;
import it.uniba.dib.sms22239.Activities.Activity_QRGenerate;
import it.uniba.dib.sms22239.Activities.Activity_Spese;
import it.uniba.dib.sms22239.R;

public class Fragment_profile_animale extends Fragment {
    private TextView mNomeTextView;
    private TextView mrazzaTextView;
    private TextView msessoTextView;
    private String idUtente;
    private TextView midproprietarioTextView;
    private TextView nomecognomeprop;
    private TextView statusTextView;
    private TextView casaluogoTextView;
    private ImageView profilo;
    public CircleImageView qrbutton;
    public CircleImageView appre;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PERMISSION_BLUETOOTH = 2;


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

        String idAnimal = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;
        DatabaseReference mDatabase1;

        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("Animale").child(idAnimal);
        mDatabase1 = database.getInstance().getReference().child("User");

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Animali/" + idAnimal + ".jpg");

        mNomeTextView = getView().findViewById(R.id.animal_nome);
        mrazzaTextView =  getView().findViewById(R.id.razza);
        msessoTextView =  getView().findViewById(R.id.sesso);
        ImageButton backBtn = getView().findViewById(R.id.back);
        nomecognomeprop = getView().findViewById(R.id.nom_cogn_prop);
        statusTextView = getView().findViewById(R.id.status);
        casaluogoTextView  = getView().findViewById(R.id.luogo);
        profilo = getView().findViewById(R.id.profile_image);
        qrbutton = getView().findViewById(R.id.qr_button);
        appre = getView().findViewById(R.id.appren_button);

        appre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Calendario_Animale.class);
            }
        });
        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                // Usa Picasso per caricare l'immagine nell'ImageView
                Picasso.get().load(imageUrl).into(profilo);
                profilo = getView().findViewById(R.id.profile_image);
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
        // Recupera i dati dal database 2 e popola le viste
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


        getView().findViewById(R.id.modifica).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_edit_animal_profile());
                fragmentTransaction.commit();
            }
        });

        getView().findViewById(R.id.bluetooth_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null) {
                    Toast.makeText(getActivity(), "Bluetooth not supported on this device", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    // Bluetooth is already enabled, check for Bluetooth permissions
                    checkBluetoothPermissions();
                }
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

        CircleImageView shareButton = view.findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recupera i dati dall'interfaccia utente
                String nome = mNomeTextView.getText().toString();
                String razza = mrazzaTextView.getText().toString();
                String sesso = msessoTextView.getText().toString();
                String proprietario = nomecognomeprop.getText().toString();

                // Costruisci la stringa di testo per la condivisione
                String shareText = "Nome: " + nome + "\n" +
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

        getView().findViewById(R.id.spese_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Spese.class);
                intent.putExtra("ANIMAL_CODE",idAnimal);
                startActivity(intent);            }
        });

        getView().findViewById(R.id.multimedia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Multimedia.class);
                startActivity(intent);            }
        });
    }
    private void checkBluetoothPermissions() {
        // Check for Bluetooth permissions
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.BLUETOOTH") != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.BLUETOOTH"}, REQUEST_PERMISSION_BLUETOOTH);
        } else {
            // Permission already granted, proceed with Bluetooth usage
            Toast.makeText(getActivity(), "Bluetooth ready to use", Toast.LENGTH_SHORT).show();
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