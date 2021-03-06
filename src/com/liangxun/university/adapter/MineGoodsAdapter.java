package com.liangxun.university.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.liangxun.university.R;
import com.yixia.camera.demo.UniversityApplication;
import com.liangxun.university.entity.PaopaoGoods;
import com.liangxun.university.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * 动态
 */
public class MineGoodsAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<PaopaoGoods> findEmps;
    private Context mContext;
    private Resources res;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public MineGoodsAdapter(List<PaopaoGoods> findEmps, Context mContext) {
        res = mContext.getResources();
        this.findEmps = findEmps;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return findEmps.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mine_goods_item, null);
            holder.goods_item_pic = (ImageView) convertView.findViewById(R.id.goods_item_pic);
            holder.goods_item_title = (TextView) convertView.findViewById(R.id.goods_item_title);
            holder.goods_item_money = (TextView) convertView.findViewById(R.id.goods_item_money);
            holder.goods_item_dateline = (TextView) convertView.findViewById(R.id.goods_item_dateline);
            holder.goods_item_money_market = (TextView) convertView.findViewById(R.id.goods_item_money_market);
            holder.goods_item_money_market.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PaopaoGoods cell = findEmps.get(position);//获得元素
        if (cell != null) {
            if (!StringUtil.isNullOrEmpty(cell.getCover())) {
                String[] picarr = cell.getCover().split(",");
                if (picarr != null) {
                    imageLoader.displayImage(picarr[0], holder.goods_item_pic, UniversityApplication.txOptions, animateFirstListener);
                }
            }
            holder.goods_item_title.setText(cell.getName());
            holder.goods_item_money.setText(res.getString(R.string.prices) + cell.getSellPrice());
            holder.goods_item_money_market.setText(res.getString(R.string.prices) + cell.getMarketPrice());
            holder.goods_item_dateline.setText(cell.getUpTime());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView goods_item_pic;//商品封面
        TextView goods_item_title;//商品
        TextView goods_item_money_market;//市场价格
        TextView goods_item_money;//价格
        TextView goods_item_dateline;//时间
    }
}
