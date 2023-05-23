package it.uniba.dib.sms22239.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import it.uniba.dib.sms22239.R;

public class Fragment_Testo extends Fragment
{
    private ImageButton btnInserisci;
    private ImageButton btnVisualizza;
    StorageReference reference;

    public Fragment_Testo() {
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
        return inflater.inflate(R.layout.fragment_testo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       btnInserisci = getView().findViewById(R.id.AggiungiTesto);
       btnVisualizza = getView().findViewById(R.id.VisualizzaTesti);
       String idAnimale = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
       reference = FirebaseStorage.getInstance().getReference("Animali").child(idAnimale).child("Testi");

       EditText editNome = getView().findViewById(R.id.NomeTesto);
       EditText editTesto = getView().findViewById(R.id.Testo);

        ImageButton backBtn2 = getView().findViewById(R.id.back);
        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

       btnInserisci.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String Nome = editNome.getText().toString();
               String Testo = editTesto.getText().toString();
               reference.child(Nome + ".txt").putBytes(Testo.getBytes()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Toast.makeText(getActivity(), "File Caricato con successo", Toast.LENGTH_SHORT).show();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                   }
               });
           }
       });

        btnVisualizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_VisualizzaTesto vistestoFragment = new Fragment_VisualizzaTesto();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, vistestoFragment)
                        .commit();
            }
        });
    }
}
