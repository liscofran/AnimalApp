package it.uniba.dib.sms22239.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms22239.Models.Testo;
import it.uniba.dib.sms22239.R;

public class Fragment_VisualizzaTesto extends Fragment {

    private StorageReference txtRef;
    private RecyclerView recyclerView;
    private static List<Testo> mFileList;
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

        txtRef = FirebaseStorage.getInstance().getReference().child("/Document");
        recyclerView = view.findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFileList = new ArrayList<>();
        fileListAdapter = new FileListAdapter(mFileList);
        recyclerView.setAdapter(fileListAdapter);

        txtRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference storageRef : listResult.getItems()) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Testo testo = new Testo();
                            testo.setUrl(uri.toString());
                            testo.setNome(storageRef.getName());
                            mFileList.add(testo);
                            fileListAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            }
        });
//                    List<String> fileNames = new ArrayList<>();
//                    for (StorageReference fileRef : listResult.getItems()) {
//                        if (fileRef.getName().endsWith(".txt")) {
//                            fileNames.add(fileRef.getName());
//                        }
//                    }
//                    fileListAdapter = new FileListAdapter(fileNames);
//                    recyclerView.setAdapter(fileListAdapter);
//                })
//                .addOnFailureListener(e -> Log.e("TAG", "Errore durante la ricerca dei file", e));
    }

    public class FileListAdapter extends RecyclerView.Adapter<FileViewHolder> {

        private List<Testo> fileList;

        public FileListAdapter(List<Testo> fileList) {
            this.fileList = fileList;
        }

        public void removeFile(Testo testo) {
            fileList.remove(testo);
        }

        @NonNull
        @Override
        public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.testo_item, parent, false);
            return new FileViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
            Testo testo = fileList.get(position);
            holder.bind(testo);
        }

        @Override
        public int getItemCount() {
            return fileList.size();
        }
    }

    public class FileViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView fileNameTextView;
        private final ImageView downloadImageView;
        private final ImageView deleteImageView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);

            fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
            downloadImageView = itemView.findViewById(R.id.downloadImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
        }

        public void bind(Testo testo) {
            fileNameTextView.setText(testo.getNome());

            downloadImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadFile(testo.getNome());
                }
            });

            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    deleteFile(testo);
                }
            });
        }
    }

//    public void onItemClick(String fileName) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Scegli un'azione")
//                .setItems(new CharSequence[]{"Download", "Elimina"}, (dialog, which) -> {
//                    switch (which) {
//                        case 0:
//                            // Azione Download
//                            downloadFile(fileName);
//                            break;
//                        case 1:
//                            // Azione Elimina
//                            deleteFile(fileName);
//                            break;
//                    }
//                });
//        builder.show();
//    }

    private void downloadFile(String fileName) {
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

    private void deleteFile(Testo testo) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child("Document/" + testo.getNome());

        // Elimina il file dallo Storage di Firebase
        fileRef.delete().addOnSuccessListener(aVoid -> {
            Log.d("TAG", "File eliminato dallo Storage di Firebase: " + testo.getNome());

            // Rimuovi l'elemento dalla lista della recyclerView
            fileListAdapter.removeFile(testo);
            fileListAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Log.e("TAG", "Errore durante l'eliminazione del file", e));
    }

}