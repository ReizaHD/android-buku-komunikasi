package com.bintangjuara.bk;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarouselItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;

    public CarouselItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        if (position == 0) {
            // Add extra start spacing for the first item
            outRect.left = spacing;
        } else {
            outRect.left = spacing / 2;
        }

        if (position == state.getItemCount() - 1) {
            // Add extra end spacing for the last item
            outRect.right = spacing;
        } else {
            outRect.right = spacing / 2;
        }
    }
}
