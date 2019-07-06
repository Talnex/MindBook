package com.talnex.wrongsbook.Beans;

import com.talnex.wrongsbook.Utils.UUID;

import java.util.ArrayList;
import java.util.List;


/**
 * 思维导图节点类，里面存储各种信息，在绘图中最重要的是TreeParm类，里面有节点的各种view信息
 *
 */
public class Node {
    public String id;
    public int type;
    public String info = "";
    public String description;
    public String url = "";
    public List<Node> children = new ArrayList<>();
    public int rank = 1;
    public int no;
    public TreeParm treeParm = new TreeParm();
    public List<String> keywords = null;
    public String parent;

    public void setId(String id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Node(String parent) {
        this.parent = parent;
        id = UUID.generateShortUuid();
        type = 0;

    }

    public Node(int type, String info, int rank, String description, String url, List<Node> children, TreeParm treeParm, List<String> keywords, String parent) {
        id = UUID.getUUID();
        this.type = type;
        this.info = info;
        this.rank = rank;
        this.description = description;
        this.url = url;
        this.children = children;
        this.treeParm = treeParm;
        this.keywords = keywords;
        this.parent = parent;
    }

    public String getId() {
        return id;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }


    public void setInfo(String info) {
        this.info = info;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void addChild(Node node, int rank) {
        children.add(rank, node);
    }

    public void addChild(Node node) {
        children.add(node);
    }

    public void deleteChild(String id) {
        int i = 0;
        for (Node node :
                children) {
            if (node.getId().equals(id)) {
                children.remove(i);
            }
            i++;
        }
    }

    public TreeParm getTreeParm() {
        return treeParm;
    }

    public void setTreeParm(TreeParm treeParm) {
        this.treeParm = treeParm;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }


    public boolean hasChildren() {
        if (children.size() == 0) return false;
        return true;
    }

    /**
     * fastJSON 序列化时不是按照类的成员变量，而是通过get方法，这样可以把嵌套的子节点类型换成String型的ID
     * @return 孩子节点的ID
     */
    public List<String> getChildrenID() {
        List<String> ids = new ArrayList<>();
        for (Node child :
                children) {
            ids.add(child.id);
        }
        if (ids.size() == 0) return null;
        return ids;
    }

}
