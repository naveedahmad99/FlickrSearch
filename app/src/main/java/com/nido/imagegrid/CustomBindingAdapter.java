package com.nido.imagegrid;

import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import com.nido.R;

public class CustomBindingAdapter {
    @BindingAdapter("src")
    public static void setImage(ImageView imageView, String url) {
        imageView.setImageResource(R.drawable.ic_photo);
        ImageLoader.INSTANCE.fetchImage(imageView, url);
    }
}
