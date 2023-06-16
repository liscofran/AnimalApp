package it.uniba.dib.sms22239.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Activities.Activity_Profilo_Veterinario;
import it.uniba.dib.sms22239.Activities.Activity_QRcode;
import it.uniba.dib.sms22239.Activities.Activity_Scheda_Veterinario;
import it.uniba.dib.sms22239.Activities.Activity_Segnalazioni_Offerte;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Fragment_toolbarVeterinario extends Fragment {

    public Fragment_toolbarVeterinario() {
    }

    public static Fragment_toolbarVeterinario newInstance(String param1, String param2) {
        Fragment_toolbarVeterinario fragment = new Fragment_toolbarVeterinario();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_toolbar1, container, false);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){
        view.findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Home.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Profilo_Veterinario.class));
            }
        });

        view.findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Segnalazioni_Offerte.class));
            }
        });

        view.findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_QRcode.class));
            }
        });

        view.findViewById(R.id.scheda).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Scheda_Veterinario.class));
            }
        });

        view.findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Preference.class));
            }
        });

    }

}