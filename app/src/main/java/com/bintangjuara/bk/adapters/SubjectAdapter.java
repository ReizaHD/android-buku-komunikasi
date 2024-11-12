package com.bintangjuara.bk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bintangjuara.bk.R;
import com.bintangjuara.bk.models.Subject;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>{

    private Context ctx;
    private ArrayList<Subject> subjects;

    public SubjectAdapter(Context ctx, ArrayList<Subject> subjects) {
        this.ctx = ctx;
        this.subjects = subjects;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.list_catatan, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.subjectName.setText(subject.getSubjectName());
        holder.message.setText(subject.getMessage());
        if(position == subjects.size()-1){
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.card.getLayoutParams();
            params.setMargins(0,0,0,0);
        }
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        TextView message;
        ConstraintLayout card;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.nama_mapel);
            message = itemView.findViewById(R.id.catatan);
            card = itemView.findViewById(R.id.main);
        }
    }

}
