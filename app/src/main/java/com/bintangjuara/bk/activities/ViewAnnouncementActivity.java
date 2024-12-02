package com.bintangjuara.bk.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bintangjuara.bk.R;
import com.bintangjuara.bk.adapters.CarouselAdapter;
import com.bintangjuara.bk.models.Announcement;
import com.bintangjuara.bk.models.CarouselItem;
import com.bintangjuara.bk.services.RequestBK;
import com.bintangjuara.bk.utilities.CarouselItemDecoration;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewAnnouncementActivity extends AppCompatActivity {

    private TextView mJudul, mContent;
    private RecyclerView mImageRecyclerView;
    private int activityResult = 0;
    private MaterialToolbar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_announcement);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.yellow_light));

        Intent intent = getIntent();
        Announcement announcement = (Announcement) intent.getSerializableExtra("berita");

        mJudul = findViewById(R.id.judul);
        mContent = findViewById(R.id.content);
        mImageRecyclerView = findViewById(R.id.recycler_carousel);
        topBar = findViewById(R.id.top_bar);

        mJudul.setText(announcement.getTitle());
        mContent.setText(announcement.getContent());
        topBar.setSubtitle(announcement.getStrDate());

        ArrayList<CarouselItem> carouselItems = new ArrayList<>();
        if(announcement.getImageUrl().length>0) {
            for (String image : announcement.getImageUrl()) {
                carouselItems.add(new CarouselItem(image, ""));
            }
            CarouselAdapter adapter = new CarouselAdapter(this, carouselItems);
            mImageRecyclerView.setVisibility(View.VISIBLE);

            adapter.setOnItemClickListener(new CarouselAdapter.OnItemClickListener() {
                @Override
                public void onClick(ImageView imageView, CarouselItem item) {
                    startActivity(new Intent(ViewAnnouncementActivity.this, ViewImageActivity.class).putExtra("image", item), ActivityOptions.makeSceneTransitionAnimation(ViewAnnouncementActivity.this, imageView, "image").toBundle());
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mImageRecyclerView.setLayoutManager(layoutManager);
            mImageRecyclerView.setAdapter(adapter);

            int spacing = 50; // Adjust this value as needed
            mImageRecyclerView.addItemDecoration(new CarouselItemDecoration(spacing));

            LinearSnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(mImageRecyclerView);
        }
        requestReadBerita(String.valueOf(announcement.getAnnouncementId()));
    }

    private void requestReadBerita(String id){
        RequestBK requestBK = RequestBK.getInstance(this);
        requestBK.announcementRead(id, new RequestBK.ResponseListener() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                boolean status= false;
                try {
                    JSONObject obj = new JSONObject(response);
                    status = obj.getBoolean("success");
                } catch (JSONException e) {
                    e.toString();
                }
                if(status)
                    activityResult = 1;
            }

            @Override
            public void onError(Exception error) {
                Log.e("Response", error.toString());
            }
        });
    }

    @Override
    public void finish() {
        setResult(activityResult);
        super.finish();
    }
}