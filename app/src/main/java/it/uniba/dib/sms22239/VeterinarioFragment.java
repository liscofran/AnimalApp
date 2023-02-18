package it.uniba.dib.sms22239;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class VeterinarioFragment extends Fragment {

    public VeterinarioFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_veterinario, container, false);
        // qui puoi inizializzare le tue view o aggiungere ulteriori operazioni
        return view;
    }
}
