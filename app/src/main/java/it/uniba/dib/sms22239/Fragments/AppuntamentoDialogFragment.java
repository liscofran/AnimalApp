package it.uniba.dib.sms22239.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import it.uniba.dib.sms22239.Models.Appuntamento;
import it.uniba.dib.sms22239.Models.Prenotazione;

public class AppuntamentoDialogFragment extends DialogFragment {
    private ArrayList<Appuntamento> appuntamenti;
    private ArrayList<Prenotazione> prenotazioni;
    private String data;

    public AppuntamentoDialogFragment(ArrayList<Appuntamento> appuntamenti, ArrayList<Prenotazione> prenotazioni, String data) {
        this.appuntamenti = appuntamenti;
        this.prenotazioni = prenotazioni;
        this.data = data;
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

        return builder.create();
    }
}
