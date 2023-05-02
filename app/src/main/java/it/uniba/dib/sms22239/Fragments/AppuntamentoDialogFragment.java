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
import it.uniba.dib.sms22239.Models.Appuntamento;
import it.uniba.dib.sms22239.Models.Prenotazione;

public class AppuntamentoDialogFragment extends DialogFragment {
    private ArrayList<Appuntamento> appuntamenti;
    private ArrayList<Prenotazione> prenotazioni;
    private String data;
    private String idAnimale;

    public AppuntamentoDialogFragment(ArrayList<Appuntamento> appuntamenti, ArrayList<Prenotazione> prenotazioni, String data, String idAnimale) {
        this.appuntamenti = appuntamenti;
        this.prenotazioni = prenotazioni;
        this.data = data;
        this.idAnimale = idAnimale;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Appuntamenti del " + data);

        // Creiamo una lista degli appuntamenti
        ListView listView = new ListView(getActivity());
        ArrayList<String> appuntamentiList = new ArrayList<>();
        for (Appuntamento appuntamento : appuntamenti) {
            if (appuntamento.getData().equals(data)) {
                appuntamentiList.add(appuntamento.getOrario_inizio() + " - " + appuntamento.getOrario_fine());
            }
        }

        /*
        // Creiamo una lista delle prenotazioni
        ArrayList<String> prenotazioniList = new ArrayList<>();
        for (Prenotazione prenotazione : prenotazioni) {
            if (prenotazione.getData().equals(data)) {
                prenotazioniList.add(prenotazione.getOra() + " - " + prenotazione.getNomeAnimale());
            }
        }

         */

        // Aggiungiamo gli appuntamenti e le prenotazioni alla lista
        ArrayList<String> listItems = new ArrayList<>(appuntamentiList);
        // listItems.addAll(prenotazioniList);

        // Mostriamo la lista all'interno della finestra di dialogo
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        builder.setView(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Recuperiamo l'oggetto Appuntamento corrispondente alla posizione selezionata
                Appuntamento appuntamentoSelezionato = null;
                for (Appuntamento appuntamento : appuntamenti) {
                    if (appuntamento.getData().equals(data)) {
                        if (appuntamentiList.get(position).equals(appuntamento.getOrario_inizio() + " - " + appuntamento.getOrario_fine())) {
                            appuntamentoSelezionato = appuntamento;
                            break;
                        }
                    }
                }

                // Verifichiamo se è stato trovato l'appuntamento selezionato
                if (appuntamentoSelezionato != null) {
                    // Passiamo il codice identificativo dell'appuntamento come parametro extra nell'intent
                    Intent intent = new Intent(getActivity(), Activity_Appuntamento.class);
                    intent.putExtra("id_appuntamento", appuntamentoSelezionato.getId_appuntamento());
                    intent.putExtra("ANIMAL_CODE", idAnimale);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Errore: appuntamento non trovato", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return builder.create();
    }
}
