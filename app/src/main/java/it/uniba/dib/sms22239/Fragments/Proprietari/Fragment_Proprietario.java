package it.uniba.dib.sms22239.Fragments.Proprietari;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.uniba.dib.sms22239.Activities.Animali.Activity_Album_Animali;
import it.uniba.dib.sms22239.Activities.Animali.Activity_Menu_Animali;
import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Activities.Animali.Activity_Miei_Animali;
import it.uniba.dib.sms22239.Activities.Proprietari.Activity_Profilo_Proprietario;
import it.uniba.dib.sms22239.Activities.Activity_QRcode;
import it.uniba.dib.sms22239.Activities.Annunci.Activity_Menu_Annunci;
import it.uniba.dib.sms22239.Activities.Activity_Settings;
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
     //   ((AppCompatActivity) requireActivity()).setSupportActionBar((Toolbar) toolbar);


        getView().findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Home.class));
            }
        });

        getView().findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Profilo_Proprietario.class));
            }
        });

        getView().findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Menu_Annunci.class));
            }
        });

        getView().findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Menu_Animali.class));
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
                startActivity(new Intent(getActivity(), Activity_Settings.class));
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

        getView().findViewById(R.id.album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Album_Animali.class));
            }
        });

        getView().findViewById(R.id.albumimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Album_Animali.class));
            }
        });

        getView().findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Menu_Annunci.class));
            }
        });

        getView().findViewById(R.id.annuncimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Menu_Annunci.class));
            }
        });

        getView().findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_QRcode.class));
            }
        });

    }

}
