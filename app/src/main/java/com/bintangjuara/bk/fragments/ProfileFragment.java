package com.bintangjuara.bk.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bintangjuara.bk.activities.LoginActivity;
import com.bintangjuara.bk.R;
import com.bintangjuara.bk.models.UserData;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ProfileFragment extends Fragment {

    UserData userData;
    TextView profileName, email;
    MaterialSwitch notificationSwitch;
    RecyclerView listView;

    public ProfileFragment() {
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
        Log.d("New Fragment","True");
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_session",MODE_PRIVATE);
        boolean enableNotification = sharedPreferences.getBoolean("enableNotification",false);
        Log.d("enableNotification", String.valueOf(enableNotification));
        Button logoutBtn = view.findViewById(R.id.logout_btn);
        Button editPassBtn = view.findViewById(R.id.edit_password_btn);
        listView = view.findViewById(R.id.list_anak);

        ArrayList<String> nama = new ArrayList<>();
        ArrayList<String> kelas = new ArrayList<>();
        ArrayList<String> id = new ArrayList<>();

        nama.add("Abqory Fusena Anarghya Setiadi");
        kelas.add("1A");
        id.add("1");

        nama.add("Reiza Hersa Dwitama");
        kelas.add("10 MIPA 1");
        id.add("2");

        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        AnakAdapter adapter = new AnakAdapter(getContext(), nama, kelas, id);
        listView.setAdapter(adapter);

        profileName = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.email);
        notificationSwitch = view.findViewById(R.id.notification_switch);

        notificationSwitch.setChecked(enableNotification);

        profileName.setText(userData.getProfile());
        email.setText(userData.getEmail());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();  // Clears all data
                editor.apply();
                Log.d("Button","Logout");
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();

            }
        });
        editPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().setFragmentResult("goToEdit", new Bundle());
            }
        });

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    FirebaseMessaging.getInstance().subscribeToTopic("Berita");
                    sharedPreferences.edit().putBoolean("enableNotification", true).apply();
                }else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Berita");
                    sharedPreferences.edit().putBoolean("enableNotification", false).apply();
                }
            }
        });

    }

    public class AnakAdapter extends RecyclerView.Adapter<AnakAdapter.AnakViewHolder>{

        Context ctx;
        ArrayList<String> nama, kelas, id;
        float[] bottomRound = new float[]{
                0f, 0f,        // Top-left corner radius
                0f, 0f,        // Top-right corner radius
                30f, 30f,      // Bottom-right corner radius (change these values)
                30f, 30f         // Bottom-left corner radius
        };
        float[] topRound = new float[]{
                30f, 30f,        // Top-left corner radius
                30f, 30f,        // Top-right corner radius
                0f, 0f,      // Bottom-right corner radius (change these values)
                0f, 0f         // Bottom-left corner radius
        };

        public AnakAdapter(Context ctx, ArrayList<String> nama, ArrayList<String> kelas, ArrayList<String> id) {
            this.ctx = ctx;
            this.nama = nama;
            this.kelas = kelas;
            this.id = id;
        }

        @NonNull
        @Override
        public AnakViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.list_anak,parent, false);
            return new AnakViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AnakViewHolder holder, int position) {

            Log.d("COunt",String.valueOf(getItemCount()));
            holder.mNama.setText(nama.get(position));
            holder.mKelas.setText(kelas.get(position));
            if(position == 0){
                holder.mainLayout.setBackgroundResource(R.drawable.card_yellow_round_top);
                Log.d("FIRST", "true");
            }else if(position == nama.size()-1){
                holder.mainLayout.setBackgroundResource(R.drawable.card_yellow_round_bottom);
                Log.d("LAST", "true");
            }
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterNama(id.get(holder.getAdapterPosition()));
                }
            });

        }

        @Override
        public int getItemCount() {
            return nama.size();
        }


        public class AnakViewHolder extends RecyclerView.ViewHolder{
            TextView mNama, mKelas;
            LinearLayout mainLayout;

            public AnakViewHolder(@NonNull View itemView) {
                super(itemView);
                mNama = itemView.findViewById(R.id.nama_anak);
                mKelas = itemView.findViewById(R.id.kelas_anak);
                mainLayout = itemView.findViewById(R.id.main);
            }
        }
    }

    public void filterNama(String id){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        getParentFragmentManager().setFragmentResult("filter", bundle);
    }

}