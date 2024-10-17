package com.bintangjuara.bk.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.bintangjuara.bk.WebSocketService;
import com.bintangjuara.bk.R;
import com.bintangjuara.bk.models.UserData;
import com.bintangjuara.bk.fragments.HomeFragment;
import com.bintangjuara.bk.fragments.NotificationFragment;
import com.bintangjuara.bk.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

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

        Intent intent = getIntent();
        userData = (UserData) intent.getSerializableExtra("userData");
        sharedPreferences = getSharedPreferences("login_session", Context.MODE_PRIVATE);

        if (userData != null) {
            Log.d("nama", userData.getName());
            Log.d("email", userData.getEmail());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert");
            builder.setMessage("This is an alert dialog. Click OK to dismiss.");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Action when OK button is clicked
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();

            // Set the onDismissListener
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    // Action to be taken after the dialog is dismissed
                    Log.d("AlertDialog", "Dialog dismissed. Perform post-dismiss actions here.");
                    // Add more code here to execute after the dialog is dismissed
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            });

            // Show the dialog
            alertDialog.show();
        }

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("MainActivity", "Fetching FCM registration token failed", task.getException());
                return;
            }

            // Get the FCM token
            String token = task.getResult();
            Log.d("MainActivity", "FCM Token: " + token);
//            Toast.makeText(MainActivity.this, "FCM Token: " + token, Toast.LENGTH_SHORT).show();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        100);
            }
        }else {
            enableNotifications();
        }

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
            if(result.getResultCode() == Activity.RESULT_OK){
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        Intent serviceIntent = new Intent(this, WebSocketService.class);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            // Check if the permission request was for notifications
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable notifications
                enableNotifications();
            }
        }
    }

    private void enableNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic("Berita");
        sharedPreferences.edit().putBoolean("enableNotification", true).apply();
    }

    private void handlePermissionDenied() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Berita");
        sharedPreferences.edit().putBoolean("enableNotification", false).apply();
    }

}