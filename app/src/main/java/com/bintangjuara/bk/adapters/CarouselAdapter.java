package com.bintangjuara.bk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bintangjuara.bk.models.CarouselItem;
import com.bintangjuara.bk.R;

import com.bumptech.glide.Glide;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {

    private Context context;
    private List<CarouselItem> carouselItems;
    private OnItemClickListener onItemClickListener;

    public CarouselAdapter(Context context, List<CarouselItem> carouselItems) {
        this.context = context;
        this.carouselItems = carouselItems;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_item, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        CarouselItem item = carouselItems.get(position);
        String urlImage = carouselItems.get(position).getImageUrl();
        Glide.with(context).load(urlImage).into(holder.itemImage);
//        holder.itemImage.setImageResource(item.getImageResource());
        holder.itemText.setText(item.getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick(holder.itemImage, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carouselItems.size();
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemText;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemText = itemView.findViewById(R.id.itemText);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(ImageView imageView, CarouselItem item);
    }
}

