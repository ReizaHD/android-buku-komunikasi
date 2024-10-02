package com.example.bk.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bk.models.Pemberitahuan;
import com.example.bk.R;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<Pemberitahuan> listPemberitahuan;
    LayoutInflater inflater;

    public MessageAdapter(Context ctx,  ArrayList<Pemberitahuan> listPemberitahuan) {
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.listPemberitahuan = listPemberitahuan;
    }

    @Override
    public int getCount() {
        return listPemberitahuan.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Pemberitahuan pemberitahuan = listPemberitahuan.get(i);
        view = inflater.inflate(R.layout.new_list_message, null);
        TextView namaSiswa = view.findViewById(R.id.nama_siswa);
        TextView tgl = view.findViewById(R.id.tanggal);
        TextView pesanTxt = view.findViewById(R.id.deskripsi);

        FrameLayout avatarLayout = view.findViewById(R.id.avatar_layout);
        RelativeLayout roundBg = view.findViewById(R.id.pemberitahuan);
        Button infoBtn = view.findViewById(R.id.info_button);

        if(pemberitahuan.isSudahDibaca()){
            avatarLayout.setBackgroundColor(Color.parseColor("#C3C3C3"));
            roundBg.setBackground(ctx.getDrawable(R.drawable.round_bg_gray));
            infoBtn.setBackgroundColor(Color.parseColor("#C3C3C3"));
        }

        namaSiswa.setText(pemberitahuan.getMurid());
        tgl.setText(pemberitahuan.getTanggal());
        pesanTxt.setText(pemberitahuan.getIsiPesan());
        return view;
    }
}
