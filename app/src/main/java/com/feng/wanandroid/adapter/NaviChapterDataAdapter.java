package com.feng.wanandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.feng.wanandroid.entity.data.NavigationData;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Feng Zhaohao
 * Created on 2019/5/1
 */
public class NaviChapterDataAdapter extends RecyclerView.Adapter<NaviChapterDataAdapter.NaviChapterDataViewHolder>{

    private Context mContext;
    private List<String> mChapterNames;
    private List<NavigationData.ChapterData> mChapterData;

    public NaviChapterDataAdapter(Context mContext, List<String> mChapterNames, List<NavigationData.ChapterData> mChapterData) {
        this.mContext = mContext;
        this.mChapterNames = mChapterNames;
        this.mChapterData = mChapterData;
    }

    @NonNull
    @Override
    public NaviChapterDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NaviChapterDataViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_navi_chapter_data, null));
    }

    @Override
    public void onBindViewHolder(@NonNull NaviChapterDataViewHolder naviChapterDataViewHolder, int i) {
        naviChapterDataViewHolder.chapterName.setText(mChapterNames.get(i));
        naviChapterDataViewHolder.chapterContent.setAdapter(new NaviTagAdapter(mContext, mChapterData.get(i).getTitles()));
    }

    @Override
    public int getItemCount() {
        return mChapterNames.size();
    }

    class NaviChapterDataViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_navi_chapter_data_chapter_name)
        TextView chapterName;
        @BindView(R.id.tfv_item_navi_chapter_data_chapter_content)
        TagFlowLayout chapterContent;

        public NaviChapterDataViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
