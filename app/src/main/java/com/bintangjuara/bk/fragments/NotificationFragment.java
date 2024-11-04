package com.bintangjuara.bk.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.bintangjuara.bk.R;
import com.bintangjuara.bk.RequestBK;
import com.bintangjuara.bk.adapters.MessageAdapter;
import com.bintangjuara.bk.models.Berita;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationFragment extends Fragment {

    RecyclerView list;
    ProgressBar pb;
    EditText searchBar;
    SwipeRefreshLayout refreshLayout;
    String idFilter;
    ArrayList<Berita> beritaArrayList;



    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            idFilter = getArguments().getString("id");
        }

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

        if(idFilter!=null)
            Log.d("id",idFilter);

        list = view.findViewById(R.id.list_item);
        pb = view.findViewById(R.id.progress_bar);
        searchBar = view.findViewById(R.id.search_bar);
        refreshLayout = view.findViewById(R.id.refresh);

        requestBerita();

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        MaterialDatePicker<Long> datePicker = builder.build();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestBerita();
                searchBar.setText("");
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
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(selection));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date selectedDate = calendar.getTime();
                Log.d("DATE", selectedDate.toString());
                ArrayList<Berita> filteredBerita = new ArrayList<>();
                for(Berita berita : beritaArrayList){
                    if(berita.getDate().equals(selectedDate)){
                        filteredBerita.add(berita);
                    }
                }
                MessageAdapter adapter;
                adapter = new MessageAdapter(getContext(), filteredBerita);
                list.setLayoutManager(new LinearLayoutManager(getContext()));
                list.setAdapter(adapter);
                searchBar.setText(formattedDate);
            }
        });


    }



    private void requestBerita(){
        RequestBK requestBK = RequestBK.getInstance(getContext());
        requestBK.requestBerita(new RequestBK.BeritaListener() {
            @Override
            public void onResponse(ArrayList<Berita> listBerita) {
                if(idFilter!=null){
                    ArrayList<Berita> filteredBerita = new ArrayList<>();
                    for(Berita berita:listBerita){
                        Log.d("DATE", berita.getDate().toString());
                        if(berita.getStudentId()==Integer.parseInt(idFilter)){
                            filteredBerita.add(berita);
                        }
                    }
                    listBerita = filteredBerita;
                }
                beritaArrayList = listBerita;
                MessageAdapter adapter;
                adapter = new MessageAdapter(getContext(), listBerita);
                list.setLayoutManager(new LinearLayoutManager(getContext()));
                list.setAdapter(adapter);

                pb.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Exception error) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void refresh(){

    }


}