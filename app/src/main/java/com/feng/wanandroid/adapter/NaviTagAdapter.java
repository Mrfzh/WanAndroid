package com.feng.wanandroid.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * FlowLayout的Adpater
 *
 * @author Feng Zhaohao
 * Created on 2019/5/1
 */
public class NaviTagAdapter extends TagAdapter<String> {

    private Context mContext;

    /**
     * @param datas 各标签的数据
     */
    public NaviTagAdapter(Context context, List<String> datas) {
        super(datas);
        mContext = context;
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        TextView textView = (TextView) LayoutInflater.from(mContext).inflate(
                R.layout.item_navi_tag, parent, false);
        textView.setText(s);

        return textView;
    }
}
