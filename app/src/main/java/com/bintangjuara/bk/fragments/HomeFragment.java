package com.bintangjuara.bk.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bintangjuara.bk.activities.ViewBeritaActivity;
import com.bintangjuara.bk.services.RequestBK;
import com.bintangjuara.bk.adapters.MessageAdapter;
import com.bintangjuara.bk.models.Berita;
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
    ArrayList<Berita> arrayListBerita;
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
            arrayListBerita = (ArrayList<Berita>) getArguments().getSerializable("berita");
            if(arrayListBerita == null)
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
        }
        requestBerita();

        Glide.with(this).load("http://192.168.1.13/buku_komunikasi/images/abqory.png").placeholder(R.drawable.avatar_default).error(R.drawable.avatar_default).centerCrop().into(avatar);



    }

    private void requestBerita(){
        MessageAdapter.OnClickListener onClickListener = new MessageAdapter.OnClickListener() {
            @Override
            public void onClick(Berita berita) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("berita",berita);
                getParentFragmentManager().setFragmentResult("view_berita", bundle);
                Log.d("RESULT", "Refreshed");
            }
        };
        RequestBK requestBK = RequestBK.getInstance(getContext());
        requestBK.requestBerita(new RequestBK.BeritaListener() {
            @Override
            public void onResponse(ArrayList<Berita> listBerita) {

                ArrayList<Berita> readBerita = new ArrayList<>();
                for(Berita berita : listBerita){
                    if(!berita.isRead())
                        readBerita.add(berita);
                }
                if(readBerita.isEmpty()){
                    emptyMsg.setVisibility(View.VISIBLE);
                }else {
                    MessageAdapter adapter;
                    adapter = new MessageAdapter(getContext(), readBerita);
                    adapter.setOnClickListener(onClickListener);
                    list.setLayoutManager(new LinearLayoutManager(getContext()));
                    list.setAdapter(adapter);
                }

                pb.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception error, ArrayList<Berita> listBerita) {
                ArrayList<Berita> readBerita = new ArrayList<>();
                for(Berita berita : listBerita){
                    if(!berita.isRead())
                        readBerita.add(berita);
                }
                if(readBerita.isEmpty()){
                    emptyMsg.setVisibility(View.VISIBLE);
                }else {
                    MessageAdapter adapter;
                    adapter = new MessageAdapter(getContext(), readBerita);
                    adapter.setOnClickListener(onClickListener);
                    list.setLayoutManager(new LinearLayoutManager(getContext()));
                    list.setAdapter(adapter);
                }

                pb.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        });
    }


}