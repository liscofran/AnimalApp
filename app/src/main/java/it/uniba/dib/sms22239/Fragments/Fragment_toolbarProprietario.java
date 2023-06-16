package it.uniba.dib.sms22239.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.dib.sms22239.Activities.Activity_Animali;
import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Activities.Activity_Profile_Proprietario_Ente;
import it.uniba.dib.sms22239.Activities.Activity_QRcode;
import it.uniba.dib.sms22239.Activities.Activity_Segnalazioni_Offerte;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Fragment_toolbarProprietario extends Fragment {
    public Fragment_toolbarProprietario() {
    }

    public static Fragment_toolbarProprietario newInstance(String param1, String param2) {
        Fragment_toolbarProprietario fragment = new Fragment_toolbarProprietario();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_toolbar, container, false);
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
                startActivity(new Intent(getActivity(), Activity_Profile_Proprietario_Ente.class));
            }
        });

        view.findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Segnalazioni_Offerte.class));
            }
        });

        view.findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Animali.class));
            }
        });

        view.findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_QRcode.class));
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