package it.uniba.dib.sms22239;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_profile_animale extends Fragment {
    private TextView mNomeTextView;
    private TextView mrazzaTextView;
    private TextView msessoTextView;

    private TextView midproprietarioTextView;

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

        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("Animale").child(idAnimal);

        midproprietarioTextView = getView().findViewById(R.id.animal_proprietario);
        mNomeTextView = getView().findViewById(R.id.animal_nome);
        mrazzaTextView =  getView().findViewById(R.id.animal_razza);
        msessoTextView =  getView().findViewById(R.id.animal_sesso);
        MaterialButton backBtn = getView().findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        // Recupera i dati dal database e popola le viste
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String proprietario = dataSnapshot.child("Id_utente").getValue(String.class);
                String name = dataSnapshot.child("nome").getValue(String.class);
                String razza = dataSnapshot.child("razza").getValue(String.class);
                String sesso = dataSnapshot.child("sesso").getValue(String.class);

                //set delle variabili recuperate al layout
                midproprietarioTextView.setText(proprietario);
                mNomeTextView.setText(name);
                mrazzaTextView.setText(razza);
                msessoTextView.setText(sesso);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getView().findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
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
                startActivity(new Intent(getActivity(), Activity_Bluetooth.class));
            }
        });

        ImageButton shareButton = view.findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recupera i dati dall'interfaccia utente
                String nome = mNomeTextView.getText().toString();
                String razza = mrazzaTextView.getText().toString();
                String sesso = msessoTextView.getText().toString();
                String proprietario = midproprietarioTextView.getText().toString();

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
                startActivity( new Intent(getActivity(), Spese.class));
            }
        });
    }
}