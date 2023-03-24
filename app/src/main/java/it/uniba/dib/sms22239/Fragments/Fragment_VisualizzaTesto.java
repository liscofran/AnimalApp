package it.uniba.dib.sms22239.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms22239.FileListAdapter;
import it.uniba.dib.sms22239.R;

public class Fragment_VisualizzaTesto extends Fragment implements FileListAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private FileListAdapter fileListAdapter;

    public Fragment_VisualizzaTesto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visualizza_testo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference txtRef = storageRef.child("/Document");

        txtRef.listAll()
                .addOnSuccessListener(listResult -> {
                    List<String> fileNames = new ArrayList<>();
                    for (StorageReference fileRef : listResult.getItems()) {
                        if (fileRef.getName().endsWith(".txt")) {
                            fileNames.add(fileRef.getName());
                        }
                    }
                    fileListAdapter = new FileListAdapter(fileNames, this);
                    recyclerView.setAdapter(fileListAdapter);
                })
                .addOnFailureListener(e -> Log.e("TAG", "Errore durante la ricerca dei file", e));
    }

    @Override
    public void onItemClick(String fileName) {
        // Azione da eseguire quando viene cliccato un elemento della lista
        // Ad esempio, aprire il file corrispondente
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child("Document/" + fileName);

        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "text/plain");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Apri il file con..."));
        }).addOnFailureListener(e -> Log.e("TAG", "Errore durante il download del file", e));
    }
}

