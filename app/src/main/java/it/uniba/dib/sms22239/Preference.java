package it.uniba.dib.sms22239;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;

public class Preference extends PreferenceActivity  {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        Load_setting();
    }
    private void Load_setting() {
/*
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        boolean chk_night = sp.getBoolean("NIGHT",false);
        if(chk_night){
            getListView().setBackgroundColor(Color.parseColor("#222222"));
        }else{
            getListView().setBackgroundColor(Color.parseColor("#ffffff"));
        }

        CheckBoxPreference chk_night_instant = (CheckBoxPreference)findPreference("NIGHT");
        chk_night_instant.setOnPreferenceChangeListener(new androidx.preference.Preference.OnPreferenceChangeListener(){

            @Override
            public boolean onPreferenceChange(androidx.preference.Preference preference, Object obj) {

                boolean yes = (boolean) obj;
                if(yes){
                    getListView().setBackgroundColor(Color.parseColor("#222222"));
                }else {
                    getListView().setBackgroundColor(Color.parseColor("#ffffff"));
                }
                return true;
            }

        });

        ListPreference LP = (ListPreference)findPreference("ORIENTATION");

    }

    @Override
    protected void onResume() {
        Load_setting();
        super.onResume();
*/
    }

}
