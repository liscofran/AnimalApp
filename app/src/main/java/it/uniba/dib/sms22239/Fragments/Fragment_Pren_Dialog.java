package it.uniba.dib.sms22239.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.uniba.dib.sms22239.Activities.Activity_Appuntamento;
import it.uniba.dib.sms22239.Activities.Activity_Prenotazione;
import it.uniba.dib.sms22239.Activities.Activity_Prenotazione_Animale;
import it.uniba.dib.sms22239.Models.Appuntamento;
import it.uniba.dib.sms22239.Models.Prenotazione;

public class Fragment_Pren_Dialog extends DialogFragment {
    private ArrayList<Prenotazione> prenotazioni;
    private String data;
    private String idAnimale;

    public Fragment_Pren_Dialog(ArrayList<Prenotazione> prenotazioni, String data, String idAnimale) {
        this.prenotazioni = prenotazioni;
        this.data = data;
        this.idAnimale = idAnimale;
    }

    public Fragment_Pren_Dialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Prenotazione del " + data);


        // Creiamo una lista delle prenotazioni
        ListView listView = new ListView(getActivity());
        ArrayList<String> prenotazioniList = new ArrayList<>();
        for (Prenotazione prenotazione : prenotazioni) {
            if (prenotazione.data.equals(data)) {
                prenotazioniList.add(prenotazione.orario_inizio + " - " + prenotazione.orario_fine);
            }
        }


        // Aggiungiamo le prenotazioni alla lista
        ArrayList<String> listItems = new ArrayList<>(prenotazioniList);

        // Mostriamo la lista all'interno della finestra di dialogo
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        builder.setView(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Recuperiamo l'oggetto Prenotazione corrispondente alla posizione selezionata
                Prenotazione prenotazioneSelezionata = null;
                for (Prenotazione prenotazione : prenotazioni) {
                    if (prenotazione.data.equals(data)) {
                        if (prenotazioniList.get(position).equals(prenotazione.orario_inizio + " - " + prenotazione.orario_fine)) {
                            prenotazioneSelezionata = prenotazione;
                            break;
                        }
                    }
                }

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String currentUserId = currentUser.getUid();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
                Prenotazione finalPrenotazioneSelezionata = prenotazioneSelezionata;
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Intent intent;
                        if (finalPrenotazioneSelezionata != null) {
                            String userClass = dataSnapshot.child("classe").getValue(String.class);
                            if (userClass != null && userClass.equals("Veterinario")) {
                                intent = new Intent(getActivity(), Activity_Prenotazione.class);
                                intent.putExtra("ANIMAL_CODE", idAnimale);
                            } else {
                                // Utente non è di classe "Veterinario"
                                intent = new Intent(getActivity(), Activity_Prenotazione_Animale.class);
                                intent.putExtra("ANIMAL_CODE", idAnimale);
                            }

                            intent.putExtra("id_prenotazione", finalPrenotazioneSelezionata.id_prenotazione);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

            }
        });
        return builder.create();
    }

}



