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
import androidx.fragment.app.FragmentTransaction;

import com.bintangjuara.bk.R;
import com.bintangjuara.bk.fragments.AnakFragment;
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
    Fragment homeFragment, notificationFragment, profileFragment, anakFragment;
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
            }else {
                enableNotifications();
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

        bundle = new Bundle();
        bundle.putSerializable("userData", userData);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        homeFragment = new HomeFragment();
        selectedNavItem = R.id.nav_home;
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

        getSupportFragmentManager().setFragmentResultListener("filter", this, (requestKey, result) -> {
            Log.d("Fragment","Search NAMA");
            String id = result.getString("user_id");
            Log.d("ID", id);
            Bundle filterBundle = new Bundle();
            filterBundle.putString("id", id);
            notificationFragment = replaceFragment(notificationFragment, new NotificationFragment(), "NotificationFragment", filterBundle);
            bottomNavigationView.setSelectedItemId(R.id.nav_notification);
        });


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == selectedNavItem) {
                refreshFragment(itemId); // Refresh if already selected
            } else {
                if (itemId == R.id.nav_home) {
                    if (homeFragment == null) {
                        homeFragment = new HomeFragment();
                        homeFragment.setArguments(bundle);
                        addFragment(homeFragment, "HomeFragment");
                    } else {
                        showFragment(homeFragment);
                    }
                } else if (itemId == R.id.nav_notification) {
                    if (notificationFragment == null) {
                        notificationFragment = new NotificationFragment();
                        notificationFragment.setArguments(bundle);
                        addFragment(notificationFragment, "NotificationFragment");
                    } else {
                        showFragment(notificationFragment);
                    }
                } else if(itemId == R.id.nav_students){
                    if (anakFragment == null) {
                        anakFragment = new AnakFragment();
                        anakFragment.setArguments(bundle);
                        addFragment(anakFragment,"AnakFragment");
                    } else {
                        showFragment(anakFragment);
                    }
                } else if (itemId == R.id.nav_profile) {
                    if (profileFragment == null) {
                        profileFragment = new ProfileFragment();
                        profileFragment.setArguments(bundle);
                        addFragment(profileFragment,"ProfileFragment");
                    } else {
                        showFragment(profileFragment);
                    }
                }
            }
            selectedNavItem = itemId; // Update last selected item
            return true;
        });

    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragments(transaction)
                .show(fragment)
                .commit();
    }


    private void addFragment(Fragment fragment,String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragments(transaction)
                .add(R.id.fragment_container, fragment, tag)
                .show(fragment)
                .commit();
    }

    private Fragment replaceFragment (Fragment remove, Fragment add, String tag, Bundle args){
        if(getSupportFragmentManager().findFragmentByTag(tag) != null) {
            removeFragment(remove);
        }
        if(args!=null){
            add.setArguments(args);
        }
        addFragment(add, tag);
        return add;
    }

    private void removeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().remove(fragment);
    }

    private FragmentTransaction hideAllFragments(FragmentTransaction transaction) {
        if (homeFragment != null) transaction.hide(homeFragment);
        if (notificationFragment != null) transaction.hide(notificationFragment);
        if (profileFragment != null) transaction.hide(profileFragment);
        if (anakFragment != null) transaction.hide(anakFragment);
        return transaction;
    }

    private void refreshFragment(int itemId) {
        Fragment fragmentToRefresh = null;
        String tag = "";
        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        if (itemId == R.id.nav_home) {
            transaction.remove(homeFragment).commit();
            fragmentToRefresh = new HomeFragment();
            homeFragment = fragmentToRefresh;
            tag = "HomeFragment";
        } else if (itemId == R.id.nav_notification) {
            transaction.remove(notificationFragment).commit();
            fragmentToRefresh = new NotificationFragment();
            notificationFragment = fragmentToRefresh;
            tag = "NotificationFragment";
        } else if (itemId == R.id.nav_students){
            transaction.remove(anakFragment).commit();
            fragmentToRefresh = new AnakFragment();
            anakFragment = fragmentToRefresh;
            tag = "ProfileFragment";
        } else if (itemId == R.id.nav_profile) {
            transaction.remove(profileFragment).commit();
            fragmentToRefresh = new ProfileFragment();
            profileFragment = fragmentToRefresh;
            tag = "ProfileFragment";
        }
        fragmentToRefresh.setArguments(bundle);
        hideAllFragments(getSupportFragmentManager().beginTransaction())
                .add(R.id.fragment_container, fragmentToRefresh, tag)
                .show(fragmentToRefresh)
                .commit();
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