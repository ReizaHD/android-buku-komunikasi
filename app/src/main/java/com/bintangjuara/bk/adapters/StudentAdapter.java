package com.bintangjuara.bk.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bintangjuara.bk.R;
import com.bintangjuara.bk.fragments.ProfileFragment;
import com.bintangjuara.bk.models.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private Context ctx;
    private ArrayList<Student> students;
    private Fragment fragment;

    public StudentAdapter(Context ctx, ArrayList<Student> students, Fragment fragment) {
        this.ctx = ctx;
        this.students = students;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.list_anak,parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Log.d("COunt",String.valueOf(getItemCount()));
        Student student = students.get(position);
        holder.mNama.setText(student.getName());
        holder.mKelas.setText(student.getClassName());

        int id = student.getId();

//        if(position == 0){
//            holder.mainLayout.setBackgroundResource(R.drawable.card_yellow_round_top);
//            Log.d("FIRST", "true");
//        }else if(position == getItemCount()-1){
//            holder.mainLayout.setBackgroundResource(R.drawable.card_yellow_round_bottom);
//            Log.d("LAST", "true");
//        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("user_id",String.valueOf(id));
                fragment.getParentFragmentManager().setFragmentResult("filter", bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView mNama, mKelas;
        LinearLayout mainLayout;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            mNama = itemView.findViewById(R.id.nama_anak);
            mKelas = itemView.findViewById(R.id.kelas_anak);
            mainLayout = itemView.findViewById(R.id.main);
        }
    }
}
