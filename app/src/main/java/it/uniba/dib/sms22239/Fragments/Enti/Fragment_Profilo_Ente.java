package it.uniba.dib.sms22239.Fragments.Enti;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import it.uniba.dib.sms22239.Activities.Activity_Main;
import it.uniba.dib.sms22239.Models.Ente;
import it.uniba.dib.sms22239.R;


public class Fragment_Profilo_Ente extends Fragment
{
    private FirebaseAuth mAuth;
    private TextView mtipoTextView;
    private TextView msedelegaleTextView;
    private TextView mpiva;
    private TextView mragionesociale;
    private ImageView profilo;

    public Fragment_Profilo_Ente() {
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
        return inflater.inflate(R.layout.fragment_profile_ente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Enti/" + user.getUid() + ".jpg");

        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

        mragionesociale = getView().findViewById(R.id.ente_ragione_sociale);
        mtipoTextView =  getView().findViewById(R.id.ente_tipo);
        msedelegaleTextView =  getView().findViewById(R.id.ente_sede_legale);
        mpiva = getView().findViewById(R.id.ente_piva);
        profilo = getView().findViewById(R.id.profile_image);

        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                // Usa Picasso per caricare l'immagine nell'ImageView
                Picasso.get().load(imageUrl).into(profilo);
                profilo = getView().findViewById(R.id.profile_image);
            }
        });

        getView().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c1= getString(R.string.elimina_profile);
                String c2= getString(R.string.sss);
                String c3= getString(R.string.conferma);
                String c5= getString(R.string.annulla);

                new AlertDialog.Builder(getActivity())

                        .setTitle(c1)
                        .setMessage(c2)
                        .setPositiveButton(c3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase.removeValue();
                                user.delete();
                                String c4= getString(R.string.ee);

                                Toast.makeText(getActivity(), c4, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Activity_Main.class);
                                startActivity(intent);
                            }
                        })

                        .setNegativeButton(c5, null)
                        .show();
            }
        });

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    Ente ente = task.getResult().getValue(Ente.class);

                    String c6= getString(R.string.rs);
                    String c7= getString(R.string.tp);
                    String c8= getString(R.string.sls);
                    String c9= getString(R.string.piv);

                    mragionesociale.setText(c6 + ": " + ente.ragione_sociale);
                    mtipoTextView.setText(c7 + ": " + ente.tipo);
                    msedelegaleTextView.setText(c8 + ": " + ente.sede_legale);
                    mpiva.setText(c9 + ": " + ente.p_iva);
                }
            }
        });

        getView().findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_Edit_Profilo_Ente());
                fragmentTransaction.commit();
            }
        });

    }
}