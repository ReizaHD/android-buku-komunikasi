package com.example.bk.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.bk.R;

public class EditPasswordActivity extends AppCompatActivity {

    EditText currentPass, newPass, confirmPass;
    ImageView showCurrentPass, showNewPass, showConfirmPass;
    String currentPassTxt, newPassTxt, confirmPassTxt;
    AlertDialog.Builder builder;
    ProgressBar pb;
    Button confirmBtn;

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

        currentPass = findViewById(R.id.current_password);
        newPass = findViewById(R.id.new_password);
        confirmPass = findViewById(R.id.confirm_password);
        showCurrentPass = findViewById(R.id.ic_show_current_password);
        showNewPass = findViewById(R.id.ic_show_new_password);
        showConfirmPass = findViewById(R.id.ic_show_confirm_password);

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
                    Intent intent = new Intent();
                    intent.putExtra("status",1);
                    setResult(Activity.RESULT_OK, intent);  // Send result code and data back
                    finish();
                }
            }
        });

    }

}