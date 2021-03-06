package com.feng.wanandroid.adapter;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Feng Zhaohao
 * Created on 2019/2/27
 */
public class CollectedArticleAdapter extends BasePagingLoadAdapter<ArticleData> {

    private List<Boolean> mCheckList;   //存储item是否被选中
    private List<Integer> mIdList;   //存储选中的item的id

    private List<Integer> mTagList;     //用于解决holder复用导致的多选问题
    private boolean isShowCheckBox = false;     //设置是否显示CheckBox

    public CollectedArticleAdapter(Context context, List<ArticleData> list, LoadMoreListener loadMoreListener, List<Boolean> mCheckList, List<Integer> mIdList) {
        super(context, list, loadMoreListener);
        this.mCheckList = mCheckList;
        this.mIdList = mIdList;
        this.mTagList = new ArrayList<>();
    }

    private OnClickListener clickListener;

    public interface OnClickListener {
        //        void clickChapter();      //点击了栏目（以后再实现）
        void clickCollect(boolean collect, int id, int position);    //点击了收藏item
        void clickItem(String link, String title, boolean isCollect, int id, int position);
        void selectItemChanged(int num);    //当选中的item数目改变时，回调该方法
        void longClickItem();
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected int getPageCount() {
        return Constant.HOME_ARTICLE_PAGE_SIZE;
    }

    @Override
    protected RecyclerView.ViewHolder setItemViewHolder(ViewGroup parent, int viewType) {
        return new CollectedArticleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_collected_article, parent, false));
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((CollectedArticleViewHolder)holder).authorName.setText(list.get(position).getAuthor());
        ((CollectedArticleViewHolder)holder).date.setText(list.get(position).getNiceDate());
        ((CollectedArticleViewHolder)holder).title.setText(list.get(position).getTitle());
        ((CollectedArticleViewHolder)holder).chapter.setText(list.get(position).getChapterName());

        ((CollectedArticleViewHolder)holder).itemView.setOnClickListener(v -> {
            //分两种情况
            //一种是普通的点击，一种是处于编辑（多选删除）状态下的点击
            if (((CollectedArticleViewHolder)holder).checkBox.getVisibility() == View.GONE) {
                clickListener.clickItem(list.get(position).getLink(), list.get(position).getTitle(),
                        list.get(position).isCollect(), list.get(position).getId(), position);    //普通点击
            } else {
                //处于编辑状态下
                boolean newCheck = !((CollectedArticleViewHolder)holder).checkBox.isChecked();
                ((CollectedArticleViewHolder)holder).checkBox.setChecked(newCheck);
                doInCheckStateChanged(newCheck, position, ((CollectedArticleViewHolder)holder).checkBox.getTag(),
                        list.get(position).getId());
            }
        });

        //设置CheckBox是否可见
        if (isShowCheckBox) {
            ((CollectedArticleViewHolder)holder).checkBox.setVisibility(View.VISIBLE);
            //针对holder复用导致的CheckBox混乱的解决
            ((CollectedArticleViewHolder)holder).checkBox.setTag(position);
            if (mTagList.contains((int)((CollectedArticleViewHolder)holder).checkBox.getTag())) {
                ((CollectedArticleViewHolder)holder).checkBox.setChecked(true);   //如果在tags集合中包括当前CheckBox，则当前CheckBox设置为选中
            } else {
                ((CollectedArticleViewHolder)holder).checkBox.setChecked(false);
            }
        } else {
            ((CollectedArticleViewHolder)holder).checkBox.setVisibility(View.GONE);
        }

        //点击checkbox
        ((CollectedArticleViewHolder)holder).checkBox.setOnClickListener(v -> {
            boolean isChecked = ((CollectedArticleViewHolder)holder).checkBox.isChecked();

            doInCheckStateChanged(isChecked, position,((CollectedArticleViewHolder)holder).checkBox.getTag(),
                    list.get(position).getId());
        });


        holder.itemView.setOnLongClickListener(v -> {

            if (mCheckList != null) {   //分情况，checklist不为空时，才有长按进入多选编辑
                ((CollectedArticleViewHolder)holder).checkBox.setChecked(true);
                doInCheckStateChanged(true, position, ((CollectedArticleViewHolder)holder).checkBox.getTag(),
                        list.get(position).getId());
                isShowCheckBox = true;
                notifyDataSetChanged();

                clickListener.longClickItem();    //回调长按事件
            }

            return true;
        });
    }

    class CollectedArticleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_collected_article_author_name)
        TextView authorName;
        @BindView(R.id.tv_collected_article_date)
        TextView date;
        @BindView(R.id.tv_collected_article_title)
        TextView title;
        @BindView(R.id.tv_collected_article_chapter)
        TextView chapter;
        @BindView(R.id.cb_item_collected_article_check_box)
        CheckBox checkBox;

        public CollectedArticleViewHolder(View itemView) {
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
