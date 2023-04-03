package it.uniba.dib.sms22239.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import it.uniba.dib.sms22239.R;

public class Fragment_Immagine extends Fragment
{

    private ActivityResultLauncher<String> imagePickerLauncher;
    private Uri mImageUri;

    private Button AddImage;
    private Button ShowImages;
    private ImageButton UploadImages;
    private ImageButton DownloadImages;
    private ImageView Image;
    private EditText Text;

    public Fragment_Immagine() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_immagine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        AddImage = getView().findViewById(R.id.AggiungiImmagini);
        ShowImages = getView().findViewById(R.id.VisualizzaImmagine);
        UploadImages = getView().findViewById(R.id.UploadImmagini);
        DownloadImages = getView().findViewById(R.id.DownloadImmagini);
        Image = getView().findViewById(R.id.Immagine);
        Text = getView().findViewById(R.id.NomeImmagine);

        // Inizializza il launcher per il selettore di immagini
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    // L'immagine è stata selezionata con successo
                    mImageUri = result;
                    Image.setImageURI(mImageUri);
                    ImageUpload();
                    Toast.makeText(getActivity(), "Immagine caricata con successo", Toast.LENGTH_LONG).show();
                }
                else
                {
                    // L'utente ha annullato la selezione dell'immagine
                    Toast.makeText(getActivity(), "Selezione immagine annullata", Toast.LENGTH_LONG).show();
                }
            }
        });

        AddImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                imagePickerLauncher.launch("image/*");
            }
        });

        ShowImages.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Fragment_VisualizzaImmagine visimmagineFragment = new Fragment_VisualizzaImmagine();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, visimmagineFragment)
                        .commit();
            }
        });
    }

    public void ImageUpload()
    {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("Images/" + Text.getText().toString());
        imagesRef.putFile(mImageUri);
    }
}
