package com.example.bk.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.bk.adapters.MessageAdapter;
import com.example.bk.models.Pemberitahuan;
import com.example.bk.R;
import com.example.bk.models.UserData;
import com.example.bk.fragments.HomeFragment;
import com.example.bk.fragments.NotificationFragment;
import com.example.bk.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    UserData userData;
    Bundle bundle;
    int selectedNavItem;
    Fragment homeFragment, notificationFragment, profileFragment;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
            if(result.getResultCode() == Activity.RESULT_OK){
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });


        Intent intent = getIntent();
        Log.d("State","before");
        if (savedInstanceState != null) {
            // Restore your object from savedInstanceState
            Log.d("State","Not null");
            userData = (UserData) savedInstanceState.getSerializable("userData");
        } else {
            userData = (UserData) intent.getSerializableExtra("userData");
        }
//        userData = (UserData) intent.getSerializableExtra("userData");

        if (userData != null) {
            Log.d("nama", userData.getName());
            Log.d("email", userData.getEmail());
        }


        bundle = new Bundle();
        bundle.putSerializable("userData", userData);

        selectedNavItem = R.id.nav_home;
        homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();

        getSupportFragmentManager().setFragmentResultListener("goToEdit", this, (requestKey, result) -> {
            Log.d("Fragment","data");
            Intent passwordIntent = new Intent(MainActivity.this, EditPasswordActivity.class);
            passwordIntent.putExtra("username",userData.getName());
            passwordIntent.putExtra("id",userData.getId());
            activityResultLauncher.launch(passwordIntent);
        });


        sharedPreferences = getSharedPreferences("login_session", Context.MODE_PRIVATE);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            Log.d("item id", itemId + " " + selectedNavItem);

            if (itemId == R.id.nav_home) {
                if (selectedNavItem == R.id.nav_home) {
                    // Pressed twice, refresh the fragment
                    selectedFragment = new HomeFragment();
                } else {
                    // Use existing fragment if available
                    if (homeFragment == null) {
                        homeFragment = new HomeFragment();
                        homeFragment.setArguments(bundle);
                    }
                    selectedFragment = homeFragment;
                }
            } else if (itemId == R.id.nav_notification) {
                if (selectedNavItem == R.id.nav_notification) {
                    // Pressed twice, refresh the fragment
                    selectedFragment = new NotificationFragment();
                } else {
                    if (notificationFragment == null) {
                        notificationFragment = new NotificationFragment();
                        notificationFragment.setArguments(bundle);
                    }
                    selectedFragment = notificationFragment;
                }
            } else if (itemId == R.id.nav_profile) {
                if (selectedNavItem == R.id.nav_profile) {
                    // Pressed twice, refresh the fragment
                    selectedFragment = new ProfileFragment();
                } else {
                    if (profileFragment == null) {
                        profileFragment = new ProfileFragment();
                        profileFragment.setArguments(bundle);
                    }
                    selectedFragment = profileFragment;
                }
            }

            selectedNavItem = itemId;

            // Replace the fragment
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }

            return true;
        });

    }

}