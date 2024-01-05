package com.example.dreamjournal;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.dreamjournal.databinding.ActivityMainBinding;
import com.example.dreamjournal.ui.calendar.CalendarFragment;
import com.example.dreamjournal.ui.dashboard.DashboardFragment;
import com.example.dreamjournal.ui.favorites.FavoritesFragment;
import com.example.dreamjournal.ui.home.HomeFragment;
import com.example.dreamjournal.ui.sleeptimer.SleepTimerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    final Fragment fragment1 = new HomeFragment();

    final Fragment fragment2 = new FavoritesFragment();

    final Fragment fragment3 = new DashboardFragment();
    final Fragment fragment4 = new SleepTimerFragment();
    final Fragment fragment5 = new CalendarFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
//
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_calendar)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main, fragment1, "1").commit();



    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.navigation_home) {
                fm.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                return true;

            } else if (item.getItemId() == R.id.navigation_favorites) {
                fm.beginTransaction().hide(active).show(fragment2).commit();
                active = fragment2;
                return true;

            } else if (item.getItemId() == R.id.navigation_dashboard) {
                fm.beginTransaction().hide(active).show(fragment3).commit();
                active = fragment3;
                return true;

            } else if (item.getItemId() == R.id.navigation_notifications) {
                fm.beginTransaction().hide(active).show(fragment4).commit();
                active = fragment4;
                return true;
            } else if (item.getItemId() == R.id.navigation_calendar) {
                fm.beginTransaction().hide(active).show(fragment5).commit();
                active = fragment5;
                return true;
            }
            return false;
        }



    };


}