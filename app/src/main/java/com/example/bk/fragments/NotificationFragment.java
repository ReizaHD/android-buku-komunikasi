package com.example.bk.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bk.R;
import com.example.bk.adapters.MessageAdapter;
import com.example.bk.models.Pemberitahuan;
import com.example.bk.models.UserData;
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
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = view.findViewById(R.id.list_item);
        pb = view.findViewById(R.id.progress_bar);
        searchBar = view.findViewById(R.id.search_bar);

        ArrayList<Pemberitahuan> listPemberitahuan = new ArrayList<>();
        listPemberitahuan.add(new Pemberitahuan("Adi","Kegiatan siswa pada hari kamis " +
                "adalah melakukan kunjungan ke salah satu masjid unik yang terletak di " +
                "kabupaten semarang. para siswa didampingi bapak ibu guru berangkat menggunakan " +
                "Bus dari SD Islam Bintang Juara pukul 08.00 pagi .....","Kamis, 16 Mei 2024","Budi",false));
        listPemberitahuan.add(new Pemberitahuan("Adi","Ayah Bunda, Besok Jumat 17 Mei 2024 diharapkan kakak" +
                " kakak salih dan salihah membawa buku tulis dan tumbler untuk kegiatan outing class di" +
                " sekitar sekolah. Ayah Bunda bisa menjemput Kakak Salih dan Salihah pada jam 17.00 setelah" +
                " kegiatan","Kamis, 16 Mei 2024","Alan", false));
        listPemberitahuan.add(new Pemberitahuan("Adi","Pemberitahuan hari selasa","21-09-2024","Budi", false));
        listPemberitahuan.add(new Pemberitahuan("Adi","Pemberitahuan hari selasa","21-09-2024","Budi", true));

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        MaterialDatePicker<Long> datePicker = builder.build();

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

        MessageAdapter adapter = new MessageAdapter(getContext(), listPemberitahuan);
        list.setAdapter(adapter);
        pb.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
    }


}