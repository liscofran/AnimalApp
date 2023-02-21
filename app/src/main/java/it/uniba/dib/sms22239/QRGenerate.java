package it.uniba.dib.sms22239;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRGenerate extends AppCompatActivity {
    FirebaseAuth mAuth;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerate);

        imageView = findViewById(R.id.qr_code_image_view);

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

        findViewById(R.id.home).setOnClickListener(view -> startActivity(new Intent(QRGenerate.this, HomeActivity.class)));

        findViewById(R.id.profile).setOnClickListener(view -> startActivity(new Intent(QRGenerate.this, Profile_Activity.class)));

        findViewById(R.id.annunci).setOnClickListener(view -> {
        });

        findViewById(R.id.pet).setOnClickListener(view -> {
        });

        findViewById(R.id.qr).setOnClickListener(view -> startActivity(new Intent(QRGenerate.this, QRcode.class)));

        findViewById(R.id.impostazioni).setOnClickListener(view -> startActivity(new Intent(QRGenerate.this, Preference.class)));

    }

    private void Load_setting() {

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

    private void logout() {
        mAuth.signOut();
        irMain();
    }

    private void irMain() {
        Intent intent = new Intent(QRGenerate.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(QRGenerate.this, "Logout effettuato con successo", Toast.LENGTH_SHORT).show();
        finish();
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

}
