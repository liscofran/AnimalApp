package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import it.uniba.dib.sms22239.Fragments.Fragment_toolbarEnte;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarProprietario;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarVeterinario;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_QRGenerate extends AppCompatActivity
{
    FirebaseAuth mAuth;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerate);

        ImageButton backbutton = findViewById(R.id.back);
        ImageView imageView = findViewById(R.id.qr_code_image_view);

        autenticazione();

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 onBackPressed();
            }
        });

        // Retrieve the animal code from the intent
        String animalName = getIntent().getStringExtra("ANIMAL_CODE");

        // Generate the QR code and set it to the ImageView
        try {
            Bitmap bitmap = encodeAsBitmap(animalName);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        mAuth = FirebaseAuth.getInstance();

        Load_setting();



    }

    private void Load_setting()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        String orien = sp.getString("ORIENTATION", "false");
        if ("1".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        } else if ("2".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else if ("3".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onResume() {
        Load_setting();
        super.onResume();
    }

    @Nullable
    private Bitmap encodeAsBitmap(String data) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    protected void autenticazione()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
        Query query = myRef.orderByChild("classe");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Recupera il valore dell'attributo "classe"
                String classe = snapshot.child("classe").getValue(String.class);


                // Verifica il valore dell'attributo "classe"
                if (classe.equals("Veterinario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbarVeterinario());
                    fragmentTransaction.commit();
                    flag = "veterinario";
                }
                else if(classe.equals("Proprietario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbarProprietario());
                    fragmentTransaction.commit();
                    flag = "proprietario";
                }
                else
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbarEnte());
                    fragmentTransaction.commit();
                    flag = "ente";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'evento di annullamento
                String c4= getString(R.string.a3);
                Log.e("Firebase", c4 + error.getMessage());
            }
        });
    }

}
