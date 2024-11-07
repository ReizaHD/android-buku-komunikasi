package com.bintangjuara.bk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bintangjuara.bk.R;
import com.bintangjuara.bk.models.CarouselItem;
import com.bumptech.glide.Glide;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CarouselItem item = null;
        Intent intent = getIntent();
        if(intent!=null){
            item = (CarouselItem) intent.getSerializableExtra("image");
        }

        ImageView imageView = findViewById(R.id.imageView);

        Glide.with(ViewImageActivity.this).load(item.getImageUrl()).into(imageView);
    }
}