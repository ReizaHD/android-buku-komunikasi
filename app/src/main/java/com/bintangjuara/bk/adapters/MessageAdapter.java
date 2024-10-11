package com.bintangjuara.bk.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bintangjuara.bk.models.Berita;
import com.bintangjuara.bk.R;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<Berita> listBerita;
    LayoutInflater inflater;

    public MessageAdapter(Context ctx,  ArrayList<Berita> listBerita) {
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.listBerita = listBerita;
    }

    @Override
    public int getCount() {
        return listBerita.size();
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
        Berita berita = listBerita.get(i);
        view = inflater.inflate(R.layout.new_list_message, null);
        TextView namaSiswa = view.findViewById(R.id.nama_siswa);
        TextView tgl = view.findViewById(R.id.tanggal);
        TextView pesanTxt = view.findViewById(R.id.deskripsi);

        FrameLayout avatarLayout = view.findViewById(R.id.avatar_layout);
        RelativeLayout roundBg = view.findViewById(R.id.pemberitahuan);
        Button infoBtn = view.findViewById(R.id.info_button);

        if(berita.isSudahDibaca()){
            avatarLayout.setBackgroundColor(Color.parseColor("#C3C3C3"));
            roundBg.setBackground(ctx.getDrawable(R.drawable.card_round_bg_gray));
            infoBtn.setBackgroundColor(Color.parseColor("#C3C3C3"));
        }

        namaSiswa.setText(berita.getMurid());
        tgl.setText(berita.getTanggalText());
        pesanTxt.setText(berita.getIsiPesan());
        return view;
    }
}
