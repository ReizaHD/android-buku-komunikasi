package com.bintangjuara.bk.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bintangjuara.bk.R;
import com.bintangjuara.bk.adapters.MessageAdapter;
import com.bintangjuara.bk.models.Berita;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationFragment extends Fragment {

    ListView list;
    ProgressBar pb;
    EditText searchBar;
    SwipeRefreshLayout refreshLayout;


    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("New Fragment","True");
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = view.findViewById(R.id.list_item);
        pb = view.findViewById(R.id.progress_bar);
        searchBar = view.findViewById(R.id.search_bar);
        refreshLayout = view.findViewById(R.id.refresh);

        ArrayList<Berita> listBerita = new ArrayList<>();
        listBerita.add(new Berita("Adi","Kegiatan siswa pada hari kamis " +
                "adalah melakukan kunjungan ke salah satu masjid unik yang terletak di " +
                "kabupaten semarang. para siswa didampingi bapak ibu guru berangkat menggunakan " +
                "Bus dari SD Islam Bintang Juara pukul 08.00 pagi .....","16-05-2024","Budi",false));
        listBerita.add(new Berita("Adi","Ayah Bunda, Besok Jumat 17 Mei 2024 diharapkan kakak" +
                " kakak salih dan salihah membawa buku tulis dan tumbler untuk kegiatan outing class di" +
                " sekitar sekolah. Ayah Bunda bisa menjemput Kakak Salih dan Salihah pada jam 17.00 setelah" +
                " kegiatan","16-05-2024","Alan", false));
        listBerita.add(new Berita("Adi","Berita hari Jumat","17-05-2024","Budi", false));
        listBerita.add(new Berita("Adi","Berita hari Senin","20-05-2024","Budi", false));
        listBerita.add(new Berita("Adi","Berita hari Selasa","21-05-2024","Budi", true));

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        MaterialDatePicker<Long> datePicker = builder.build();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
                String formattedDate = sdf.format(new Date(selection));

                searchBar.setText(formattedDate);
            }
        });

        MessageAdapter adapter = new MessageAdapter(getContext(), listBerita);
        list.setAdapter(adapter);
        pb.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
    }

    private void refresh(){

    }

}