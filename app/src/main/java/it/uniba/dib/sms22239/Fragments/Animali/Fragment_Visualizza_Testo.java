package it.uniba.dib.sms22239.Fragments.Animali;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import it.uniba.dib.sms22239.Activities.Animali.Activity_Multimedia;
import it.uniba.dib.sms22239.Models.Testo;
import it.uniba.dib.sms22239.R;

public class Fragment_Visualizza_Testo extends Fragment {

    private StorageReference txtRef;
    private RecyclerView recyclerView;
    private static List<Testo> mFileList;
    private FileListAdapter fileListAdapter;
    String idAnimale;

    public Fragment_Visualizza_Testo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visualizza_testo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        idAnimale = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
        txtRef = FirebaseStorage.getInstance().getReference("Animali").child(idAnimale).child("Testi");
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
                {String c1= getString(R.string.ent);
                    String c2= getString(R.string.ent2);
                    String c3= getString(R.string.conferma);
                    String c4= getString(R.string.ent3);
                    String c5= getString(R.string.annulla);
                    new AlertDialog.Builder(getActivity())


                            .setTitle(c1)
                            .setMessage(c2)
                            .setPositiveButton(c3, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteFile(testo);
                                    Toast.makeText(getActivity(), c4, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), Activity_Multimedia.class);
                                    startActivity(intent.putExtra("ANIMAL_CODE",idAnimale));
                                }
                            })
                            .setNegativeButton(c5, null)
                            .show();
                }
            });
        }
    }
    private void downloadFile(String fileName) {
        String c9= getString(R.string.edt);

        String idAnimale = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
        StorageReference fileRef = FirebaseStorage.getInstance().getReference("Animali").child(idAnimale).child("Testi/" + fileName);

        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "text/plain");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String c8= getString(R.string.ent4);

            startActivity(Intent.createChooser(intent, c8));
        }).addOnFailureListener(e -> Log.e("TAG", c9, e));
    }

    private void deleteFile(Testo testo) {
        String idAnimale = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
        StorageReference fileRef = FirebaseStorage.getInstance().getReference("Animali").child(idAnimale).child("Testi").child(testo.getNome());
        String c11= getString(R.string.enr);

        // Elimina il file dallo Storage di Firebase
        fileRef.delete().addOnSuccessListener(aVoid -> {
            String c10= getString(R.string.fes);

            Log.d("TAG", c10 + testo.getNome());

            // Rimuovi l'elemento dalla lista della recyclerView
            fileListAdapter.removeFile(testo);
            fileListAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Log.e("TAG", c11, e));
    }

}