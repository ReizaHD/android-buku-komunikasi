package com.bintangjuara.bk.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bintangjuara.bk.activities.EditPasswordActivity;
import com.bintangjuara.bk.activities.MainActivity;
import com.bintangjuara.bk.services.RequestBK;
import com.bintangjuara.bk.activities.LoginActivity;
import com.bintangjuara.bk.R;
import com.bintangjuara.bk.adapters.StudentAdapter;
import com.bintangjuara.bk.models.Student;
import com.bintangjuara.bk.models.UserData;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    UserData userData;
    TextView profileName, email;
    MaterialSwitch notificationSwitch;
    EditText currentPass, newPass, confirmPass;
    ImageView showCurrentPass, showNewPass, showConfirmPass;
    String currentPassTxt, newPassTxt, confirmPassTxt;
    AlertDialog.Builder builder;
    boolean isPassVisible = false;
    EditText focusedEditText;


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

        currentPass = view.findViewById(R.id.current_password);
        newPass = view.findViewById(R.id.new_password);
        confirmPass = view.findViewById(R.id.confirm_password);
        showCurrentPass = view.findViewById(R.id.ic_show_current_password);
        showNewPass = view.findViewById(R.id.ic_show_new_password);
        showConfirmPass = view.findViewById(R.id.ic_show_confirm_password);

        currentPass.setOnFocusChangeListener(focusChangeListener);
        newPass.setOnFocusChangeListener(focusChangeListener);
        confirmPass.setOnFocusChangeListener(focusChangeListener);

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

        builder = new AlertDialog.Builder(getContext(),R.style.darkDialogStyle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        editPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getParentFragmentManager().setFragmentResult("goToEdit", new Bundle());
                currentPassTxt = currentPass.getText().toString();
                newPassTxt = newPass.getText().toString();
                confirmPassTxt = confirmPass.getText().toString();

                if(currentPassTxt.isEmpty() || newPassTxt.isEmpty() || confirmPassTxt.isEmpty()){
                    builder.setTitle("Gagal Ubah Password").setMessage("Semua form harap diisi").show();
                }else if(!confirmPassTxt.equals(newPassTxt)){
                    builder.setTitle("Gagal Ubah Password").setMessage("Konfirmasi kata sandi salah").show();
                }else {
                    Map<String, String> body = new HashMap<>();
                    body.put("username",userData.getName());
                    body.put("user_id",String.valueOf(userData.getId()));
                    body.put("p1",currentPassTxt);
                    body.put("p2",newPassTxt);
                    body.put("p3",confirmPassTxt);
                    editPassword(body);
                }
            }
        });

        ImageView[] showPasswords = {showCurrentPass, showNewPass, showConfirmPass};
        for(ImageView showPass: showPasswords){
            showPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPass();
                }
            });
        }

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

    private void editPassword(Map<String, String> body) {
        // Initializing the AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Password");

        // Set up a layout with a ProgressBar and TextView
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 40);

        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setVisibility(View.VISIBLE); // Initially visible

        TextView messageTextView = new TextView(getContext());
        messageTextView.setText("Please wait...");
        layout.addView(progressBar);
        layout.addView(messageTextView);

        builder.setView(layout);

        // Creating and displaying the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        RequestBK requestBK = RequestBK.getInstance(getContext());
        requestBK.userEditPassword(
                body.get("username"),
                body.get("user_id"),
                body.get("p1"),
                body.get("p2"),
                body.get("p3"),
                new RequestBK.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_session", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        progressBar.setVisibility(View.GONE);
                        messageTextView.setText("Password updated successfully.");
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            getActivity().finish();
                        });
                    }

                    @Override
                    public void onError(Exception error) {
                        Log.e("Change Password", error.toString());
                        progressBar.setVisibility(View.GONE);
                        messageTextView.setText("Failed to update password. Please try again.");
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                        });
                    }
                }
        );
    }

    public void showPass(){
        if (isPassVisible) {
            // Hide password
            currentPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            newPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            confirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showCurrentPass.setImageResource(R.drawable.ic_show_password);
            showNewPass.setImageResource(R.drawable.ic_show_password);
            showConfirmPass.setImageResource(R.drawable.ic_show_password);
        } else {
            // Show password
            currentPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            newPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            confirmPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showCurrentPass.setImageResource(R.drawable.ic_hide_password);
            showNewPass.setImageResource(R.drawable.ic_hide_password);
            showConfirmPass.setImageResource(R.drawable.ic_hide_password);
        }
        isPassVisible = !isPassVisible;
    }

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if(b){
                focusedEditText = (EditText) view;
            }
        }
    };

}