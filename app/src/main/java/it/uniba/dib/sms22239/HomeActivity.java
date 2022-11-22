package it.uniba.dib.sms22239;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btnLogout;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    Button btnIntent;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AnimalApp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);//serve per inserire l'icona del menu per la navigation view

        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        btnIntent = findViewById(R.id.btnIntent);

        btnIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Second_Activity.class));
            }
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        View view = navigationView.inflateHeaderView(R.layout.drawer_header);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }


    }

    // per creare le icone dell'actionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                Toast.makeText(HomeActivity.this, "Search is Expanded", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                Toast.makeText(HomeActivity.this, "Search is Collapse", Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Cerca...");

        return true;
    }


    // azioni dei singoli item presenti nel file menu.xml
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            irMain();
        }
    }

    private void logout() {
        mAuth.signOut();
        irMain();
    }

    private void irMain() {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.offerta:
                Toast.makeText(this, "Offerta", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Toast.makeText(this, "Condividi", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this, "Impostazioni", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lingua:
                Toast.makeText(this, "Lingua", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                Toast.makeText(this, "Profilo", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
/*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.offerta:
                Toast.makeText(this, "Offerta è stato premuto", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Toast.makeText(this, "Condividi è stato premuto", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this, "Impostazioni è stato premuto", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home:
                Toast.makeText(this, "Home è stato premuto", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lingua:
                Toast.makeText(this, "Lingua è stato premuto", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
 */
