package com.bintangjuara.bk.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bintangjuara.bk.R;
import com.bintangjuara.bk.activities.ViewBeritaActivity;
import com.bintangjuara.bk.services.RequestBK;
import com.bintangjuara.bk.adapters.MessageAdapter;
import com.bintangjuara.bk.models.Berita;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HistoryFragment extends Fragment {

    RecyclerView list;
    ProgressBar pb;
    EditText searchBar;
    SwipeRefreshLayout refreshLayout;
    String idFilter;
    ArrayList<Berita> beritaArrayList;
    LinearLayout mainLayout;
    ActivityResultLauncher<Intent> resultLauncher;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            idFilter = getArguments().getString("user_id");
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
        mainLayout = view.findViewById(R.id.main);

        requestBerita();

//        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
//        builder.setTitleText("Select date")
//                .setSelection(MaterialDatePicker.todayInUtcMilliseconds());
//        MaterialDatePicker<Long> datePicker = builder.build();

        // Set the app's locale to Indonesian
        Locale locale = new Locale("id", "ID");
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

        builder.setTitleText("Select date");

        Calendar endCalendar = Calendar.getInstance();
        long endDate = endCalendar.getTimeInMillis();
        endCalendar.add(Calendar.DAY_OF_YEAR, -7);
        long startDate = endCalendar.getTimeInMillis();
        endCalendar.set(2024, 8, 1, 0,0,0);
        long startMonth = endCalendar.getTimeInMillis();
        builder.setSelection(new Pair<>(startDate, endDate));

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now());
        constraintsBuilder.setStart(startMonth);
        constraintsBuilder.setEnd(MaterialDatePicker.thisMonthInUtcMilliseconds());
        builder.setCalendarConstraints(constraintsBuilder.build());
        builder.setTheme(R.style.DateRangePickerDialogTheme);
        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();

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

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", new Locale("id", "ID"));
                Calendar calendar = Calendar.getInstance();

                calendar.setTime(new Date(selection.first));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date dateStart = calendar.getTime();

                calendar.setTime(new Date(selection.second));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date dateEnd = calendar.getTime();
                ArrayList<Berita> filteredBerita = new ArrayList<>();
                for(Berita berita : beritaArrayList){
                    Date dateBerita = berita.getDate();
                    if(!dateBerita.before(dateStart) && !dateBerita.after(dateEnd)){
                        filteredBerita.add(berita);
                    }
                }
                MessageAdapter adapter;
                adapter = new MessageAdapter(getContext(), filteredBerita);
                list.setLayoutManager(new LinearLayoutManager(getContext()));
                list.setAdapter(adapter);
                String formattedDate = sdf.format(dateStart) + " - "+ sdf.format(dateEnd);
                searchBar.setText(formattedDate);
            }
        });

//        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
//            @Override
//            public void onPositiveButtonClick(Long selection) {
//                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
//                String formattedDate = sdf.format(new Date(selection));
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(new Date(selection));
//                calendar.set(Calendar.HOUR_OF_DAY, 0);
//                calendar.set(Calendar.MINUTE, 0);
//                calendar.set(Calendar.SECOND, 0);
//                calendar.set(Calendar.MILLISECOND, 0);
//                Date selectedDate = calendar.getTime();
//                Log.d("DATE", selectedDate.toString());
//                ArrayList<Berita> filteredBerita = new ArrayList<>();
//                for(Berita berita : beritaArrayList){
//                    if(berita.getDate().equals(selectedDate)){
//                        filteredBerita.add(berita);
//                    }
//                }
//                MessageAdapter adapter;
//                adapter = new MessageAdapter(getContext(), filteredBerita);
//                list.setLayoutManager(new LinearLayoutManager(getContext()));
//                list.setAdapter(adapter);
//                searchBar.setText(formattedDate);
//            }
//        });


    }



    private void requestBerita(){
        MessageAdapter.OnClickListener onClickListener = new MessageAdapter.OnClickListener() {
            @Override
            public void onClick(Berita berita) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("berita",berita);
                getParentFragmentManager().setFragmentResult("view_berita", bundle);
            }
        };
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
                adapter.setOnClickListener(onClickListener);
                list.setLayoutManager(new LinearLayoutManager(getContext()));
                list.setAdapter(adapter);

                pb.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                mainLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception error, ArrayList<Berita> listBerita) {
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
                adapter.setOnClickListener(onClickListener);
                list.setLayoutManager(new LinearLayoutManager(getContext()));
                list.setAdapter(adapter);

                pb.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                mainLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void refresh(){

    }


}