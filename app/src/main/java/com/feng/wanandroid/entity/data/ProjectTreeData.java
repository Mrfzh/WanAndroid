package com.feng.wanandroid.entity.data;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/6/4
 *
 * 项目分类数据
 */
public class ProjectTreeData {
    private List<Integer> idList;   //项目id
    private List<String> nameList;  //项目名

    public ProjectTreeData(List<Integer> idList, List<String> nameList) {
        this.idList = idList;
        this.nameList = nameList;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }
}
