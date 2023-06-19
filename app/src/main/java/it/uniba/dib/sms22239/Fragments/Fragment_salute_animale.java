package it.uniba.dib.sms22239.Fragments;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Activities.Activity_Animali;
import it.uniba.dib.sms22239.Models.Animale;
import it.uniba.dib.sms22239.R;

public class Fragment_salute_animale extends Fragment
{
    private EditText mPatologieEditText;
    private EditText mPrefciboEditText;
    private ImageButton saveProfileButton, backBtn;
    private String idAnimal;

    public Fragment_salute_animale()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_salute_animale, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        idAnimal = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimal);

        saveProfileButton = getView().findViewById(R.id.salva);
        mPatologieEditText = getView().findViewById(R.id.patologie);
        mPrefciboEditText = getView().findViewById(R.id.preferenze_cibo);
        backBtn = getView().findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_profile_animale fragment = new Fragment_profile_animale();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    Animale ani = task.getResult().getValue(Animale.class);

                    String patologie = getString(R.string.patologie);
                    String preferenzecibo = getString(R.string.Preferenze_cibo);

                mPatologieEditText.setText(ani.patologie);
                mPrefciboEditText.setText(ani.preferenzecibo);
                }
            }
        });

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Salva i dati del profilo e torna all'activity precedente
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimal);

                //prende i dati inseriti in input e gli assegna alle variabili temporanee
                String newPatologie = mPatologieEditText.getText().toString();
                String newPrefcibo = mPrefciboEditText.getText().toString();

                //modifica e salva i dati anche sul database
                mDatabase.child("patologie").setValue(newPatologie);
                mDatabase.child("preferenzecibo").setValue(newPrefcibo);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_profile_animale());
                fragmentTransaction.commit();
            }
        });
    }
}