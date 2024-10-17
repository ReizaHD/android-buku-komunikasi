package com.bintangjuara.bk.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bintangjuara.bk.R;
import com.bintangjuara.bk.models.Berita;
import com.bintangjuara.bk.models.Pelajaran;

import java.util.ArrayList;

public class ViewBeritaActivity extends AppCompatActivity {

    RecyclerView listPembelajaran;
    TextView namaSiswa, kelas, tugasWeekend, catatan, ekstrakurikuler, catatanOrtu;

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
        listPembelajaran = findViewById(R.id.list_catatan);
        Intent intent = getIntent();
        Berita berita = (Berita) intent.getSerializableExtra("berita");

        namaSiswa = findViewById(R.id.nama_siswa);
        kelas = findViewById(R.id.kelas);
        tugasWeekend = findViewById(R.id.tugas_weekend_card);
        catatan = findViewById(R.id.catatan_tambahan_card);
        ekstrakurikuler = findViewById(R.id.ekstrakurikuler_card);
        catatanOrtu = findViewById(R.id.catatan_ortu_card);

        namaSiswa.setText(berita.getNamaSiswa());
        kelas.setText(berita.getKelas());
        tugasWeekend.setText(berita.getTugasWeekend());
        catatan.setText(berita.getCatatan());
        ekstrakurikuler.setText(berita.getEkstrakurikuler());
        catatanOrtu.setText(berita.getCatatanOrtu());

        Log.d("Pelajaran", "Size : "+berita.getPembelajaran().size());
        for(Pelajaran i : berita.getPembelajaran()) {
            Log.d("Pelajaran", "mapel : " + i.getMataPelajaran());
        }

        listPembelajaran.setLayoutManager(new LinearLayoutManager(this));
        CatatanAdapter catatanAdapter = new CatatanAdapter(this, berita.getPembelajaran());
        listPembelajaran.setAdapter(catatanAdapter);

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

//    public class CatatanAdapter extends BaseAdapter {
//
//        private Context ctx;
//        private LayoutInflater inflater;
//        private ArrayList<Pelajaran> pembelajaran;
//
//        public CatatanAdapter(Context ctx, ArrayList<Pelajaran> pembelajaran) {
//            this.ctx = ctx;
//            this.inflater = LayoutInflater.from(ctx);
//            this.pembelajaran = pembelajaran;
//            Log.d("Pembelajaran", "size"+pembelajaran.size());
//        }
//
//        @Override
//        public int getCount() {
//            return pembelajaran.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            Pelajaran pelajaran = pembelajaran.get(i);
//            Log.d("Pembelajaran", ""+i);
//            view = inflater.inflate(R.layout.list_catatan, null);
//            TextView namaMapel = view.findViewById(R.id.nama_mapel);
//            TextView catatan = view.findViewById(R.id.catatan);
//
//            namaMapel.setText(pelajaran.getMataPelajaran());
//            catatan.setText(pelajaran.getIsi());
//
//            return view;
//        }
//    }


}