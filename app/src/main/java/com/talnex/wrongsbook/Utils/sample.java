package com.talnex.wrongsbook.Utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.talnex.wrongsbook.Beans.Node;
import com.talnex.wrongsbook.Beans.TreeParm;

import java.util.ArrayList;
import java.util.List;

public class sample {
    public static Node getANode(){
        Node node = new Node(null);
        List<Node> children = new ArrayList<>();
        children.add(new Node(0,"info",1,"这是描述","url1",null,new TreeParm(),null,node.getId()));
        children.add(new Node(0,"info",2,"这是描述","url2",null,new TreeParm(),null,node.getId()));
        children.add(new Node(0,"info",3,"这是描述","url3",null,new TreeParm(),null,node.getId()));
        node.setChildren(children);
        node.setDescription("description");
        node.treeParm.setOffset(300);
        node.treeParm.setHight(0);
        node.setInfo("hello");
        node.setUrl("你哈克我了骄傲是看得见按实际考虑到即可拉伸的件莱卡圣诞节快乐");

        return node;
    }
}
