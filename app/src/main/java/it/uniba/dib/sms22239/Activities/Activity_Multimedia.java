package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.dib.sms22239.Fragments.Fragment_Immagine;
import it.uniba.dib.sms22239.Fragments.Fragment_Testo;
import it.uniba.dib.sms22239.Fragments.Fragment_Veterinario;
import it.uniba.dib.sms22239.Fragments.Fragment_Video;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Multimedia extends AppCompatActivity
{
    private Spinner spinner;
    private String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);

        //Back Button
        ImageButton backBtn2 = findViewById(R.id.back);
        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Navbar
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Multimedia.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Multimedia.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Multimedia.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Multimedia.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Multimedia.this, Preference.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Spinner Tipo di Media
        spinner = findViewById(R.id.spinner1);
        spinner.setPrompt("");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.media_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
                switch(selectedItem)
                {
                    case "Testo":
                        Fragment_Testo testoFragment = new Fragment_Testo();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, testoFragment)
                                .commit();
                        break;
                    case "Immagine":
                        Fragment_Immagine immagineFragment = new Fragment_Immagine();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, immagineFragment)
                                .commit();
                        break;
                    case "Video":
                        Fragment_Video videoFragment = new Fragment_Video();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, videoFragment)
                                .commit();
                        break;
                    default:
                        onNothingSelected(parent);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Activity_Multimedia.this, "Scelta non valida", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
