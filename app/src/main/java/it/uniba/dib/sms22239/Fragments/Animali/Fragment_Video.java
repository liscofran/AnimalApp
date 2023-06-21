package it.uniba.dib.sms22239.Fragments.Animali;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Fragment_Video extends Fragment {

    private static final int REQUEST_CODE_SELECT_VIDEO = 1234;
    private EditText Nome_Video;

    public Fragment_Video()
    {
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
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton aggiungi = getView().findViewById(R.id.AggiungiTesto);
        ImageButton visualizza = getView().findViewById(R.id.VisualizzaTesti);
        Nome_Video = getView().findViewById(R.id.NomeVideo);

        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideoFromGallery();
            }
        });

        visualizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Visualizza_Video visvideoFragment = new Fragment_Visualizza_Video();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, visvideoFragment)
                        .commit();
            }
        });
    }

    private void selectVideoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedVideoUri = data.getData();
            String Nome_video = Nome_Video.getText().toString();
            uploadVideoToFirebase(selectedVideoUri, Nome_video);
            String c1= getString(R.string.ups);
            Toast.makeText(getActivity(), c1, Toast.LENGTH_SHORT).show();

        }
    }

    private void uploadVideoToFirebase(Uri videoUri, String Nome_video)
    {
        String idAnimale = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
        StorageReference videoRef = FirebaseStorage.getInstance().getReference("Animali").child(idAnimale).child("Videos/" + Nome_video);
        UploadTask uploadTask = videoRef.putFile(videoUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
}
