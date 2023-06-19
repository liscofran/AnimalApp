package it.uniba.dib.sms22239;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import it.uniba.dib.sms22239.Activities.Activity_Main;

public class Preference extends PreferenceActivity
{
    FirebaseAuth mAuth;
    String selectedLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        Load_setting();
        mAuth = FirebaseAuth.getInstance();
    }

    private void Load_setting()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        android.preference.Preference logout = findPreference("Logout");
        logout.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.preference.Preference preference) {
                logout();
                return true;
            }
        });

        ListPreference languagePreference = (ListPreference) findPreference("language");
        selectedLanguage = sp.getString("language", "it");
        languagePreference.setEntries(R.array.language_entries);
        languagePreference.setEntryValues(R.array.language_values);
        languagePreference.setDefaultValue("it");
        languagePreference.setSummary(languagePreference.getEntry());
        languagePreference.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference preference, Object newValue) {
                selectedLanguage = (String) newValue;
                setLocale(selectedLanguage);
                recreateActivity();
                return true;
            }
        });
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("language", language);
        editor.apply();

        Intent refreshIntent = new Intent(this, Preference.class);
        startActivity(refreshIntent);
        finish();
    }

    private void recreateActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        Load_setting();
        super.onResume();
    }

    private void logout() {
        mAuth.signOut();
        irMain();
    }

    private void irMain() {
        Intent intent = new Intent(Preference.this, Activity_Main.class);
        startActivity(intent);
        Toast.makeText(Preference.this, "Logout", Toast.LENGTH_SHORT).show();
        finish();
    }
}