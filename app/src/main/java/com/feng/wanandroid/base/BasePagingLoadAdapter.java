package com.feng.wanandroid.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feng.wanandroid.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/10
 */
public abstract class BasePagingLoadAdapter<T> extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 1;      //普通item
    private static final int TYPE_BOTTOM = 2;    //底部加载item
    private static final String TAG = "fzh";

    private boolean hasMore = true;     //判断是否有多页
    private boolean isLoading = false;   //是否在加载中

    private int loadState;                       //加载状态
    private static final int STATE_LASTED = 2;   //没有更多了
    private static final int STATE_ERROR = 3;    //加载中失败

    protected List<T> list;
    protected Context context;
    private LoadMoreListener loadMoreListener;

    public interface LoadMoreListener {
        void loadMore();    //回调加载更多操作
    }

    public BasePagingLoadAdapter(Context context, List<T> list, LoadMoreListener loadMoreListener) {
        this.context = context;
        this.list = list;
        this.loadMoreListener = loadMoreListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_BOTTOM) {
            return new BottomViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bottom, parent, false));
        } else {
            return setItemViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (TYPE_BOTTOM == getItemViewType(position)) {

            final TextView bottomText = ((BottomViewHolder) holder).content;

            switch (loadState) {
                case STATE_LASTED:
                    bottomText.setText("没有更多了");
                    holder.itemView.setOnClickListener(null);
                    hasMore = false;
                    break;
                case STATE_ERROR:
                    bottomText.setText("加载失败，请点击重试");
                    holder.itemView.setOnClickListener(v -> {
                        bottomText.setText("加载中......");
                        LoadingMore();
                    });
                    hasMore = true;
                    break;
                default:
                    break;
            }
        } else {
            onBindItemViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < getPageCount()){
            hasMore = false;
            return list.size();
        } else {
            return list.size() + 1; //比数据量多一项（加载项）
        }
    }

    /**
     * 设置每个item的View类型（即普通item和底部加载item的位置）
     *
     * @param position item索引
     * @return View类型
     */
    @Override
    public int getItemViewType(int position) {
        if (list.size() < getPageCount()) {
            return TYPE_ITEM;
        } else {
            if (position == list.size()) {
                return TYPE_BOTTOM;
            } else {
                return TYPE_ITEM;
            }
        }
    }

    class BottomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_bottom_text)
        TextView content;

        public BottomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean loading) {
        isLoading = loading;
    }

    /**
     * 设置加载状态为加载失败
     */
    public void setErrorStatus() {
        loadState = STATE_ERROR;
        notifyItemChanged(list.size());
        setIsLoading(false);
    }

    /**
     * 设置加载状态为没有更多
     */
    public void setLastedStatus() {
        loadState = STATE_LASTED;
        notifyItemChanged(list.size());
    }

    /**
     * 加载操作
     */
    public void LoadingMore() {
        Log.d(TAG, "LoadingMore: run");
        if (loadMoreListener != null)
            loadMoreListener.loadMore();
        else
            throw new RuntimeException("LoadMoreListener不能为空");
    }

    /**
     * 获取了新的页数后更新列表
     */
    public void updateList() {
        notifyItemChanged(list.size());
//        notifyDataSetChanged();
        setIsLoading(false);
    }

    /**
     * 获取每一页的item数
     *
     * @return item数量
     */
    protected abstract int getPageCount();

    /**
     * 获取普通item的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract RecyclerView.ViewHolder setItemViewHolder(ViewGroup parent, int viewType);

    /**
     * 普通item绑定ViewHolder
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);
}
