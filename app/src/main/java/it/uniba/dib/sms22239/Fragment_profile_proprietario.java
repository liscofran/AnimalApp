package it.uniba.dib.sms22239;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_profile_proprietario extends Fragment
{
    private FirebaseAuth mAuth;
    private TextView mNomeTextView;
    private TextView mCognomeTextView;
    private TextView mcodfiscaleTextView;

    public Fragment_profile_proprietario() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profile_proprietario, container, false);
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

        mNomeTextView = getView().findViewById(R.id.user_nome);
        mCognomeTextView =  getView().findViewById(R.id.user_cognome);
        mcodfiscaleTextView =  getView().findViewById(R.id.user_codicefiscale);

        Button backBtn = getView().findViewById(R.id.back);

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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String name = dataSnapshot.child("nome").getValue(String.class);
                String cognome = dataSnapshot.child("cognome").getValue(String.class);
                String codfiscale = dataSnapshot.child("codice_fiscale").getValue(String.class);

                //set delle variabili recuperate al layout
                mNomeTextView.setText(name);
                mCognomeTextView.setText(cognome);
                mcodfiscaleTextView.setText(codfiscale);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

            getView().findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_edit_profile_proprietario());
                fragmentTransaction.commit();
            }
        });
    }
}