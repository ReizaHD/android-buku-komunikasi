package com.bintangjuara.bk.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bintangjuara.bk.activities.LoginActivity;
import com.bintangjuara.bk.R;
import com.bintangjuara.bk.models.UserData;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.messaging.FirebaseMessaging;

public class ProfileFragment extends Fragment {

    UserData userData;
    TextView profileName, email;
    MaterialSwitch notificationSwitch;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userData = (UserData) getArguments().getSerializable("userData");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("New Fragment","True");
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_session",MODE_PRIVATE);
        boolean enableNotification = sharedPreferences.getBoolean("enableNotification",false);
        Log.d("enableNotification", String.valueOf(enableNotification));
        Button logoutBtn = view.findViewById(R.id.logout_btn);
        Button editPassBtn = view.findViewById(R.id.edit_password_btn);

        profileName = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.email);
        notificationSwitch = view.findViewById(R.id.notification_switch);

        notificationSwitch.setChecked(enableNotification);

        profileName.setText(userData.getProfile());
        email.setText(userData.getEmail());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();  // Clears all data
                editor.apply();
                Log.d("Button","Logout");
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();

            }
        });
        editPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().setFragmentResult("goToEdit", new Bundle());
            }
        });

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    FirebaseMessaging.getInstance().subscribeToTopic("Berita");
                    sharedPreferences.edit().putBoolean("enableNotification", true).apply();
                }else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Berita");
                    sharedPreferences.edit().putBoolean("enableNotification", false).apply();
                }
            }
        });
    }
}