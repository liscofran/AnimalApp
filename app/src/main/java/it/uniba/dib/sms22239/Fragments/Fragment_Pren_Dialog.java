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

import java.util.ArrayList;

import it.uniba.dib.sms22239.Activities.Activity_Appuntamento;
import it.uniba.dib.sms22239.Activities.Activity_Prenotazione;
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

                // Verifichiamo se è stata trovata la prenotazione selezionata
                if (prenotazioneSelezionata != null) {
                    // Passiamo il codice identificativo della prenotazione come parametro extra nell'intent
                    Intent intent = new Intent(getActivity(), Activity_Prenotazione.class);
                    intent.putExtra("id_prenotazione", prenotazioneSelezionata.id_prenotazione);
                    intent.putExtra("ANIMAL_CODE", idAnimale);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Errore: prenotazione non trovata", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return builder.create();
    }
}
