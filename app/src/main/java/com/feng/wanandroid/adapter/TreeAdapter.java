package com.feng.wanandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.feng.wanandroid.entity.data.TreeData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public class TreeAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<TreeData> mTreeDataList;
    private OnClickListener onClickListener;

    public interface OnClickListener {
        /**
         * @param name 一级分类名
         * @param childNames    二级分类名
         * @param ids   二级分类id
         */
        void clickItem(String name, List<String> childNames, List<Integer> ids);
    }

    public TreeAdapter(Context mContext, List<TreeData> mTreeDataList) {
        this.mContext = mContext;
        this.mTreeDataList = mTreeDataList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TreeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tree, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TreeViewHolder)holder).name.setText(mTreeDataList.get(position).getName());

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mTreeDataList.get(position).getChildrenList().size(); i++) {
            builder.append(mTreeDataList.get(position).getChildrenList().get(i).getName());
            builder.append("   ");
        }
        ((TreeViewHolder)holder).childrenName.setText(builder.toString());

        holder.itemView.setOnClickListener(v -> {
            List<String> childNames = new ArrayList<>();
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < mTreeDataList.get(position).getChildrenList().size(); i++) {
                childNames.add(mTreeDataList.get(position).getChildrenList().get(i).getName());
                ids.add(mTreeDataList.get(position).getChildrenList().get(i).getId());
            }
            onClickListener.clickItem(mTreeDataList.get(position).getName(), childNames, ids);
        });
    }

    @Override
    public int getItemCount() {
        return mTreeDataList.size();
    }

    class TreeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_tree_name)
        TextView name;
        @BindView(R.id.tv_item_tree_children_name)
        TextView childrenName;

        public TreeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
