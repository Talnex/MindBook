package com.talnex.wrongsbook.Utils;

import com.talnex.wrongsbook.Beans.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUtil {
    public static int GAP = 225;
    public static int CENG_CAP = 800;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static Node mindTree;
    public static Map<String, Node> map_idtoModle = new HashMap<>();
    public static Map<String, Node> map_noChildren = new HashMap<>();

    public static void initUtil(Node node) {
        mindTree = node;
    }

    public static void loadAllNode(Node node) {
        map_idtoModle.put(node.getId(), node);
        List<Node> children = node.getChildren();
        if (children.size() == 0) {
            map_noChildren.put(node.getId(), node);
        } else {
            for (Node child :
                    children) {
                loadAllNode(child);
            }
        }
    }

    public static void computeOffSet() {
        List<Node> children = (List<Node>) map_noChildren.values();
        for (Node child :
                children) {
            child.treeParm.setOffset_down(0);
            child.treeParm.setOffset_up(0);
        }

        String id = "";
        for (Node child :
                children) {
            if (child.getParent().equals(id)) {
            } else {
                id = child.getParent();
                computeBP(map_idtoModle.get(id));
            }

        }
    }

    private static void computeBP(Node node) {
        int offset_up = 0;
        int offset_down = 0;
        List<Node> children = node.getChildren();
        int size = children.size();
        int mid = children.size() / 2;
        if (children.size() % 2 == 0) {
            /**
             * 计算上偏移
             */
            offset_up += getDownOffSet(children.get(0));
            for (int i = 1; i < mid; i++) {
                offset_up += getUpOffSet(children.get(i));
                offset_up += getDownOffSet(children.get(i));
                offset_up += GAP;
            }
            /**
             * 计算下偏移
             */
            offset_down += getUpOffSet(children.get(size - 1));
            for (int i = mid; i < size; i++) {
                offset_down += getUpOffSet(children.get(i));
                offset_down += getDownOffSet(children.get(i));
                offset_down += GAP;
            }



        } else {

        }

    }

    /**
     * 计算一个节点及之后节点的总下偏移量
     * @param node
     * @return
     */
    public static int getDownOffSet(Node node) {
        int res = 0;
        int size;
        List<Node> children = node.getChildren();
        if (children == null) {
            size = 0;
        } else {
            size = children.size();
        }

        res += node.treeParm.getOffset_down();

        if (size == 0) {
            return 0;
        } else {
            node = children.get(size - 1);
            res += getDownOffSet(node);
        }

        return res;
    }

    /**
     * 计算一个节点及之后所有节点的总上偏移量
     * @param node
     * @return
     */
    public static int getUpOffSet(Node node) {
        int res = 0;
        int size;
        List<Node> children = node.getChildren();
        if (children == null) {
            size = 0;
        } else {
            size = children.size();
        }

        res += node.treeParm.getOffset_up();

        if (size == 0) {
            return 0;
        } else {
            node = children.get(0);
            res += getUpOffSet(node);
        }

        return res;
    }

}
