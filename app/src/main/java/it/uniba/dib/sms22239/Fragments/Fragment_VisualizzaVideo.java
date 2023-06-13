package it.uniba.dib.sms22239.Fragments;

import static it.uniba.dib.sms22239.Fragments.Fragment_VisualizzaImmagine.idAnimale;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms22239.Activities.Activity_Multimedia;
import it.uniba.dib.sms22239.Models.Video;
import it.uniba.dib.sms22239.R;

public class Fragment_VisualizzaVideo extends Fragment {

    private StorageReference mStorageRef;
    private RecyclerView mRecyclerView;
    private static List<Video> mVideoList;
    private static VideoAdapter mAdapter;
    String idAnimale;

    public Fragment_VisualizzaVideo()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_visualizzavideo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        idAnimale = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
        mStorageRef = FirebaseStorage.getInstance().getReference("Animali").child(idAnimale).child("Videos");
        mRecyclerView = getView().findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mVideoList = new ArrayList<>();
        mAdapter = new VideoAdapter(mVideoList);
        mRecyclerView.setAdapter(mAdapter);

        mStorageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference storageRef : listResult.getItems()) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Video video = new Video();
                            video.setUrl(uri.toString());
                            video.setTitle(storageRef.getName());
                            mVideoList.add(video);
                            mAdapter.notifyDataSetChanged();
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

        private class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {

            private List<Video> mVideoList;

            public VideoAdapter(List<Video> videoList) {
                mVideoList = videoList;
            }

            @NonNull
            @Override
            public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_item, parent, false);
                return new VideoViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
                Video video = mVideoList.get(position);
                holder.bind(video);
            }

            @Override
            public int getItemCount() {
                return mVideoList.size();
            }
        }

        private class VideoViewHolder extends RecyclerView.ViewHolder {

            private final TextView mTitleTextView;
            private final VideoView mVideoView;
            private final ImageButton mStartButton;
            private final ImageButton mPauseButton;
            private final ImageButton mDeleteButton;
            private final ImageButton mDownloadButton;
            private final ProgressBar mProgressBar;

            public VideoViewHolder(@NonNull View itemView) {
                super(itemView);
                mTitleTextView = itemView.findViewById(R.id.title_text_view);
                mVideoView = itemView.findViewById(R.id.video_view);
                mStartButton = itemView.findViewById(R.id.start_button);
                mPauseButton = itemView.findViewById(R.id.pause_button);
                mDownloadButton = itemView.findViewById(R.id.download_button);
                mDeleteButton = itemView.findViewById(R.id.btn_elimina);
                mProgressBar = itemView.findViewById(R.id.progress_bar);
                mProgressBar.setVisibility(View.VISIBLE); // impostare la ProgressBar come visibile
            }

            public void bind(Video video) {
                mTitleTextView.setText(video.getTitle());
                mVideoView.setVideoURI(Uri.parse(video.getUrl()));
                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // Impostare l'autoplay del video quando è stato caricato
                        mp.setLooping(false);
                        //mp.start();
                        // Nascondere la ProgressBar quando il video è pronto
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
                // Mostrare la ProgressBar quando il video sta venendo caricato
                mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                            mProgressBar.setVisibility(View.VISIBLE);
                        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });

                mStartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mVideoView.isPlaying()) {
                            mVideoView.resume();
                        } else {
                            mVideoView.start();
                        }
                    }
                });

                mPauseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVideoView.pause();
                    }
                });


                mDownloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadVideo(video);
                    }
                });

                mDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String c1= getString(R.string.ev);
                        String c2= getString(R.string.ev1);
                        String c3= getString(R.string.conferma);
                        String c4= getString(R.string.ev2);
                        String c5= getString(R.string.annulla);

                        new AlertDialog.Builder(getActivity())

                                .setTitle("Elimina video")
                                .setMessage("Sei sicuro di voler eliminare il video?")
                                .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteVideo(video);
                                        Toast.makeText(getActivity(), "Video eliminato con successo!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), Activity_Multimedia.class);
                                        startActivity(intent.putExtra("ANIMAL_CODE",idAnimale));
                                    }
                                })
                                .setNegativeButton("Annulla", null)
                                .show();
                    }
                });
            }

            private void downloadVideo(Video video) {
                String videoTitle = video.getTitle();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(video.getUrl()))
                        .setTitle(videoTitle)
                        .setDescription("Downloading")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true);

                // Impostare il nome del file come il titolo del video
                File file = new File(itemView.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), videoTitle);
                request.setDestinationUri(Uri.fromFile(file));

                DownloadManager downloadManager = (DownloadManager) itemView.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);
                String c6= getString(R.string.dwn);

                Toast.makeText(itemView.getContext(), c6, Toast.LENGTH_SHORT).show();
            }

            private void deleteVideo(Video video)
            {
                mProgressBar.setVisibility(View.VISIBLE); // mostrare la ProgressBar durante l'eliminazione
                StorageReference videoRef = FirebaseStorage.getInstance().getReferenceFromUrl(video.getUrl());
                videoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // eliminazione completata con successo, rimuovi il video dalla lista
                        mVideoList.remove(video);
                        mAdapter.notifyDataSetChanged();
                        mProgressBar.setVisibility(View.GONE); // nascondere la ProgressBar dopo l'eliminazione
                        String c7= getString(R.string.ev2);
                        Toast.makeText(itemView.getContext(), c7, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // errore durante l'eliminazione, mostrare un messaggio di errore
                        mProgressBar.setVisibility(View.GONE);
                        String c8= getString(R.string.ev3);
                        Toast.makeText(itemView.getContext(), c8, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
}