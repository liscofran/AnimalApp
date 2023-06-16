package it.uniba.dib.sms22239.Fragments;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import it.uniba.dib.sms22239.Activities.Activity_Main;
import it.uniba.dib.sms22239.Models.Proprietario;
import it.uniba.dib.sms22239.R;


public class Fragment_profile_proprietario extends Fragment
{


    private FirebaseAuth mAuth;
    private TextView mNomeTextView;
    private TextView mCognomeTextView;
    private TextView mcodfiscaleTextView;
    private CircleImageView profilo;

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

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Proprietari/" + user.getUid() + ".jpg");


        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("User").child(user.getUid());
        mNomeTextView = getView().findViewById(R.id.user_nome);
        mCognomeTextView =  getView().findViewById(R.id.user_cognome);
        mcodfiscaleTextView =  getView().findViewById(R.id.user_codicefiscale);
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

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    Proprietario prop = task.getResult().getValue(Proprietario.class);

                    String nome = getString(R.string.nome1);
                    String cognome = getString(R.string.cogn1);
                    String cf = getString(R.string.cf);

                    mNomeTextView.setText(nome +" : "+ prop.nome);
                    mCognomeTextView.setText(cognome +" : " + prop.cognome);
                    mcodfiscaleTextView.setText(cf + " : " + prop.codice_fiscale);
                }
            }
        });

        getView().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c1= getString(R.string.annulla);
                String c2= getString(R.string.elimina_profile);
                String c3= getString(R.string.conferma);
                String c4= getString(R.string.sss);
                String c5= getString(R.string.aa);
                new AlertDialog.Builder(getActivity())
                        .setTitle(c2)
                        .setMessage(c4)
                        .setPositiveButton(c3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase.removeValue();
                                user.delete();
                                Toast.makeText(getActivity(), c5, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Activity_Main.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(c1, null)
                        .show();
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
                    //codice vecchio , onresume pensato per aggiornare l'immagine di profilo appena clicchi su salva
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
//
//        // Verifica che la variabile profilo non sia nulla
//        if (profilo != null) {
//            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//            StorageReference imagesRef = storageRef.child("Proprietari/" + user.getUid() + ".jpg");
//
//            imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    String imageUrl = uri.toString();
//                    // Invalida la cache per l'immagine
//                    Picasso.get().invalidate(imageUrl);
//                    // Usa Picasso per caricare l'immagine nell'ImageView
//                    Picasso.get().load(imageUrl).into(profilo);
//                    Picasso.get().resumeTag(imagesRef);
//                }
//            });
//        }
//    }
}