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

    ArrayList<Pemberitahuan> listPemberitahuan;
    MessageAdapter adapter;
    ListView listView;
    int resumeCode;
    SharedPreferences sharedPreferences;
    UserData userData;
    Bundle bundle;

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        Log.d("time","now");
//        outState.putSerializable("userData", userData);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        Log.d("time","now");
//        userData = (UserData) savedInstanceState.getSerializable("userData");
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("result","Balik");
//        HomeFragment homeFragment = new HomeFragment();
//        homeFragment.setArguments(bundle);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, homeFragment).commit();
    }

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

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();

        getSupportFragmentManager().setFragmentResultListener("goToEdit", this, (requestKey, result) -> {

            Log.d("Fragment","data");
            startActivityForResult(new Intent(MainActivity.this, EditPasswordActivity.class), 0);
        });


        sharedPreferences = getSharedPreferences("login_session", Context.MODE_PRIVATE);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                selectedFragment.setArguments(bundle);
            } else if (itemId == R.id.nav_notification) {
                selectedFragment = new NotificationFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }
            if (selectedFragment != null)
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode== Activity.RESULT_OK){
                Fragment selectedFragment = new HomeFragment();
                selectedFragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
        }
    }


}