package com.example.bk.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bk.R;
import com.example.bk.models.UserData;

import org.json.JSONException;
import org.json.JSONObject;

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
        builder = new AlertDialog.Builder(LoginActivity.this,R.style.darkDialogStyle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

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

                if(!emailTxt.isBlank() || !passTxt.isBlank()) {
                    Log.d("Username", emailTxt + " " + passTxt);
                    hideKeyboard(view);
                    authLogin();
                } else {
                    builder.setMessage("Email dan password harap diisi")
                            .setTitle("Gagal Login");
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://siakad.bintangjuara.sch.id/rest_mobile/rest_user";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            String data = json.getString("data");
                            UserData userData = new UserData(data);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", emailTxt);
                            editor.putString("password", passTxt);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userData", userData);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                pb.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
                builder.setMessage("Email atau password salah").setTitle("Gagal Login");
                builder.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailTxt);
                params.put("password", passTxt);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-API-KEY", "sso-ikitas_1993smb11");
                return headers;
            }
        };

        requestQueue.add(stringRequest);

    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}