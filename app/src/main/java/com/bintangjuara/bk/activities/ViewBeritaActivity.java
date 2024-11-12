package com.bintangjuara.bk.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bintangjuara.bk.adapters.SubjectAdapter;
import com.bintangjuara.bk.utilities.CarouselItemDecoration;
import com.bintangjuara.bk.R;
import com.bintangjuara.bk.services.RequestBK;
import com.bintangjuara.bk.adapters.CarouselAdapter;
import com.bintangjuara.bk.models.Berita;
import com.bintangjuara.bk.models.CarouselItem;
import com.bintangjuara.bk.models.Subject;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewBeritaActivity extends AppCompatActivity {

    private RecyclerView listPembelajaran;
    private TextView namaSiswa, kelas, tugasWeekend, catatan, ekstrakurikuler, catatanOrtu, balasan;
    private Button feedbackBtn;
    private LinearLayout feedbackView;
    private MaterialToolbar topBar;
    private AlertDialog alertDialog;
    private RecyclerView carouselRecyclerView;
    private CarouselAdapter carouselAdapter;
    private List<CarouselItem> carouselItemList;
    private int activityResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_berita);
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
        Berita berita = (Berita) intent.getSerializableExtra("berita");

        namaSiswa = findViewById(R.id.nama_siswa);
        kelas = findViewById(R.id.kelas);
        tugasWeekend = findViewById(R.id.tugas_weekend_card);
        catatan = findViewById(R.id.catatan_tambahan_card);
        ekstrakurikuler = findViewById(R.id.ekstrakurikuler_card);
        catatanOrtu = findViewById(R.id.catatan_ortu_card);
        balasan = findViewById(R.id.feedback_card);
        topBar = findViewById(R.id.top_bar);

        namaSiswa.setText(berita.getStudentName());
        kelas.setText(berita.getStudentClass());
        tugasWeekend.setText(berita.getWeekendAssignment());
        catatan.setText(berita.getAdditionalFeedback());
        ekstrakurikuler.setText(berita.getExtracurricular());

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
                    berita.setParentFeedback(inputText);
                    feedbackView.setVisibility(View.VISIBLE);
                    balasan.setText(inputText);
                    Log.d("Feedback", inputText);
                    feedbackDialogEditText.setEnabled(false);
                    submitDialogBtn.setEnabled(false);
                    cancelDialogBtn.setEnabled(false);
                    pb.setVisibility(View.VISIBLE);
                    textBox.setVisibility(View.GONE);
                    feedbackDialogEditText.setText("");
                    insertUserFeedback(String.valueOf(berita.getFeedbackId()), inputText);
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

        if(berita.getParentFeedback() != null){
            if(!berita.getParentFeedback().isEmpty()) {
                feedbackView.setVisibility(View.VISIBLE);
                balasan.setText(berita.getParentFeedback());
                feedbackBtn.setVisibility(View.GONE);
            }
        }

        Log.d("Pelajaran", "Size : "+berita.getSubjects().size());
        for(Subject i : berita.getSubjects()) {
            Log.d("Pelajaran", "mapel : " + i.getSubjectName());
        }

        listPembelajaran.setLayoutManager(new LinearLayoutManager(this));
        SubjectAdapter subjectAdapter = new SubjectAdapter(this, berita.getSubjects());
        listPembelajaran.setAdapter(subjectAdapter);

        carouselRecyclerView = findViewById(R.id.recycler_carousel);
        carouselItemList = new ArrayList<>();
        carouselItemList.add(new CarouselItem("https://www.sekolah-aljannah.com/new/cms/wp-content/uploads/2020/02/IMG-20200213-WA0003-Desy-Pamungkas.jpg", "Slide 1"));
        carouselItemList.add(new CarouselItem("https://klikmu.co/wp-content/uploads/2018/01/WhatsApp-Image-2018-01-30-at-10.01.48.jpeg", "Slide 2"));
        carouselItemList.add(new CarouselItem("https://pwmjateng.com/wp-content/uploads/2024/05/Gambar-WhatsApp-2024-05-18-pukul-10.53.03_b691b0dc-780x470.jpg", "Slide 3"));

        carouselAdapter = new CarouselAdapter(this, carouselItemList);
        carouselAdapter.setOnItemClickListener(new CarouselAdapter.OnItemClickListener() {
            @Override
            public void onClick(ImageView imageView, CarouselItem item) {
                startActivity(new Intent(ViewBeritaActivity.this, ViewImageActivity.class).putExtra("image", item), ActivityOptions.makeSceneTransitionAnimation(ViewBeritaActivity.this, imageView, "image").toBundle());
            }
        });

        // Set up the RecyclerView with horizontal layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        carouselRecyclerView.setLayoutManager(layoutManager);
        carouselRecyclerView.setAdapter(carouselAdapter);

        int spacing = 50; // Adjust this value as needed
        carouselRecyclerView.addItemDecoration(new CarouselItemDecoration(spacing));

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(carouselRecyclerView);

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Log.d("ID", String.valueOf(berita.getFeedbackId()));
        requestReadBerita(String.valueOf(berita.getFeedbackId()));

    }

    private void requestReadBerita(String id){
        RequestBK requestBK = RequestBK.getInstance(this);
        requestBK.beritaRead(id, new RequestBK.ResponseListener() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                String status="";
                try {
                    JSONObject obj = new JSONObject(response);
                    status = obj.getString("status");
                } catch (JSONException e) {
                    e.toString();
                }
                if(status.equals("success"))
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