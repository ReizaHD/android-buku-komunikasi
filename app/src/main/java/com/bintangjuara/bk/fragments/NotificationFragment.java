package com.bintangjuara.bk.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bintangjuara.bk.R;
import com.bintangjuara.bk.adapters.MessageAdapter;
import com.bintangjuara.bk.models.Berita;
import com.bintangjuara.bk.models.Pelajaran;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class NotificationFragment extends Fragment {

    RecyclerView list;
    ProgressBar pb;
    EditText searchBar;
    SwipeRefreshLayout refreshLayout;
    String idFilter;


    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            idFilter = getArguments().getString("id");
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

        requestBerita();

        ArrayList<Berita> listBerita = new ArrayList<>();
        listBerita.add(new Berita("Kegiatan siswa pada hari kamis " +
                "adalah melakukan kunjungan ke salah satu masjid unik yang terletak di " +
                "kabupaten semarang. para siswa didampingi bapak ibu guru berangkat menggunakan " +
                "Bus dari SD Islam Bintang Juara pukul 08.00 pagi ....."));
        listBerita.add(new Berita("Ayah Bunda, Besok Jumat 17 Mei 2024 diharapkan kakak" +
                " kakak salih dan salihah membawa buku tulis dan tumbler untuk kegiatan outing class di" +
                " sekitar sekolah. Ayah Bunda bisa menjemput Kakak Salih dan Salihah pada jam 17.00 setelah" +
                " kegiatan"));
        listBerita.add(new Berita("Berita hari Jumat"));
        listBerita.add(new Berita("Berita hari Senin"));
        listBerita.add(new Berita("Berita hari Selasa"));

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        MaterialDatePicker<Long> datePicker = builder.build();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
                String formattedDate = sdf.format(new Date(selection));

                searchBar.setText(formattedDate);
            }
        });


    }

    private void refresh(){

    }

    private void requestBerita(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "http://192.168.1.13/buku_komunikasi/berita_rest.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        ArrayList<Berita> listBerita = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject obj = jsonArray.getJSONObject(i);
                                if(idFilter != null){
                                    if(!idFilter.equals(obj.getString("user_id"))){
                                        continue;
                                    }
                                }
                                String id = obj.getString("id");
                                String tugasWeekend = obj.getString("tugas");
                                String catatan = obj.getString("catatan");
                                String ekstrakurikuler = obj.getString("ekstrakurikuler");
                                String catatanOrtu = obj.getString("catatan_ortu");
                                JSONObject mapel = obj.getJSONObject("mata_pelajaran");

                                ArrayList<Pelajaran> listPelajaran = new ArrayList<>();
                                for (Iterator<String> it = mapel.keys(); it.hasNext(); ) {
                                    String key = it.next();
                                    listPelajaran.add(new Pelajaran(key, mapel.getString(key)));
                                }
                                listBerita.add(new Berita(id,tugasWeekend, catatan, ekstrakurikuler, catatanOrtu, listPelajaran));


                            }
                            MessageAdapter adapter;
                            adapter = new MessageAdapter(getContext(), listBerita);
                            list.setLayoutManager(new LinearLayoutManager(getContext()));
                            list.setAdapter(adapter);
                            pb.setVisibility(View.GONE);
                            list.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                ArrayList<Berita> listBerita = new ArrayList<>();
                listBerita.add(new Berita("Kegiatan siswa pada hari kamis " +
                        "adalah melakukan kunjungan ke salah satu masjid unik yang terletak di " +
                        "kabupaten semarang. para siswa didampingi bapak ibu guru berangkat menggunakan " +
                        "Bus dari SD Islam Bintang Juara pukul 08.00 pagi ....."));
                listBerita.add(new Berita("Ayah Bunda, Besok Jumat 17 Mei 2024 diharapkan kakak" +
                        " kakak salih dan salihah membawa buku tulis dan tumbler untuk kegiatan outing class di" +
                        " sekitar sekolah. Ayah Bunda bisa menjemput Kakak Salih dan Salihah pada jam 17.00 setelah" +
                        " kegiatan"));
                listBerita.add(new Berita("Berita hari Jumat"));
                listBerita.add(new Berita("Berita hari Senin"));

                MessageAdapter adapter;
                adapter = new MessageAdapter(getContext(), listBerita);
                list.setLayoutManager(new LinearLayoutManager(getContext()));
                list.setAdapter(adapter);
                pb.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }
        });

        requestQueue.add(stringRequest);

    }

}