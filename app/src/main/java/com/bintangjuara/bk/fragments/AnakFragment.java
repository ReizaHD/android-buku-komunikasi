package com.bintangjuara.bk.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bintangjuara.bk.R;
import com.bintangjuara.bk.adapters.StudentAdapter;
import com.bintangjuara.bk.models.Student;
import com.bintangjuara.bk.models.UserData;
import com.bintangjuara.bk.services.RequestBK;
import com.bintangjuara.bk.utilities.VerticalSpaceItemDecoration;

import java.util.ArrayList;

public class AnakFragment extends Fragment {

    RecyclerView recyclerView;
    UserData userData;
    ProgressBar pb;

    public AnakFragment() {
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
        return inflater.inflate(R.layout.fragment_anak, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.list_anak);
        pb = view.findViewById(R.id.progress_bar);
        if(userData!=null)requestStudent(userData.getId());

    }

    private void requestStudent(int id){
        RequestBK requestBK = RequestBK.getInstance(getContext());
        requestBK.requestStudent(id, new RequestBK.StudentListener() {
            @Override
            public void onResponse(ArrayList<Student> students) {
                Log.d("Response", String.valueOf(students.size()));
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                StudentAdapter adapter = new StudentAdapter(getContext(),students, AnakFragment.this);
                int spaceHeight = 16; // Change this value to set the desired space (in pixels)
                recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spaceHeight));
                recyclerView.setAdapter(adapter);
                pb.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception error, ArrayList<Student> students) {
                Log.e("ERROR", error.toString());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                StudentAdapter adapter = new StudentAdapter(getContext(),students, AnakFragment.this);
                int spaceHeight = 16; // Change this value to set the desired space (in pixels)
                recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spaceHeight));
                recyclerView.setAdapter(adapter);
                pb.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
}