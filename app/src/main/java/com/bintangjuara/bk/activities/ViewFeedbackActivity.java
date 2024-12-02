package com.bintangjuara.bk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bintangjuara.bk.adapters.SubjectAdapter;
import com.bintangjuara.bk.models.Feedback;
import com.bintangjuara.bk.R;
import com.bintangjuara.bk.services.RequestBK;
import com.bintangjuara.bk.models.Subject;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewFeedbackActivity extends AppCompatActivity {

    private RecyclerView listPembelajaran;
    private TextView namaSiswa, kelas, tugasWeekend, catatan, ekstrakurikuler, catatanOrtu, balasan;
    private Button feedbackBtn;
    private LinearLayout feedbackView;
    private MaterialToolbar topBar;
    private AlertDialog alertDialog;
    private int activityResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.yellow_light));


        listPembelajaran = findViewById(R.id.list_catatan);
        Intent intent = getIntent();
        Feedback announcement = (Feedback) intent.getSerializableExtra("berita");

        namaSiswa = findViewById(R.id.nama_siswa);
        kelas = findViewById(R.id.kelas);
        tugasWeekend = findViewById(R.id.tugas_weekend_card);
        catatan = findViewById(R.id.catatan_tambahan_card);
        ekstrakurikuler = findViewById(R.id.ekstrakurikuler_card);
        catatanOrtu = findViewById(R.id.catatan_ortu_card);
        balasan = findViewById(R.id.feedback_card);
        topBar = findViewById(R.id.top_bar);

        namaSiswa.setText(announcement.getStudentName());
        kelas.setText(announcement.getStudentClass());
        tugasWeekend.setText(announcement.getWeekendAssignment());
        catatan.setText(announcement.getAdditionalFeedback());
        ekstrakurikuler.setText(announcement.getExtracurricular());
        topBar.setSubtitle(announcement.getStrDate());

        feedbackView = findViewById(R.id.feedback);
        feedbackBtn = findViewById(R.id.feedback_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.darkDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.feedback_alert_dialog,null);

        builder.setView(dialogView);

        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        FrameLayout textBox = dialogView.findViewById(R.id.text_box);
        ProgressBar pb = dialogView.findViewById(R.id.progress_bar);
        EditText feedbackDialogEditText = dialogView.findViewById(R.id.feedback_edit_text);
        Button submitDialogBtn = dialogView.findViewById(R.id.btnSubmit);
        Button cancelDialogBtn = dialogView.findViewById(R.id.btnCancel);

        submitDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = feedbackDialogEditText.getText().toString();
                if(!inputText.isEmpty()) {
                    announcement.setParentFeedback(inputText);
                    feedbackView.setVisibility(View.VISIBLE);
                    balasan.setText(inputText);
                    Log.d("Feedback", inputText);
                    feedbackDialogEditText.setEnabled(false);
                    submitDialogBtn.setEnabled(false);
                    cancelDialogBtn.setEnabled(false);
                    pb.setVisibility(View.VISIBLE);
                    textBox.setVisibility(View.GONE);
                    feedbackDialogEditText.setText("");
                    insertUserFeedback(String.valueOf(announcement.getFeedbackId()), inputText);
                }
            }
        });

        cancelDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        if(announcement.getParentFeedback() != null){
            if(!announcement.getParentFeedback().isEmpty()) {
                feedbackView.setVisibility(View.VISIBLE);
                balasan.setText(announcement.getParentFeedback());
                feedbackBtn.setVisibility(View.GONE);
            }
        }

        Log.d("Pelajaran", "Size : "+ announcement.getSubjects().size());
        for(Subject i : announcement.getSubjects()) {
            Log.d("Pelajaran", "mapel : " + i.getSubjectName());
        }

        listPembelajaran.setLayoutManager(new LinearLayoutManager(this));
        SubjectAdapter subjectAdapter = new SubjectAdapter(this, announcement.getSubjects());
        listPembelajaran.setAdapter(subjectAdapter);

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Log.d("ID", String.valueOf(announcement.getAnnouncementId()));
        requestReadBerita(String.valueOf(announcement.getAnnouncementId()));

    }

    private void requestReadBerita(String id){
        RequestBK requestBK = RequestBK.getInstance(this);
        requestBK.announcementRead(id, new RequestBK.ResponseListener() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                boolean status= false;
                try {
                    JSONObject obj = new JSONObject(response);
                    status = obj.getBoolean("success");
                } catch (JSONException e) {
                    e.toString();
                }
                if(status)
                    activityResult = 1;
            }

            @Override
            public void onError(Exception error) {
                Log.e("Response", error.toString());
            }
        });
    }

    private void insertUserFeedback(String id, String msg){
        RequestBK requestBK = RequestBK.getInstance(this);
        requestBK.insertFeedback(msg, id, new RequestBK.ResponseListener() {
            @Override
            public void onResponse(String response) {
                activityResult = 1;
                alertDialog.dismiss();
                feedbackBtn.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception error) {
                Log.e("Error", error.toString());
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public void finish() {
        setResult(activityResult);
        super.finish();
    }
}