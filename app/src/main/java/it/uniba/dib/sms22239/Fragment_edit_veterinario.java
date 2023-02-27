package it.uniba.dib.sms22239;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_edit_veterinario extends Fragment {
    RelativeLayout relativeLayout;
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;

    private Button editProfileButton;

    private FirebaseRecyclerOptions<Proprietario> options;
    private FirebaseRecyclerAdapter<Proprietario, MyViewHolder> adapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase mDatabase;
    private EditText mNomeTextView;
    private EditText mCognomeTextView;
    private EditText mtitolostudioTextView;


    public Fragment_edit_veterinario() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_edit_veterinario, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;

        mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

        // Collega i componenti dell'interfaccia con le variabili
        EditText editName = getView().findViewById(R.id.vet_nome);
        EditText editCognome = getView().findViewById(R.id.vet_cognome);
        EditText editTitoloStudio = getView().findViewById(R.id.vet_titolostudio);


        Button saveProfileButton = getView().findViewById(R.id.save_profile_button);

        mNomeTextView = getView().findViewById(R.id.vet_nome);
        mCognomeTextView = getView().findViewById(R.id.vet_cognome);
        mtitolostudioTextView = getView().findViewById(R.id.vet_titolostudio);

        // Recupera i dati dal database e popola i campi
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //recupero dati e assegnazione alle variabili
                String name = dataSnapshot.child("nome").getValue(String.class);
                String cognome = dataSnapshot.child("cognome").getValue(String.class);
                String titolostudio = dataSnapshot.child("titolo_studio").getValue(String.class);

                //set delle variabili recuperate al layout
                mNomeTextView.setText(name);
                mCognomeTextView.setText(cognome);
                mtitolostudioTextView.setText(titolostudio);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Imposta un listener di clic sul pulsante di salvataggio del profilo
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Salva i dati del profilo e torna all'activity precedente
                DatabaseReference mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

                //prende i dati inseriti in input e gli assegna alle variabili temporanee
                String newName = editName.getText().toString();
                String newCognome = editCognome.getText().toString();
                String newTitoloStudio = editTitoloStudio.getText().toString();

                //modifica e salva i dati anche sul database
                mDatabase.child("cognome").setValue(newCognome);
                mDatabase.child("nome").setValue(newName);
                mDatabase.child("titolo_studio").setValue(newTitoloStudio);


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_profile_veterinario());
                fragmentTransaction.commit();

            }
        });
    }}