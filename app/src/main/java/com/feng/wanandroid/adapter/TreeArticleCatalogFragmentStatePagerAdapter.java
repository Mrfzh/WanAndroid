package com.feng.wanandroid.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.feng.wanandroid.view.fragment.TreeArticleCatalogFragment;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/3/5
 */
public class TreeArticleCatalogFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mTitleList;
    private List<TreeArticleCatalogFragment> mFragmentList;

    public TreeArticleCatalogFragmentStatePagerAdapter(FragmentManager fm
            , List<String> mTitleList, List<TreeArticleCatalogFragment> mFragmentList) {
        super(fm);
        this.mTitleList = mTitleList;
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
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
