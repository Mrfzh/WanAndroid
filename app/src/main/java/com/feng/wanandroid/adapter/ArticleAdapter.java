package com.feng.wanandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/11
 */
public class ArticleAdapter extends BasePagingLoadAdapter<ArticleData> {

    private OnClickListener clickListener;

    private List<Boolean> mCheckList;   //存储item是否被选中
    private List<Integer> mIdList;   //存储选中的item的id

    private List<Integer> mTagList;     //用于解决holder复用导致的多选问题
    private boolean isShowCheckBox = false;     //设置是否显示CheckBox

    private static final String TAG = "fzh";

    public interface OnClickListener {
//        void clickChapter();      //点击了栏目（以后再实现）
        void clickCollect(boolean collect, int id, int position);    //点击了收藏item
        void clickItem(String link, String title, boolean isCollect, int id, int position);
        void selectItemChanged(int num);    //当选中的item数目改变时，回调该方法
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ArticleAdapter(Context context, List<ArticleData> list, LoadMoreListener loadMoreListener) {
        this(context, list, loadMoreListener, null, null);
    }

    public ArticleAdapter(Context context, List<ArticleData> list, LoadMoreListener loadMoreListener,
                          List<Boolean> mCheckList, List<Integer> mIdList) {
        super(context, list, loadMoreListener);
        this.mCheckList = mCheckList;
        this.mIdList = mIdList;
        this.mTagList = new ArrayList<>();
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
            if (((HomeArticleViewHolder)holder).checkBox.getVisibility() == View.GONE) {
                boolean isCollect = list.get(position).isCollect();
                clickListener.clickCollect(isCollect, list.get(position).getId(), holder.getAdapterPosition());
            } else {
                //处于编辑状态
                boolean newCheck = !((HomeArticleViewHolder)holder).checkBox.isChecked();
                ((HomeArticleViewHolder)holder).checkBox.setChecked(newCheck);
                doInCheckStateChanged(newCheck, position, ((HomeArticleViewHolder)holder).checkBox.getTag(),
                        list.get(position).getId());
            }
        });

        ((HomeArticleViewHolder)holder).itemView.setOnClickListener(v -> {
            //分两种情况
            //一种是普通的点击，一种是处于编辑（多选删除）状态下的点击
            if (((HomeArticleViewHolder)holder).checkBox.getVisibility() == View.GONE) {
                clickListener.clickItem(list.get(position).getLink(), list.get(position).getTitle(),
                        list.get(position).isCollect(), list.get(position).getId(), position);    //普通点击
            } else {
                //处于编辑状态下
                boolean newCheck = !((HomeArticleViewHolder)holder).checkBox.isChecked();
                ((HomeArticleViewHolder)holder).checkBox.setChecked(newCheck);
                doInCheckStateChanged(newCheck, position, ((HomeArticleViewHolder)holder).checkBox.getTag(),
                        list.get(position).getId());
            }
        });

        //设置CheckBox是否可见
        if (isShowCheckBox) {
            ((HomeArticleViewHolder)holder).checkBox.setVisibility(View.VISIBLE);
            //针对holder复用导致的CheckBox混乱的解决
            ((HomeArticleViewHolder)holder).checkBox.setTag(position);
            if (mTagList.contains((int)((HomeArticleViewHolder)holder).checkBox.getTag())) {
                ((HomeArticleViewHolder)holder).checkBox.setChecked(true);   //如果在tags集合中包括当前CheckBox，则当前CheckBox设置为选中
            } else {
                ((HomeArticleViewHolder)holder).checkBox.setChecked(false);
            }
        } else {
            ((HomeArticleViewHolder)holder).checkBox.setVisibility(View.GONE);
        }

        //点击checkbox
        ((HomeArticleViewHolder)holder).checkBox.setOnClickListener(v -> {
            boolean isChecked = ((HomeArticleViewHolder)holder).checkBox.isChecked();

            doInCheckStateChanged(isChecked, position,((HomeArticleViewHolder)holder).checkBox.getTag(),
                    list.get(position).getId());
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
        @BindView(R.id.cb_item_home_article_check_box)
        CheckBox checkBox;

        public HomeArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 设置CheckBox是否可见
     *
     * @param isShowCheckBox 是否可见
     */
    public void setIsShowCheckBox(boolean isShowCheckBox) {
        this.isShowCheckBox = isShowCheckBox;
    }

    /**
     * 重置操作
     */
    public void reset() {
        mTagList.clear();
        for (int i = 0; i < mCheckList.size(); i++) {
            mCheckList.set(i, false);   //全部设为未选中
        }
        mIdList.clear();
    }

    /**
     * 全选
     */
    public void selectAll() {
        mTagList.clear();
        mIdList.clear();
        for (int i = 0; i < mCheckList.size(); i++) {
            mCheckList.set(i, true);
            mTagList.add(i);
            mIdList.add(list.get(i).getId());
        }
        clickListener.selectItemChanged(mIdList.size());

        notifyDataSetChanged();
    }

    /**
     * 反选
     */
    public void selectNull() {
        mTagList.clear();
        mIdList.clear();
        for (int i = 0; i < mCheckList.size(); i++) {
            mCheckList.set(i, false);
        }
        clickListener.selectItemChanged(mIdList.size());

        notifyDataSetChanged();
    }

    /**
     * 获取要删除的tag的索引
     *
     * @param tag 要删除的tag
     * @return 对应索引
     */
    private int getRemoveTag(int tag) {
        for (int i = 0; i < mTagList.size(); i++) {
            if (mTagList.get(i) == tag) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 在改变了CheckBox的选择状态后进行的操作
     *
     * @param isChecked 当前item是否被选中
     * @param position 当前item的位置
     * @param tag 当前item的tag
     * @param id 当前item的文章id
     */
    private void doInCheckStateChanged(boolean isChecked, int position, Object tag, int id) {

        if (isChecked){
            mCheckList.set(position, true);     //设置为选中
            if (!mIdList.contains(id)) {
                mIdList.add(id);  //将当前选中的item的id加入到idList中
            }
            if(!mTagList.contains((Integer) tag)){
                mTagList.add(position);     //选中item后，若tags集合还未添加该item，则添加
            }
        }else{
            mCheckList.set(position, false);
            for (int i = 0; i < mIdList.size(); i++) {
                if (mIdList.get(i) == id) {
                    mIdList.remove(i);  //从idList中移除该id
                }
            }
            if(mTagList.contains((Integer) tag)){
                mTagList.remove(getRemoveTag(position));  //否则在tags集合中移除该item
            }
        }

        clickListener.selectItemChanged(mIdList.size());    //当前选中的item数
    }
}
