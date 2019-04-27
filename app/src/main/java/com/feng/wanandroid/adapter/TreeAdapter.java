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
public class TreeAdapter extends RecyclerView.Adapter<TreeAdapter.TreeViewHolder> {

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
    public TreeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TreeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tree, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TreeViewHolder treeViewHolder, int i) {
        treeViewHolder.name.setText(mTreeDataList.get(i).getName());

        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < mTreeDataList.get(i).getChildrenList().size(); j++) {
            builder.append(mTreeDataList.get(i).getChildrenList().get(j).getName());
            builder.append("   ");
        }
        treeViewHolder.childrenName.setText(builder.toString());

        treeViewHolder.itemView.setOnClickListener(v -> {
            List<String> childNames = new ArrayList<>();
            List<Integer> ids = new ArrayList<>();
            for (int j = 0; j < mTreeDataList.get(i).getChildrenList().size(); j++) {
                childNames.add(mTreeDataList.get(i).getChildrenList().get(j).getName());
                ids.add(mTreeDataList.get(i).getChildrenList().get(j).getId());
            }
            onClickListener.clickItem(mTreeDataList.get(i).getName(), childNames, ids);
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
