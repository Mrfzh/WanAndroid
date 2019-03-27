package com.feng.wanandroid.widget;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * @author Feng Zhaohao
 * Created on 2019/2/14
 */
public class MyImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context.getApplicationContext())
                .load(path)
                .into(imageView);
    }
}
