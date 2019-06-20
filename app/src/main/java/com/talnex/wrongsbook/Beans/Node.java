package com.talnex.wrongsbook.Beans;

import com.talnex.wrongsbook.Utils.UUID;

import java.util.List;


public class Node {
    private String id;
    private int type;
    private String info;
    private String description;
    private String url;
    private List<Node> children = null;
    private int rank;
    public TreeParm treeParm = new TreeParm();
    private List<String> keywords = null;
    private String parent;

    public void setId(String id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }


    public Node(String parent) {
        this.parent = parent;
        id = UUID.getUUID();
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

    public void deleteChild() {

    }

}
