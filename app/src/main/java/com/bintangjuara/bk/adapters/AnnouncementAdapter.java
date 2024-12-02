package com.bintangjuara.bk.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bintangjuara.bk.models.Announcement;
import com.bintangjuara.bk.R;
import com.bintangjuara.bk.models.Feedback;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>{
    Context ctx;
    ArrayList<Object> listObject;
    ArrayList<Announcement> listAnnouncement;
    LayoutInflater inflater;
    OnClickListener onClickListener;

    public AnnouncementAdapter(Context ctx, ArrayList<Object> listObject) {
        this.ctx = ctx;
        this.listObject = listObject;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.new_list_message,parent, false);
        return new AnnouncementViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Object obj = listObject.get(position);
        Announcement announcement = (Announcement) obj;
        if(announcement.isRead()){
            Log.d(position+"", "TRUE");
            holder.avatarLayout.setBackgroundColor(Color.parseColor("#C3C3C3"));
            holder.roundBg.setBackground(ctx.getDrawable(R.drawable.card_round_bg_gray));
            holder.infoBtn.setBackgroundColor(Color.parseColor("#C3C3C3"));
        }else{
            Log.d(position+"", "FALSE");
            holder.avatarLayout.setBackgroundColor(Color.parseColor("#FEC100"));
            holder.roundBg.setBackground(ctx.getDrawable(R.drawable.card_round_bg));
            holder.infoBtn.setBackgroundColor(Color.parseColor("#FEC100"));
        }
        if(obj instanceof Feedback){
            Feedback feedback = (Feedback) announcement;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(feedback);
                }
            });
            holder.namaSiswa.setText(feedback.getStudentName());
            holder.pesanTxt.setText(feedback.getAdditionalFeedback());
            holder.tgl.setText(feedback.getStrDate());
            holder.infoBtn.setText("Informasi Personal");
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(announcement);
                }
            });
            holder.infoBtn.setText("Informasi Umum");
            holder.namaSiswa.setText(announcement.getTitle());
            holder.pesanTxt.setText(announcement.getContent());
            holder.tgl.setText(announcement.getStrDate());
        }
    }

    @Override
    public int getItemCount() {
        return listObject.size();
    }

    public class AnnouncementViewHolder extends RecyclerView.ViewHolder{
        TextView namaSiswa, tgl, pesanTxt;
        FrameLayout avatarLayout;
        RelativeLayout roundBg;
        Button infoBtn;


        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            namaSiswa = itemView.findViewById(R.id.nama_siswa);
            tgl = itemView.findViewById(R.id.tanggal);
            pesanTxt = itemView.findViewById(R.id.deskripsi);
            avatarLayout = itemView.findViewById(R.id.avatar_layout);
            roundBg = itemView.findViewById(R.id.pemberitahuan);
            infoBtn = itemView.findViewById(R.id.info_button);
        }
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClick(Announcement announcement);
        void onClick(Feedback feedback);
    }

}
