package com.bintangjuara.bk.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bintangjuara.bk.R;
import com.bintangjuara.bk.RequestBK;
import com.bintangjuara.bk.adapters.MessageAdapter;
import com.bintangjuara.bk.models.Berita;
import com.bintangjuara.bk.models.Pelajaran;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ViewBeritaActivity extends AppCompatActivity {

    RecyclerView listPembelajaran;
    TextView namaSiswa, kelas, tugasWeekend, catatan, ekstrakurikuler, catatanOrtu, balasan;
    Button feedbackBtn;
    LinearLayout feedbackView;
    MaterialToolbar topBar;
    AlertDialog alertDialog;

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

        namaSiswa.setText(berita.getNamaSiswa());
        kelas.setText(berita.getKelas());
        tugasWeekend.setText(berita.getTugasWeekend());
        catatan.setText(berita.getCatatan());
        ekstrakurikuler.setText(berita.getEkstrakurikuler());
        catatanOrtu.setText(berita.getCatatanOrtu());

        feedbackView = findViewById(R.id.feedback);
        feedbackBtn = findViewById(R.id.feedback_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.feedback_alert_dialog,null);

        builder.setView(dialogView);

        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        EditText feedbackDialogEditText = dialogView.findViewById(R.id.feedback_edit_text);
        Button submitDialogBtn = dialogView.findViewById(R.id.btnSubmit);
        Button cancelDialogBtn = dialogView.findViewById(R.id.btnCancel);

        submitDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = feedbackDialogEditText.getText().toString();
                if(!inputText.isEmpty()) {
                    berita.setBalasan(inputText);
                    feedbackView.setVisibility(View.VISIBLE);
                    balasan.setText(inputText);
                    Log.d("Feedback", inputText);
                    feedbackDialogEditText.setEnabled(false);
                    submitDialogBtn.setEnabled(false);
                    cancelDialogBtn.setEnabled(false);
                    feedbackDialogEditText.setText("");
                    insertUserFeedback(berita.getId(), inputText);
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

        if(berita.getBalasan() != null){
            if(!berita.getBalasan().isEmpty()) {
                feedbackView.setVisibility(View.VISIBLE);
                balasan.setText(berita.getBalasan());
            }
        }

        Log.d("Pelajaran", "Size : "+berita.getPembelajaran().size());
        for(Pelajaran i : berita.getPembelajaran()) {
            Log.d("Pelajaran", "mapel : " + i.getMataPelajaran());
        }

        listPembelajaran.setLayoutManager(new LinearLayoutManager(this));
        CatatanAdapter catatanAdapter = new CatatanAdapter(this, berita.getPembelajaran());
        listPembelajaran.setAdapter(catatanAdapter);

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Log.d("ID", berita.getId());
        requestReadBerita(berita.getId());

    }

    public class CatatanAdapter extends RecyclerView.Adapter<CatatanAdapter.CatatanViewHolder> {

        private Context ctx;
        private ArrayList<Pelajaran> pembelajaran;

        public CatatanAdapter(Context ctx, ArrayList<Pelajaran> pembelajaran) {
            this.ctx = ctx;
            this.pembelajaran = pembelajaran;
        }

        @NonNull
        @Override
        public CatatanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate the layout for each item
            View view = LayoutInflater.from(ctx).inflate(R.layout.list_catatan, parent, false);
            return new CatatanViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CatatanViewHolder holder, int position) {
            // Bind the data to the views
            Pelajaran pelajaran = pembelajaran.get(position);
            holder.namaMapel.setText(pelajaran.getMataPelajaran());
            holder.catatan.setText(pelajaran.getIsi());
            if(position == pembelajaran.size()-1){
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.card.getLayoutParams();
                params.setMargins(0,0,0,0);
            }
        }

        @Override
        public int getItemCount() {
            return pembelajaran.size();
        }

        // ViewHolder class to hold the views for each item
        public class CatatanViewHolder extends RecyclerView.ViewHolder {
            TextView namaMapel;
            TextView catatan;
            ConstraintLayout card;

            public CatatanViewHolder(@NonNull View itemView) {
                super(itemView);
                namaMapel = itemView.findViewById(R.id.nama_mapel);
                catatan = itemView.findViewById(R.id.catatan);
                card = itemView.findViewById(R.id.main);
            }
        }
    }

    private void requestReadBerita(String id){
        RequestBK requestBK = RequestBK.getInstance(this);
        requestBK.beritaRead(id, new RequestBK.ResponseListener() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
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
                Log.d("response", response);
                alertDialog.dismiss();
            }

            @Override
            public void onError(Exception error) {
                Log.e("Error", error.toString());
                alertDialog.dismiss();
            }
        });

    }

}