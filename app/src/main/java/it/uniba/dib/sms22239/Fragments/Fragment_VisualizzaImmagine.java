package it.uniba.dib.sms22239.Fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms22239.Activities.Activity_Multimedia;
import it.uniba.dib.sms22239.Models.Image;
import it.uniba.dib.sms22239.R;

public class Fragment_VisualizzaImmagine extends Fragment
{

    private StorageReference storageRef;
    private RecyclerView recyclerView;
    private static List<Image> mImageList;
    private static ImageAdapter mAdapter;
    protected static String idAnimale;

    public Fragment_VisualizzaImmagine() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        idAnimale = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_visualizzaimmagine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        storageRef = FirebaseStorage.getInstance().getReference("Animali").child(idAnimale).child("Images");
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mImageList = new ArrayList<>();
        mAdapter = new ImageAdapter(mImageList);
        recyclerView.setAdapter(mAdapter);

        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference storageRef : listResult.getItems()) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Image image = new Image();
                            image.setUrl(uri.toString());
                            image.setNome(storageRef.getName());
                            mImageList.add(image);
                            mAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getActivity(), "Caricamento non avvenuto con successo", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder>
    {

        private List<Image> AdaptImageList;

        public ImageAdapter(List<Image> imageList) {
            AdaptImageList = imageList;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.immagine_item, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            Image image = AdaptImageList.get(position);
            holder.bind(image);
        }

        @Override
        public int getItemCount() {
            return AdaptImageList.size();
        }
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder {

        //private final TextView mTitleTextView;
        private final ImageView mImageView;
        private final ImageButton mDownloadButton;
        private final ImageButton mDeleteButton;


        public ImageViewHolder(@NonNull View itemView)
        {
            super(itemView);
//            mTitleTextView = itemView.findViewById(R.id.title_text_view);
            mImageView = itemView.findViewById(R.id.image_view);
            mDownloadButton = itemView.findViewById(R.id.download);
            mDeleteButton = itemView.findViewById(R.id.delete_button);
        }

        public void bind(Image image)
        {
//            mTitleTextView.setText(video.getTitle());
            Picasso.get().load(image.getUrl()).into(mImageView);

            mDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadImage(image);
                }
            });

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Elimina immagine")
                            .setMessage("Sei sicuro di voler eliminare l'immagine?")
                            .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteImage(image);
                                    Toast.makeText(getActivity(), "Immagine eliminata con successo!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), Activity_Multimedia.class);
                                    startActivity(intent.putExtra("ANIMAL_CODE",idAnimale));
                                }
                            })
                            .setNegativeButton("Annulla", null)
                            .show();
                }
            });

        }

        private void downloadImage(Image image)
        {
            String imageName = image.getNome();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(image.getUrl()))
                    .setTitle(imageName)
                    .setDescription("Downloading")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);

            // Impostare il nome del file come il titolo dell'immagine
            File file = new File(itemView.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), imageName);
            request.setDestinationUri(Uri.fromFile(file));

            DownloadManager downloadManager = (DownloadManager) itemView.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);
            Toast.makeText(itemView.getContext(), "Download avvenuto con successo", Toast.LENGTH_SHORT).show();
        }

        private void deleteImage(Image image)
        {
            // Ottenere il riferimento di archiviazione per l'immagine
            StorageReference imageRef = FirebaseStorage.getInstance().getReference("Animali").child(getIdAnimale()).child("Images").child(image.getNome());

            // Elimina il file dallo storage di Firebase
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Rimuovi l'immagine dal RecyclerView
                    mImageList.remove(image);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(itemView.getContext(), "Immagine eliminata con successo", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(itemView.getContext(), "Eliminazione non riuscita", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static String getIdAnimale()
    {
        return idAnimale;
    }
}
