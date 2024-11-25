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
import com.bintangjuara.bk.models.Feedback;
import com.bintangjuara.bk.models.UserData;
import com.bintangjuara.bk.services.RequestBK;
import com.bintangjuara.bk.adapters.AnnouncementAdapter;
import com.bintangjuara.bk.models.Announcement;
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
    ArrayList<Object> announcementArrayList;
    LinearLayout mainLayout;
    ActivityResultLauncher<Intent> resultLauncher;
    UserData userData;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            userData = (UserData) getArguments().getSerializable("userData");
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
        int userId = userData.getId();

        requestBerita(userId);

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
                requestBerita(userId);
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
                ArrayList<Object> filteredAnnouncement = new ArrayList<>();
                for(Object obj : announcementArrayList){
                    if(obj instanceof Announcement) {
                        Announcement announcement = (Announcement) obj;
                        Date dateBerita = announcement.getDate();
                        if (!dateBerita.before(dateStart) && !dateBerita.after(dateEnd)) {
                            filteredAnnouncement.add(announcement);
                        }
                    }
                }
                AnnouncementAdapter adapter;
                adapter = new AnnouncementAdapter(getContext(), filteredAnnouncement);
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

    private void requestBerita(int userId){
        RequestBK requestBK = RequestBK.getInstance(getContext());
        requestBK.requestAnnouncement(userId, new RequestBK.AnnouncementListener() {
            @Override
            public void onResponse(ArrayList<Object> listData) {
                AnnouncementAdapter.OnClickListener onClickListener = new AnnouncementAdapter.OnClickListener() {

                    @Override
                    public void onClick(Announcement announcement) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("announcement",announcement);
                        getParentFragmentManager().setFragmentResult("view_announcement", bundle);
                        Log.d("RESULT", "Refreshed");
                    }

                    @Override
                    public void onClick(Feedback feedback) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("announcement",feedback);
                        getParentFragmentManager().setFragmentResult("view_announcement", bundle);
                        Log.d("RESULT", "Refreshed");
                    }
                };
                if(idFilter!=null){
                    ArrayList<Object> filteredAnnouncement = new ArrayList<>();
                    for(Object obj: listData){
                        if(obj instanceof Announcement) {
                            Announcement announcement = (Announcement) obj;
                            if(announcement instanceof Feedback) {
                                Feedback feedback = (Feedback) announcement;
                                if (feedback.getStudentId() == Integer.parseInt(idFilter))
                                    filteredAnnouncement.add(announcement);
                            }else {
                                filteredAnnouncement.add(announcement);
                            }
                        }
                    }
                    listData = filteredAnnouncement;
                }
                announcementArrayList = listData;
                AnnouncementAdapter adapter;
                adapter = new AnnouncementAdapter(getContext(), listData);
                adapter.setOnClickListener(onClickListener);
                list.setLayoutManager(new LinearLayoutManager(getContext()));
                list.setAdapter(adapter);

                pb.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                mainLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception error) {

            }
        });
    }
//
//    private void requestBerita(){
//        AnnouncementAdapter.OnClickListener onClickListener = new AnnouncementAdapter.OnClickListener() {
//            @Override
//            public void onClick(Announcement berita) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("berita",berita);
//                getParentFragmentManager().setFragmentResult("view_berita", bundle);
//            }
//        };
//        RequestBK requestBK = RequestBK.getInstance(getContext());
//        requestBK.requestAnnouncement(new RequestBK.AnnouncementListener() {
//            @Override
//            public void onResponse(ArrayList<Announcement> listAnnouncement) {
//                if(idFilter!=null){
//                    ArrayList<Announcement> filteredAnnouncement = new ArrayList<>();
//                    for(Announcement announcement : listAnnouncement){
//                        Log.d("DATE", announcement.getDate().toString());
//                        if(announcement.getStudentId()==Integer.parseInt(idFilter)){
//                            filteredAnnouncement.add(announcement);
//                        }
//                    }
//                    listAnnouncement = filteredAnnouncement;
//                }
//                announcementArrayList = listAnnouncement;
//                AnnouncementAdapter adapter;
//                adapter = new AnnouncementAdapter(getContext(), listAnnouncement);
//                adapter.setOnClickListener(onClickListener);
//                list.setLayoutManager(new LinearLayoutManager(getContext()));
//                list.setAdapter(adapter);
//
//                pb.setVisibility(View.GONE);
//                refreshLayout.setRefreshing(false);
//                mainLayout.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onError(Exception error, ArrayList<Announcement> listAnnouncement) {
//                if(idFilter!=null){
//                    ArrayList<Announcement> filteredAnnouncement = new ArrayList<>();
//                    for(Announcement announcement : listAnnouncement){
//                        Log.d("DATE", announcement.getDate().toString());
//                        if(announcement.getStudentId()==Integer.parseInt(idFilter)){
//                            filteredAnnouncement.add(announcement);
//                        }
//                    }
//                    listAnnouncement = filteredAnnouncement;
//                }
//                announcementArrayList = listAnnouncement;
//                AnnouncementAdapter adapter;
//                adapter = new AnnouncementAdapter(getContext(), listAnnouncement);
//                adapter.setOnClickListener(onClickListener);
//                list.setLayoutManager(new LinearLayoutManager(getContext()));
//                list.setAdapter(adapter);
//
//                pb.setVisibility(View.GONE);
//                refreshLayout.setRefreshing(false);
//                mainLayout.setVisibility(View.VISIBLE);
//            }
//        });
//    }

    private void refresh(){

    }


}