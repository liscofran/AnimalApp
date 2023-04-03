package it.uniba.dib.sms22239.Fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms22239.Models.Video;
import it.uniba.dib.sms22239.R;

public class Fragment_VisualizzaVideo extends Fragment {

    private StorageReference mStorageRef;
    private RecyclerView mRecyclerView;
    private List<Video> mVideoList;
    private VideoAdapter mAdapter;


    public Fragment_VisualizzaVideo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_visualizzavideo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("Videos");
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

        private static class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {

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

        private static class VideoViewHolder extends RecyclerView.ViewHolder {

            private final TextView mTitleTextView;
            private final VideoView mVideoView;
            private final Button mStartButton;
            private final Button mPauseButton;
            private final Button mDownloadButton;
            private final ProgressBar mProgressBar;

            public VideoViewHolder(@NonNull View itemView) {
                super(itemView);
                mTitleTextView = itemView.findViewById(R.id.title_text_view);
                mVideoView = itemView.findViewById(R.id.video_view);
                mStartButton = itemView.findViewById(R.id.start_button);
                mPauseButton = itemView.findViewById(R.id.pause_button);
                mDownloadButton = itemView.findViewById(R.id.download_button);
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
                Toast.makeText(itemView.getContext(), "Download avvenuto con successo", Toast.LENGTH_SHORT).show();
            }

        }
}