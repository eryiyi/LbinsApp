package com.liangxun.university.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.liangxun.university.R;
import com.yixia.camera.demo.UniversityApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/5
 * Time: 16:17
 * 类的功能、说明写在此处.
 */
public class DetailGoodsGridViewAdapter extends BaseAdapter {
    private String[] imageUrls;
    private Context mContext;
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public DetailGoodsGridViewAdapter(String[] imageUrls, Context mContext) {
        this.imageUrls = imageUrls;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            imageView = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.goods_detail_item_image, parent, false);
        } else {
            imageView = (ImageView) convertView;
        }
        // 将图片显示任务增加到执行池，图片将被显示到ImageView当轮到此ImageView
        imageLoader.displayImage(imageUrls[position], imageView, UniversityApplication.tpOptions);
        return imageView;
    }
}

