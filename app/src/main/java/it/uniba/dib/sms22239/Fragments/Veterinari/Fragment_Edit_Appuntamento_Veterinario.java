package it.uniba.dib.sms22239.Fragments.Veterinari;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import it.uniba.dib.sms22239.Activities.Veterinari.Activity_Calendario_Appuntamenti_Veterinario;
import it.uniba.dib.sms22239.R;



public class Fragment_Edit_Appuntamento_Veterinario extends Fragment {

    private CalendarView DataEditText;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_appuntamento_veterinario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String idAppuntamento = requireActivity().getIntent().getStringExtra("id_appuntamento");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Appuntamenti").child(idAppuntamento);

        DataEditText = getView().findViewById(R.id.simpleCalendarView);
        TimePicker oraInizioEditText = getView().findViewById(R.id.time_start);
        TimePicker oraFineEditText = getView().findViewById(R.id.time_end);


        ImageButton save = getView().findViewById(R.id.save);

        // Imposta il listener per la selezione della data all'interno del metodo onCreate()
        DataEditText.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                // Creazione dell'oggetto Appuntamento con i dati selezionati dall'utente
                String data = LocalDate.of(year, month + 1, day).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                // Salva la data selezionata sul database
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Appuntamenti").child(idAppuntamento);
                mDatabase.child("data").setValue(data);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ottieni l'ora di inizio e fine dalla UI
                TimePicker timePicker = getView().findViewById(R.id.time_start);
                LocalTime ora_inizio = LocalTime.of(timePicker.getHour(), timePicker.getMinute());

                TimePicker timePicker2 = getView().findViewById(R.id.time_end);
                LocalTime ora_fine = LocalTime.of(timePicker2.getHour(), timePicker2.getMinute());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String ora_inizio_string = ora_inizio.format(formatter);
                String ora_fine_string = ora_fine.format(formatter);

                // Salva le informazioni sull'orario di inizio e fine sul database
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Appuntamenti").child(idAppuntamento);
                mDatabase.child("orario_fine").setValue(ora_inizio_string);
                mDatabase.child("orario_inizio").setValue(ora_fine_string);

                // Torna all'activity precedente
                Intent intent = new Intent(getActivity(), Activity_Calendario_Appuntamenti_Veterinario.class);
                String c5= getString(R.string.c2);

                Toast.makeText(getActivity(), c5, Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });


    }

}

