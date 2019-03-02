package com.feng.wanandroid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BasePagingLoadAdapter;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.entity.data.ArticleData;
import com.feng.wanandroid.widget.MyImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/11
 */
public class ArticleAdapter extends BasePagingLoadAdapter<ArticleData> {

    private static final int TYPE_HEADER = 3;      //header

    private OnClickListener clickListener;
    private BannerInfoListener bannerInfoListener;

    private static final String TAG = "fzh";

    public interface OnClickListener {
//        void clickChapter();      //点击了栏目（以后再实现）
        void clickCollect(boolean collect, int id, int position);    //点击了收藏item
        void clickItem(String link, String title, boolean isCollect, int id, int position);
        void clickBannerItem(int position); //点击Banner
    }

    public interface BannerInfoListener {
        List<String> getImageUrlList();
        List<String> getTitleList();
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setBannerInfoListener(BannerInfoListener bannerInfoListener) {
        this.bannerInfoListener = bannerInfoListener;
    }

    public ArticleAdapter(Context context, List<ArticleData> list, LoadMoreListener loadMoreListener) {
        super(context, list, loadMoreListener);
    }
    

    @Override
    protected int getPageCount() {
        return Constant.HOME_ARTICLE_PAGE_SIZE;
    }

    @Override
    protected RecyclerView.ViewHolder setItemViewHolder(ViewGroup parent, int viewType) {
        return new HomeArticleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_article, parent, false));
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        int listPosition = position - 1;
        ((HomeArticleViewHolder)holder).authorName.setText(list.get(listPosition).getAuthor());
        ((HomeArticleViewHolder)holder).date.setText(list.get(listPosition).getNiceDate());
        ((HomeArticleViewHolder)holder).title.setText(list.get(listPosition).getTitle());
        ((HomeArticleViewHolder)holder).chapter.setText(list.get(listPosition).getChapterName());
        ((HomeArticleViewHolder)holder).collect.setSelected(list.get(listPosition).isCollect());
        ((HomeArticleViewHolder)holder).collect.setOnClickListener(v -> {
            boolean isCollect = list.get(listPosition).isCollect();
            clickListener.clickCollect(isCollect, list.get(listPosition).getId(), holder.getAdapterPosition());
        });

        ((HomeArticleViewHolder)holder).itemView.setOnClickListener(v -> {
            clickListener.clickItem(list.get(listPosition).getLink(), list.get(listPosition).getTitle(),
                    list.get(listPosition).isCollect(), list.get(listPosition).getId(), listPosition);    //普通点击
        });

    }

    class HomeArticleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_home_article_author_name)
        TextView authorName;
        @BindView(R.id.tv_home_article_date)
        TextView date;
        @BindView(R.id.tv_home_article_title)
        TextView title;
        @BindView(R.id.tv_home_article_chapter)
        TextView chapter;
        @BindView(R.id.iv_home_article_collect)
        ImageView collect;

        public HomeArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.bn_home_banner)
        Banner homeBanner;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            if (list.size() < getPageCount()) {
                return TYPE_ITEM;
            } else {
                if (position == list.size() + 1) {
                    return TYPE_BOTTOM;
                } else {
                    return TYPE_ITEM;
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.header_home_banner, null));
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (TYPE_HEADER == getItemViewType(position)) {
            //header
            ((HeaderViewHolder)holder).homeBanner.setImageLoader(new MyImageLoader())  //设置图片加载器
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE) //指定样式
                    .setImages(bannerInfoListener.getImageUrlList())   //设置图片url集合
                    .setBannerTitles(bannerInfoListener.getTitleList())     //设置title集合
                    .setDelayTime(3000)     //设置轮播时间
                    .start();   //最后才start
            ((HeaderViewHolder)holder).homeBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    clickListener.clickBannerItem(position);
                }
            });
        }
    }
}
