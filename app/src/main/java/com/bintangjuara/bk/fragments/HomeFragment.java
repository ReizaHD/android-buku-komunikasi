package com.bintangjuara.bk.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bintangjuara.bk.activities.ViewBeritaActivity;
import com.bintangjuara.bk.adapters.MessageAdapter;
import com.bintangjuara.bk.models.Berita;
import com.bintangjuara.bk.R;
import com.bintangjuara.bk.models.UserData;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    ListView list;
    ProgressBar pb;
    LinearLayout layout;
    TextView profileName;
    UserData userData;


    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = view.findViewById(R.id.list_item);
        pb = view.findViewById(R.id.progress_bar);
        layout = view.findViewById(R.id.home_layout);
        profileName = view.findViewById(R.id.profile);


        if(userData!=null) {
            profileName.setText(userData.getProfile());
        }
        ArrayList<Berita> listBerita = new ArrayList<>();
        listBerita.add(new Berita("Adi","Kegiatan siswa pada hari kamis " +
                "adalah melakukan kunjungan ke salah satu masjid unik yang terletak di " +
                "kabupaten semarang. para siswa didampingi bapak ibu guru berangkat menggunakan " +
                "Bus dari SD Islam Bintang Juara pukul 08.00 pagi .....","16-05-2024","Budi",false));
        listBerita.add(new Berita("Adi","Ayah Bunda, Besok Jumat 17 Mei 2024 diharapkan kakak" +
                " kakak salih dan salihah membawa buku tulis dan tumbler untuk kegiatan outing class di" +
                " sekitar sekolah. Ayah Bunda bisa menjemput Kakak Salih dan Salihah pada jam 17.00 setelah" +
                " kegiatan","16-05-2024","Alan",false));
        listBerita.add(new Berita("Adi","Berita hari Jumat","17-05-2024","Budi", false));
        listBerita.add(new Berita("Adi","Berita hari Senin","20-05-2024","Budi", false));

//        Collections.sort(listBerita, new Comparator<Berita>() {
//            @Override
//            public int compare(Berita pemberitahuan, Berita t1) {
//                return pemberitahuan.getTanggal().compareTo(t1.getTanggal()); // Ascending order
//            }
//        });
        MessageAdapter adapter = new MessageAdapter(getContext(), listBerita);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ViewBeritaActivity.class);
                intent.putExtra("berita", listBerita.get(i));
                startActivity(intent);
            }
        });

        pb.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);

    }
}