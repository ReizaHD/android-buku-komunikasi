package com.bintangjuara.bk.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.NoConnectionError;
import com.bintangjuara.bk.R;
import com.bintangjuara.bk.services.RequestBK;
import com.bintangjuara.bk.models.UserData;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private String emailTxt, passTxt;
    private Button loginBtn;
    private SharedPreferences sharedPreferences;
    private ProgressBar pb;
    private ImageView showPass;
    private boolean isPassVisible = false;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        builder = new AlertDialog.Builder(LoginActivity.this,R.style.darkDialogStyle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        try {
            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;

            TextView versionTextView = findViewById(R.id.version);
            versionTextView.setText("V" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_button);
        showPass = findViewById(R.id.ic_show_password);
        sharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE);
        pb = findViewById(R.id.login_pb);

        if(sharedPreferences.getBoolean("isLoggedIn", false)){
            emailTxt = sharedPreferences.getString("email", "");
            passTxt = sharedPreferences.getString("password", "");
            authLogin();
        }

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPassVisible) {
                    // Hide password
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPass.setImageResource(R.drawable.ic_show_password);
                } else {
                    // Show password
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPass.setImageResource(R.drawable.ic_hide_password);
                }
                password.setSelection(password.length()); // Move cursor to the end
                isPassVisible = !isPassVisible;
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailTxt = String.valueOf(email.getText());
                passTxt = String.valueOf(password.getText());

                if(!emailTxt.isBlank() && !passTxt.isBlank()) {
                    Log.d("Username", emailTxt + " " + passTxt);
                    hideKeyboard(view);
                    authLogin();
                } else if (emailTxt.isBlank() || passTxt.isBlank()){
                    builder.setMessage("Email dan password harap diisi")
                            .setTitle("Gagal Login");
                    builder.show();
                } else if(!NetworkUtil.isConnectedToInternet(getApplicationContext())){
                    builder.setMessage("Tidak dapat terhubung ke internet");
                    builder.show();
                }

            }
        });
    }

    void authLogin(){
        pb.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.GONE);

        email.setEnabled(false);
        password.setEnabled(false);
        showPass.setEnabled(false);

        Map<String, String> body = new HashMap<>();
        body.put("email", emailTxt);
        body.put("password", passTxt);

        RequestBK requestBK = RequestBK.getInstance(this);
        requestBK.requestUser(
                emailTxt, passTxt,
                new RequestBK.UserListener() {
                    @Override
                    public void onResponse(UserData userData) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", emailTxt);
                        editor.putString("password", passTxt);
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userData", userData);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Exception error) {
                    if (error instanceof NoConnectionError) {
                        // Handle no internet connection case
                        builder.setMessage("Tidak dapat terhubung ke internet");
                        builder.show();
                    }else{
                        builder.setMessage("Email atau password salah").setTitle("Gagal Login");
                        builder.show();
                    }
                    Log.e("Error", error.toString());
                    pb.setVisibility(View.GONE);
                    loginBtn.setVisibility(View.VISIBLE);
                    email.setEnabled(true);
                    password.setEnabled(true);
                    }
                }
        );
    }


    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static class NetworkUtil {
        public static boolean isConnectedToInternet(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        }
    }
}