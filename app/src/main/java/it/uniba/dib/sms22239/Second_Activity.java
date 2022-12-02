package it.uniba.dib.sms22239;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Second_Activity extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mAuth = FirebaseAuth.getInstance();
        //Bottone Drawer che apre la barra laterale
        drawerLayout = findViewById(R.id.drawer_layout);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //Bottone Home
        findViewById(R.id.imageHome).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Second_Activity.this, HomeActivity.class));
            }
        });
        findViewById(R.id.imageimpostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Second_Activity.this, Preference.class));
            }
        });

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.search:
                        Toast.makeText(Second_Activity.this, "Search Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.offerta:
                        Intent navOfferta = new Intent(Second_Activity.this, HomeActivity.class);
                        startActivity(navOfferta);
                        break;
                    case R.id.share:
                        Toast.makeText(Second_Activity.this, "Share Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.setting:
                        //Toast.makeText(HomeActivity.this, "Setting Selected", Toast.LENGTH_SHORT).show();


                        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Second_Activity.this, Preference.class));
                            }

                        });
                        break;
                    case R.id.lingua:
                        Toast.makeText(Second_Activity.this, "Lingua Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        Toast.makeText(Second_Activity.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        logout();
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    private void logout() {
        mAuth.signOut();
        irMain();
    }

    private void irMain() {
        Intent intent = new Intent(Second_Activity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(Second_Activity.this, "Logout effettuato con successo", Toast.LENGTH_SHORT).show();
        finish();
    }
}