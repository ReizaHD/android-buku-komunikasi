package com.bintangjuara.bk.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bintangjuara.bk.activities.ViewBeritaActivity;
import com.bintangjuara.bk.models.Berita;
import com.bintangjuara.bk.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    Context ctx;
    ArrayList<Berita> listBerita;
    LayoutInflater inflater;

    public MessageAdapter(Context ctx, ArrayList<Berita> listBerita) {
        this.ctx = ctx;
        this.listBerita = listBerita;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.new_list_message,parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Berita berita = listBerita.get(position);
        if(berita.isRead()){
            holder.avatarLayout.setBackgroundColor(Color.parseColor("#C3C3C3"));
            holder.roundBg.setBackground(ctx.getDrawable(R.drawable.card_round_bg_gray));
            holder.infoBtn.setBackgroundColor(Color.parseColor("#C3C3C3"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, ViewBeritaActivity.class);
                intent.putExtra("berita", berita);
                ctx.startActivity(intent);
            }
        });

        holder.namaSiswa.setText(berita.getStudentName());
        holder.pesanTxt.setText(berita.getAdditionalFeedback());
    }

    @Override
    public int getItemCount() {
        return listBerita.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView namaSiswa, tgl, pesanTxt;
        FrameLayout avatarLayout;
        RelativeLayout roundBg;
        Button infoBtn;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            namaSiswa = itemView.findViewById(R.id.nama_siswa);
            tgl = itemView.findViewById(R.id.tanggal);
            pesanTxt = itemView.findViewById(R.id.deskripsi);
            avatarLayout = itemView.findViewById(R.id.avatar_layout);
            roundBg = itemView.findViewById(R.id.pemberitahuan);
            infoBtn = itemView.findViewById(R.id.info_button);
        }
    }

}
