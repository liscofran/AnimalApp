package it.uniba.dib.sms22239.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import it.uniba.dib.sms22239.R;

public class Fragment_Immagine extends Fragment
{
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button AddImage;
    private Button ShowImages;
    private ImageButton UploadImages;
    private ImageButton DownloadImages;
    private ImageView Image;
    private EditText Text;

    private Uri ImageUri;

    public Fragment_Immagine() {
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

        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                openFileChooser();
            }
        });

        UploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });

        ShowImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });
    }

    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            ImageUri = data.getData();

            Picasso.get().load(ImageUri).into(Image);
        }
    }
}
