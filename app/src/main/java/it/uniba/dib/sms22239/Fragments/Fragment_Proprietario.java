package it.uniba.dib.sms22239.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import it.uniba.dib.sms22239.Activities.Activity_Animal_Profile;
import it.uniba.dib.sms22239.Activities.Activity_Animali;
import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Activities.Activity_Miei_Animali;
import it.uniba.dib.sms22239.Activities.Activity_Profile_Proprietario_Ente;
import it.uniba.dib.sms22239.Activities.Activity_Profilo_Segnalazione;
import it.uniba.dib.sms22239.Activities.Activity_QRcode;
import it.uniba.dib.sms22239.Activities.Activity_Registrazione_Segnalazione;
import it.uniba.dib.sms22239.Activities.Activity_Segnalazioni_Offerte;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Fragment_Proprietario extends Fragment
{
//    SearchView searchView;
    public Fragment_Proprietario()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_proprietario, container, false);
        // qui puoi inizializzare le tue view o aggiungere ulteriori operazioni
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        View toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar((Toolbar) toolbar);

        getView().findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Home.class));
            }
        });

        getView().findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Profile_Proprietario_Ente.class));
            }
        });

        getView().findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Segnalazioni_Offerte.class));
            }
        });

        getView().findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Animali.class));
            }
        });

        getView().findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_QRcode.class));
            }
        });

        getView().findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Preference.class));
            }
        });



        getView().findViewById(R.id.tuoianimalimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Miei_Animali.class));
            }
        });

        getView().findViewById(R.id.tuoianimali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Miei_Animali.class));
            }
        });

        getView().findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Segnalazioni_Offerte.class));
            }
        });

        getView().findViewById(R.id.annuncimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Segnalazioni_Offerte.class));
            }
        });

        getView().findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_QRcode.class));
            }
        });

//        searchView = getActivity().findViewById(R.id.searchView);

    }

}
