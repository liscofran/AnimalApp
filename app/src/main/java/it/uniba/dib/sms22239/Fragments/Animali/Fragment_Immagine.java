package it.uniba.dib.sms22239.Fragments.Animali;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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

    private ImageButton AddImage;
    private ImageButton ShowImages;
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

        AddImage = getView().findViewById(R.id.AggiungiTesto);
        ShowImages = getView().findViewById(R.id.VisualizzaTesti);
        Text = getView().findViewById(R.id.NomeTesto);

        // Inizializza il launcher per il selettore di immagini
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    // L'immagine è stata selezionata con successo
                    mImageUri = result;
                    ImageUpload();
                    String c1= getString(R.string.icss);
                    Toast.makeText(getActivity(), c1, Toast.LENGTH_LONG).show();
                }
                else
                {
                    // L'utente ha annullato la selezione dell'immagine
                    String c2= getString(R.string.icss2);
                    Toast.makeText(getActivity(), c2, Toast.LENGTH_LONG).show();
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
                Fragment_Visualizza_Immagine visimmagineFragment = new Fragment_Visualizza_Immagine();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, visimmagineFragment)
                        .commit();
            }
        });
    }

    public void ImageUpload()
    {
        String idAnimale = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference("Animali").child(idAnimale).child("Images/" + Text.getText().toString());
        imagesRef.putFile(mImageUri);
    }
}
