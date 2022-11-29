package it.uniba.dib.sms22239;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


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

public class HomeActivity extends AppCompatActivity {

    Button btnLogout;
    FirebaseAuth mAuth;
    Button btnIntent;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    private LinearLayout ml;
    //private TextView Tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        //Tv = findViewById(R.id.textTitle);
        Load_setting();


        drawerLayout = findViewById(R.id.drawer_layout);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        findViewById(R.id.imageHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Second_Activity.class));
            }
        });
        findViewById(R.id.imageimpostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Preference.class));
            }
        });

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

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        // NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        // NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.search:
                        Toast.makeText(HomeActivity.this, "Search Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.offerta:
                        Intent navOfferta = new Intent(HomeActivity.this, Second_Activity.class);
                        startActivity(navOfferta);
                        break;
                    case R.id.share:
                        Toast.makeText(HomeActivity.this, "Share Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.setting:
                        //Toast.makeText(HomeActivity.this, "Setting Selected", Toast.LENGTH_SHORT).show();

                        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Load_setting();
                            }

                        });
                        break;
                    case R.id.lingua:
                        Toast.makeText(HomeActivity.this, "Lingua Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        Toast.makeText(HomeActivity.this, "Profile Selected", Toast.LENGTH_SHORT).show();
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

    private void Load_setting() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean chk_night = sp.getBoolean("NIGHT", false);
        if (chk_night) {
            drawerLayout.setBackgroundColor(Color.parseColor("#222222"));
            // Tv.setTextColor(Color.parseColor("#ffffff"));
        } else {
            drawerLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            //Tv.setTextColor(Color.parseColor("#000000"));
        }


        String orien = sp.getString("ORIENTATION", "false");
        if ("1".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        } else if ("2".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if ("3".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        Toast.makeText(HomeActivity.this, "Logout effettuato con successo", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onResume() {
        Load_setting();
        super.onResume();
    }
}

