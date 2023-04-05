package it.uniba.dib.sms22239.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import it.uniba.dib.sms22239.Models.Appuntamento;
import it.uniba.dib.sms22239.R;

public class Activity_Nuovo_Appuntamento extends AppCompatActivity {

    private CalendarView calendarView;
    private Button salvaButton;
    private int idVeterinario;
    private int idAppuntamento;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_appuntamento);

        // Inizializza gli elementi della UI
        calendarView = findViewById(R.id.simpleCalendarView);
        salvaButton = findViewById(R.id.salva);
        db = FirebaseFirestore.getInstance();

        // Imposta il listener sul CalendarView per ottenere la data selezionata dall'utente
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                // Creazione dell'oggetto Appuntamento con i dati selezionati dall'utente
                TimePicker timePicker = findViewById(R.id.ora_inizio);



                int ora_inizio = timePicker.getHour();
                int minuti_inizio = timePicker.getMinute();
                Calendar inizio = Calendar.getInstance();
                inizio.set(Calendar.HOUR_OF_DAY, ora_inizio);
                inizio.set(Calendar.MINUTE, minuti_inizio);

                TimePicker timePicker2 = findViewById(R.id.ora_fine);

                int ora_fine = timePicker.getHour();
                int minuti_fine = timePicker.getMinute();
                Calendar fine = Calendar.getInstance();
                fine.set(Calendar.HOUR_OF_DAY, ora_inizio);
                fine.set(Calendar.MINUTE, minuti_inizio);

                String data = LocalDate.of(year, month + 1, day).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                Appuntamento appuntamento = new Appuntamento(idVeterinario, inizio, fine, data, idAppuntamento);


                // Imposta il listener sul bottone "Salva"
                salvaButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }
        });

    }
}

