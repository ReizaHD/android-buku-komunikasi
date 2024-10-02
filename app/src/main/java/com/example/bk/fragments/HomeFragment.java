package com.example.bk.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bk.adapters.MessageAdapter;
import com.example.bk.models.Pemberitahuan;
import com.example.bk.R;
import com.example.bk.models.UserData;

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
        ArrayList<Pemberitahuan> listPemberitahuan = new ArrayList<>();
        listPemberitahuan.add(new Pemberitahuan("Adi","Kegiatan siswa pada hari kamis " +
                "adalah melakukan kunjungan ke salah satu masjid unik yang terletak di " +
                "kabupaten semarang. para siswa didampingi bapak ibu guru berangkat menggunakan " +
                "Bus dari SD Islam Bintang Juara pukul 08.00 pagi .....","Kamis, 16 Mei 2024","Budi",false));
        listPemberitahuan.add(new Pemberitahuan("Adi","Ayah Bunda, Besok Jumat 17 Mei 2024 diharapkan kakak" +
                " kakak salih dan salihah membawa buku tulis dan tumbler untuk kegiatan outing class di" +
                " sekitar sekolah. Ayah Bunda bisa menjemput Kakak Salih dan Salihah pada jam 17.00 setelah" +
                " kegiatan","Kamis, 16 Mei 2024","Alan",false));
        listPemberitahuan.add(new Pemberitahuan("Adi","Pemberitahuan hari selasa","21-09-2024","Budi", false));
        MessageAdapter adapter = new MessageAdapter(getContext(), listPemberitahuan);
        list.setAdapter(adapter);
        pb.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);

    }
}