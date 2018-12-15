package com.feng.wanandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BasePagingLoadAdapter;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.entity.data.ArticleData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/11
 */
public class ArticleAdapter extends BasePagingLoadAdapter<ArticleData> {

    private OnClickListener clickListener;

    public interface OnClickListener {
//        void clickChapter();      //点击了栏目
        void clickCollect(boolean collect, int id, int position);    //点击了收藏item
        void clickItem(String link, String title, boolean isCollect, int id, int position);
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
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
        ((HomeArticleViewHolder)holder).authorName.setText(list.get(position).getAuthor());
        ((HomeArticleViewHolder)holder).date.setText(list.get(position).getNiceDate());
        ((HomeArticleViewHolder)holder).title.setText(list.get(position).getTitle());
        ((HomeArticleViewHolder)holder).chapter.setText(list.get(position).getChapterName());
        ((HomeArticleViewHolder)holder).collect.setSelected(list.get(position).isCollect());
        ((HomeArticleViewHolder)holder).collect.setOnClickListener(v -> {
            boolean isCollect = list.get(position).isCollect();
//            ((HomeArticleViewHolder)holder).collect.setSelected(!isCollect);    //设置icon状态
            clickListener.clickCollect(isCollect, list.get(position).getId(), position);
        });
        ((HomeArticleViewHolder)holder).itemView.setOnClickListener(v -> clickListener.clickItem(list.get(position).getLink(),
                list.get(position).getTitle(), list.get(position).isCollect(), list.get(position).getId(), position));
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
}
