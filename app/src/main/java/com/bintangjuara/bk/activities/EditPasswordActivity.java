package com.bintangjuara.bk.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bintangjuara.bk.R;

import java.util.HashMap;
import java.util.Map;

public class EditPasswordActivity extends AppCompatActivity {

    EditText currentPass, newPass, confirmPass;
    ImageView showCurrentPass, showNewPass, showConfirmPass;
    String currentPassTxt, newPassTxt, confirmPassTxt;
    AlertDialog.Builder builder;
    ProgressBar pb;
    Button confirmBtn;
    String userName, userId;
    boolean isPassVisible = false;
    EditText focusedEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if(intent!=null){
            userName = intent.getStringExtra("username");
            userId = String.valueOf(intent.getIntExtra("id",0));
        }

        currentPass = findViewById(R.id.current_password);
        newPass = findViewById(R.id.new_password);
        confirmPass = findViewById(R.id.confirm_password);
        showCurrentPass = findViewById(R.id.ic_show_current_password);
        showNewPass = findViewById(R.id.ic_show_new_password);
        showConfirmPass = findViewById(R.id.ic_show_confirm_password);

        currentPass.setOnFocusChangeListener(focusChangeListener);
        newPass.setOnFocusChangeListener(focusChangeListener);
        confirmPass.setOnFocusChangeListener(focusChangeListener);

        pb = findViewById(R.id.edit_password_btn);
        confirmBtn = findViewById(R.id.confirm_button);

        builder = new AlertDialog.Builder(EditPasswordActivity.this,R.style.darkDialogStyle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPassTxt = currentPass.getText().toString();
                newPassTxt = newPass.getText().toString();
                confirmPassTxt = confirmPass.getText().toString();

                if(currentPassTxt.isEmpty() || newPassTxt.isEmpty() || confirmPassTxt.isEmpty()){
                    builder.setTitle("Gagal Ubah Password").setMessage("Semua form harap diisi").show();
                }else if(!confirmPassTxt.equals(newPassTxt)){
                    builder.setTitle("Gagal Ubah Password").setMessage("Konfirmasi kata sandi salah").show();
                }else {
                    editPassword();
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

    }

    public void editPassword(){
        pb.setVisibility(View.VISIBLE);
        confirmBtn.setVisibility(View.GONE);

        currentPass.setEnabled(false);
        newPass.setEnabled(false);
        confirmPass.setEnabled(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://siakad.bintangjuara.sch.id/rest_mobile/rest_user/password";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        SharedPreferences sharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                pb.setVisibility(View.GONE);
                confirmBtn.setVisibility(View.VISIBLE);
                builder.setMessage("Gagal ubah kata sandi");
                builder.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", currentPassTxt);
                params.put("password1", newPassTxt);
                params.put("password2", confirmPassTxt);
                params.put("username", userName);
                params.put("id", userId);
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