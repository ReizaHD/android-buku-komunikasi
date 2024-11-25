package com.bintangjuara.bk.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bintangjuara.bk.models.Feedback;
import com.bintangjuara.bk.services.RequestBK;
import com.bintangjuara.bk.adapters.AnnouncementAdapter;
import com.bintangjuara.bk.models.Announcement;
import com.bintangjuara.bk.R;
import com.bintangjuara.bk.models.UserData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    RecyclerView list;
    ProgressBar pb;
    LinearLayout layout;
    TextView profileName;
    UserData userData;
    ArrayList<Announcement> arrayListAnnouncement;
    ImageView avatar;
    TextView emptyMsg;
    ActivityResultLauncher<Intent> resultLauncher;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userData = (UserData) getArguments().getSerializable("userData");
            arrayListAnnouncement = (ArrayList<Announcement>) getArguments().getSerializable("berita");
            if(arrayListAnnouncement == null)
                Log.d("BeRITA","NULL");

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

        avatar = view.findViewById(R.id.avatar);
        list = view.findViewById(R.id.list_item);
        pb = view.findViewById(R.id.progress_bar);
        layout = view.findViewById(R.id.home_layout);
        profileName = view.findViewById(R.id.profile);
        emptyMsg = view.findViewById(R.id.empty_msg);


        if(userData!=null) {
            profileName.setText(userData.getProfile());
            requestBerita(userData.getId());
        }

        Glide.with(this).load("http://192.168.1.13/buku_komunikasi/images/abqory.png").placeholder(R.drawable.avatar_default).error(R.drawable.avatar_default).centerCrop().into(avatar);
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
                ArrayList<Object> readAnnouncement = new ArrayList<>();
                for(Object obj: listData){
                    if(obj instanceof Announcement) {
                        Announcement announcement = (Announcement) obj;
                        if (!announcement.isRead())
                            readAnnouncement.add(announcement);
                    }
                }
                if(readAnnouncement.isEmpty()){
                    emptyMsg.setVisibility(View.VISIBLE);
                }else {
                    AnnouncementAdapter adapter;
                    adapter = new AnnouncementAdapter(getContext(), readAnnouncement);
                    adapter.setOnClickListener(onClickListener);
                    list.setLayoutManager(new LinearLayoutManager(getContext()));
                    list.setAdapter(adapter);
                }
                pb.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception error) {

            }
        });
    }

//    private void requestBerita(){
//        AnnouncementAdapter.OnClickListener onClickListener = new AnnouncementAdapter.OnClickListener() {
//            @Override
//            public void onClick(Announcement berita) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("berita",berita);
//                getParentFragmentManager().setFragmentResult("view_berita", bundle);
//                Log.d("RESULT", "Refreshed");
//            }
//        };
//        RequestBK requestBK = RequestBK.getInstance(getContext());
//        requestBK.requestBerita(new RequestBK.BeritaListener() {
//            @Override
//            public void onResponse(ArrayList<Announcement> listAnnouncement) {
//
//                ArrayList<Announcement> readAnnouncement = new ArrayList<>();
//                for(Announcement announcement : listAnnouncement){
//                    if(!announcement.isRead())
//                        readAnnouncement.add(announcement);
//                }
//                if(readAnnouncement.isEmpty()){
//                    emptyMsg.setVisibility(View.VISIBLE);
//                }else {
//                    AnnouncementAdapter adapter;
//                    adapter = new AnnouncementAdapter(getContext(), readAnnouncement);
//                    adapter.setOnClickListener(onClickListener);
//                    list.setLayoutManager(new LinearLayoutManager(getContext()));
//                    list.setAdapter(adapter);
//                }
//
//                pb.setVisibility(View.GONE);
//                layout.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onError(Exception error, ArrayList<Announcement> listAnnouncement) {
//                ArrayList<Announcement> readAnnouncement = new ArrayList<>();
//                for(Announcement announcement : listAnnouncement){
//                    if(!announcement.isRead())
//                        readAnnouncement.add(announcement);
//                }
//                if(readAnnouncement.isEmpty()){
//                    emptyMsg.setVisibility(View.VISIBLE);
//                }else {
//                    AnnouncementAdapter adapter;
//                    adapter = new AnnouncementAdapter(getContext(), readAnnouncement);
//                    adapter.setOnClickListener(onClickListener);
//                    list.setLayoutManager(new LinearLayoutManager(getContext()));
//                    list.setAdapter(adapter);
//                }
//
//                pb.setVisibility(View.GONE);
//                layout.setVisibility(View.VISIBLE);
//            }
//        });
//    }


}