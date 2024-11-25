package com.bintangjuara.bk.activities;

import android.Manifest;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.bintangjuara.bk.R;
import com.bintangjuara.bk.fragments.AnakFragment;
import com.bintangjuara.bk.models.Announcement;
import com.bintangjuara.bk.models.Feedback;
import com.bintangjuara.bk.models.UserData;
import com.bintangjuara.bk.fragments.HomeFragment;
import com.bintangjuara.bk.fragments.HistoryFragment;
import com.bintangjuara.bk.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private final String HOME_TAG = "HomeFragment";
    private final String HISTORY_TAG = "HistoryFragment";
    private final String STUDENT_TAG = "StudentFragment";
    private final String PROFILE_TAG = "ProfileFragment";
    private final int FRAGMENT_ID = R.id.fragment_container;

    SharedPreferences sharedPreferences;
    UserData userData;
    Bundle bundle;
    int selectedNavItem;
    Fragment homeFragment, historyFragment, profileFragment, anakFragment;
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


        bundle = new Bundle();
        bundle.putSerializable("userData", userData);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
            if(result.getResultCode() == 1){
                removeAllFragments();
                bottomNavigationView.setSelectedItemId(selectedNavItem);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == selectedNavItem){
                refreshFragment(itemId);
            }else {
                if(itemId == R.id.nav_home) {
                    addFragment(new HomeFragment(), HOME_TAG, bundle);
                    showOnlyFragment(HOME_TAG);
                } else if (itemId == R.id.nav_history) {
                    addFragment(new HistoryFragment(), HISTORY_TAG, bundle);
                    showOnlyFragment(HISTORY_TAG);
                } else if (itemId == R.id.nav_students) {
                    addFragment(new AnakFragment(), STUDENT_TAG, bundle);
                    showOnlyFragment(STUDENT_TAG);
                } else if (itemId == R.id.nav_profile) {
                    addFragment(new ProfileFragment(), PROFILE_TAG, bundle);
                    showOnlyFragment(PROFILE_TAG);
                }
            }
            selectedNavItem = itemId; // Update last selected item
            return true;
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        getSupportFragmentManager().setFragmentResultListener("filter", this, (requestKey, result) -> {
            Log.d("Fragment","Search NAMA");
            String id = result.getString("user_id");
            Log.d("ID", id);
            result.putSerializable("userData", userData);
            replaceFragment(new HistoryFragment(), HISTORY_TAG, result);
            bottomNavigationView.setSelectedItemId(R.id.nav_history);
        });

        getSupportFragmentManager().setFragmentResultListener("view_announcement", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                selectedNavItem = bottomNavigationView.getSelectedItemId();
                Intent intent;
                Announcement announcement = (Announcement) result.getSerializable("announcement");
                if(announcement instanceof Feedback) {
                    Feedback feedback = (Feedback) announcement;
                    intent = new Intent(MainActivity.this, ViewFeedbackActivity.class);
                    intent.putExtra("berita", feedback);
                }else{
                    intent = new Intent(MainActivity.this, ViewAnnouncementActivity.class);
                    intent.putExtra("berita", announcement);
                }
                activityResultLauncher.launch(intent);
            }
        });

    }

    private void addFragment(Fragment frag, String tag, Bundle arg){
        frag.setArguments(arg);
        FragmentManager manager = getSupportFragmentManager();
        Fragment existingFragment = manager.findFragmentByTag(tag);

        // Only add the fragment if it doesn’t already exist
        if (existingFragment == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(FRAGMENT_ID, frag, tag).commitNow();
        }
    }

    private void replaceFragment (Fragment add, String tag, Bundle args){
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragmentToRemove = manager.findFragmentByTag(tag);
        if(fragmentToRemove != null) {
            removeFragment(fragmentToRemove);
        }
        addFragment(add, tag, args);
    }

    private void removeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().remove(fragment).commitNow();
    }

    private void showOnlyFragment(String tag){
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = null;
        fragment = manager.findFragmentByTag(tag);
        if (fragment != null) {
            hideAllFragments().show(fragment).commitNow();
        } else {
            Log.e("showOnlyFragment", "Fragment with tag " + tag + " not found.");
        }
    }

    private FragmentTransaction hideAllFragments(){
        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        System.out.println(manager.getFragments());
        for(Fragment fragment : manager.getFragments()){
            transaction.hide(fragment);
        }
        return transaction;
    }

    private void refreshFragment(int itemId){
        if(itemId == R.id.nav_home){
            replaceFragment(new HomeFragment(),HOME_TAG, bundle);
        } else if (itemId == R.id.nav_history) {
            replaceFragment(new HistoryFragment(),HISTORY_TAG, bundle);
        } else if (itemId == R.id.nav_students) {
            replaceFragment(new AnakFragment(), STUDENT_TAG, bundle);
        } else if (itemId == R.id.nav_profile) {
            replaceFragment(new ProfileFragment(), PROFILE_TAG, bundle);
        }
    }

    private void removeOtherFragments(String tag){
        FragmentManager manager = getSupportFragmentManager();
        Fragment currentFragment = manager.findFragmentByTag(tag);
        for(Fragment fragment : manager.getFragments()){
            if(!fragment.equals(currentFragment)){
                removeFragment(fragment);
            }
        }
    }

    private void removeAllFragments(){
        FragmentManager manager = getSupportFragmentManager();
        for(Fragment fragment : manager.getFragments()){
            removeFragment(fragment);
        }
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