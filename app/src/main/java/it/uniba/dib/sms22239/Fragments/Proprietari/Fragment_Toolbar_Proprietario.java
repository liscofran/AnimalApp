package it.uniba.dib.sms22239.Fragments.Proprietari;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.dib.sms22239.Activities.Animali.Activity_Menu_Animali;
import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Activities.Proprietari.Activity_Profilo_Proprietario;
import it.uniba.dib.sms22239.Activities.Activity_QRcode;
import it.uniba.dib.sms22239.Activities.Annunci.Activity_Menu_Annunci;
import it.uniba.dib.sms22239.Activities.Activity_Settings;
import it.uniba.dib.sms22239.R;

public class Fragment_Toolbar_Proprietario extends Fragment {
    public Fragment_Toolbar_Proprietario() {
    }

    public static Fragment_Toolbar_Proprietario newInstance(String param1, String param2) {
        Fragment_Toolbar_Proprietario fragment = new Fragment_Toolbar_Proprietario();
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
                startActivity(new Intent(getActivity(), Activity_Profilo_Proprietario.class));
            }
        });

        view.findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Menu_Annunci.class));
            }
        });

        view.findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Menu_Animali.class));
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
                startActivity(new Intent(getActivity(), Activity_Settings.class));
            }
        });

    }
}