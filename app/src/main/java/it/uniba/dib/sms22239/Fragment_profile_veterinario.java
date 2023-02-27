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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_profile_veterinario extends Fragment {
    RelativeLayout relativeLayout;
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;

    private Button editProfileButton;

    private FirebaseRecyclerOptions<Proprietario> options;
    private FirebaseRecyclerAdapter<Proprietario, MyViewHolder> adapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase mDatabase;
    private TextView mNomeTextView;
    private TextView mCognomeTextView;
    private TextView mcodfiscaleTextView;
    private TextView memailTextView;
    private TextView mtitolostudioTextView;


    public Fragment_profile_veterinario() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile_veterinario, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;


        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

        mNomeTextView = getView().findViewById(R.id.vet_nome);
        mCognomeTextView =  getView().findViewById(R.id.vet_cognome);
        mcodfiscaleTextView =  getView().findViewById(R.id.vet_codicefiscale);
        memailTextView =  getView().findViewById(R.id.vet_email);
        mtitolostudioTextView =  getView().findViewById(R.id.vet_titolostudio);

        // Recupera i dati dal database e popola le viste
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("nome").getValue(String.class);
                String cognome = dataSnapshot.child("cognome").getValue(String.class);
                String codfiscale = dataSnapshot.child("codice_fiscale").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String titoloStudio = dataSnapshot.child("titolo_studio").getValue(String.class);

                //set delle variabili recuperate al layout
                mNomeTextView.setText(name);
                mCognomeTextView.setText(cognome);
                mcodfiscaleTextView.setText(codfiscale);
                memailTextView.setText(email);
                mtitolostudioTextView.setText(titoloStudio);
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
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_edit_veterinario());
                fragmentTransaction.commit();
            }
        });
    }
}