package com.feng.wanandroid.entity.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public class TreeData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;    //一级分类名
    private List<Children> childrenList;    //二级分类集合

    public TreeData(String name, List<Children> childrenList) {
        this.name = name;
        this.childrenList = childrenList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Children> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<Children> childrenList) {
        this.childrenList = childrenList;
    }

    public static class Children {
        private String name;    //二级分类名
        private int id;

        public Children(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
