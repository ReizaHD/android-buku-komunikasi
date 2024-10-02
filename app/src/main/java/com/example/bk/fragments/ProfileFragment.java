package com.example.bk.fragments;

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

import com.example.bk.activities.EditPasswordActivity;
import com.example.bk.activities.LoginActivity;
import com.example.bk.R;
import com.example.bk.models.UserData;

public class ProfileFragment extends Fragment {

    UserData userData;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userData = (UserData) getArguments().getSerializable("userData");
        } else if(savedInstanceState != null) {
            userData = (UserData) savedInstanceState.getSerializable("userData");
            Log.d("USER DATA", "IS NOT NULL");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_session",MODE_PRIVATE);

        Button logoutBtn = view.findViewById(R.id.logout_btn);
        Button editPassBtn = view.findViewById(R.id.edit_password_btn);
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
                Bundle result = new Bundle();
                result.putString("data_key", "Hello from Fragment");
                getParentFragmentManager().setFragmentResult("goToEdit", new Bundle());

            }
        });
    }
}