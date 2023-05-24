package it.uniba.dib.sms22239.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import it.uniba.dib.sms22239.R;


public class Fragment_esame_veterinario extends Fragment {
    private ImageButton backButton;
    private ImageView imageView;
    private EditText dataEditText;
    private EditText orarioInizioEditText;
    private EditText orarioFineEditText;
    private EditText nomCognPropEditText;
    private EditText animaleEditText;
    private Spinner statoEsameSpinner;
    private Spinner tipoEsameSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_esame_veterinario, container, false);

        backButton = view.findViewById(R.id.back);
        imageView = view.findViewById(R.id.imageView2);
        dataEditText = view.findViewById(R.id.data);
        orarioInizioEditText = view.findViewById(R.id.orario_inizio);
        orarioFineEditText = view.findViewById(R.id.orario_fine);
        nomCognPropEditText = view.findViewById(R.id.nom_cogn_prop);
        animaleEditText = view.findViewById(R.id.animale);
        statoEsameSpinner = view.findViewById(R.id.spinner1);
        tipoEsameSpinner = view.findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> statoEsameAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.statoesame, android.R.layout.simple_spinner_item);
        statoEsameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statoEsameSpinner.setAdapter(statoEsameAdapter);

        ArrayAdapter<CharSequence> tipoEsameAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.tipoesame, android.R.layout.simple_spinner_item);
        tipoEsameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoEsameSpinner.setAdapter(tipoEsameAdapter);

        // Aggiungi eventuali altri listener o codice di gestione qui

        return view;
    }
}

