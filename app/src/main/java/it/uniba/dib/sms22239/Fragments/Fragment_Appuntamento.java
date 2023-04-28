package it.uniba.dib.sms22239.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Activities.Activity_Calendario_Veterinario;
import it.uniba.dib.sms22239.R;



public class Fragment_Appuntamento extends Fragment {

    private TextView DataTextView;
    private TextView OraInizioTextView;
    private TextView OraFineTextView;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appuntamento_veterinario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String idAppuntamento = requireActivity().getIntent().getStringExtra("id_appuntamento");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Appuntamenti").child(idAppuntamento);

        DataTextView = getView().findViewById(R.id.date);
        OraInizioTextView = getView().findViewById(R.id.time_start);
        OraFineTextView = getView().findViewById(R.id.time_end);


        // Recupera i dati dal database e popola le viste
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String data = dataSnapshot.child("data").getValue(String.class);
                String orario_inizio = dataSnapshot.child("orario_inizio").getValue(String.class);
                String orario_fine = dataSnapshot.child("orario_fine").getValue(String.class);

                //set delle variabili recuperate al layout
                DataTextView .setText("Data Appuntamento: " + data);
                OraInizioTextView.setText("Orario Inizio:  " + orario_inizio);
                OraFineTextView.setText("Orario Fine: " + orario_fine);

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
                fragmentTransaction.replace(R.id.frame, new Fragment_edit_appuntamento_veterinario());
                fragmentTransaction.commit();
            }
        });

        getView().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase1.removeValue(); // rimuovi la tupla dal database Firebase
                Toast.makeText(getActivity(), "Appuntamento eliminato con successo!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Activity_Calendario_Veterinario.class);
                startActivity(intent);
            }
        });

    }
}
