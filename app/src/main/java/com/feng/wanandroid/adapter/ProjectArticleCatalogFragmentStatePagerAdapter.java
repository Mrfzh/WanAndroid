package com.feng.wanandroid.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.feng.wanandroid.view.fragment.ProjectArticleCatalogFragment;
import com.feng.wanandroid.view.fragment.TreeArticleCatalogFragment;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/7/8
 */
public class ProjectArticleCatalogFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mTitleList;
    private List<ProjectArticleCatalogFragment> mFragmentList;

    public ProjectArticleCatalogFragmentStatePagerAdapter(FragmentManager fm,
          List<String> mTitleList, List<ProjectArticleCatalogFragment> mFragmentList) {
        super(fm);
        this.mTitleList = mTitleList;
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
