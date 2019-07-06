package com.talnex.wrongsbook.Utils;

import com.talnex.wrongsbook.Beans.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TreeUtil {
    public static Node currentNode;

    public static int GAP = 55;
    public static int CENG_CAP = 150;

    public static int treeHeight;
    public static Stack<Node> stack = new Stack<>();

    public static Node mindTree;
    public static Map<String, Node> map_IDtoClass = new HashMap<>();
    public static List<Node> list_noChildren = new ArrayList<>();

    public static void initUtil(Node node) {
        mindTree = node;
    }

    public static void loadAllNode(Node node) {
        map_IDtoClass.put(node.id, node);
        List<Node> children = node.children;
        if (children.size() == 0) {
            list_noChildren.add(node);
        } else {
            for (int i = 0; i < children.size(); i++) {
                Node child = children.get(i);
                if (!child.url.equals("")) child.type = 1;
                child.no = i;
                //child.rank = i;
                loadAllNode(child);
            }
        }
    }

    /**
     * 根据计算好的offset计算xy值
     *
     * @param node
     */
    public static void computeXY(Node node) {
        List<Node> children = new ArrayList<>();
        children = node.children;
        int x = node.treeParm.leftpoint_x + CENG_CAP;
        int y = node.treeParm.leftpoint_y - node.treeParm.offset_up;
        if (node.hasChildren()) {
            children.get(0).treeParm.leftpoint_x = x;
            children.get(0).treeParm.leftpoint_y = y;

            if (children.get(0).hasChildren()) {
                computeXY(children.get(0));
            }
            for (int i = 1; i < children.size(); i++) {
                y = y + getDownOffSet(children.get(i - 1)) + 2 * GAP + getUpOffSet(children.get(i));
                children.get(i).treeParm.leftpoint_x = x;
                children.get(i).treeParm.leftpoint_y = y;
                if (children.get(i).hasChildren()) {
                    computeXY(children.get(i));
                }
            }
        }

    }

    public static void computeOffSet() {
        Collections.sort(list_noChildren, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                int res = o1.id.compareTo(o2.id);
                if (res == -1) {
                    return -1;
                } else if (res == 1) {
                    return 1;
                }
                return 0;
            }
        });

        for (Node child :
                list_noChildren) {
            child.treeParm.offset_down = 0;
            child.treeParm.offset_up = 0;
        }

        //TODO: 对这棵树进行后序遍历

        Node node = mindTree;
        while (node.hasChildren()) {
            stack.push(node);
            node = node.children.get(0);
        }

        re_compute(node);
        treeHeight = getDownOffSet(node) + getUpOffSet(node);
    }

    /**
     * 递归求各个节点的上下偏移量
     *
     * @param node
     * @return
     */
    private static void re_compute(Node node) {
        computeBP(node);
        List<String> brother_ID = getDownBrotherID(node);
        if (brother_ID != null) {
            for (String brother :
                    brother_ID) {
                Node bro = map_IDtoClass.get(brother);
                if (bro.hasChildren()) {
                    while (bro.hasChildren()) {
                        stack.push(bro);
                        bro = bro.children.get(0);
                    }
                    re_compute(bro);
                } else {
                    computeBP(bro);
                }
            }
            if (!stack.isEmpty()) re_compute(stack.pop());

        } else {
            if (!stack.isEmpty()) re_compute(stack.pop());
        }
    }

    /**
     * 计算一个子孙都准备好的节点的上下偏移量
     *
     * @param node
     */

    public static void computeBP(Node node) {
        if (node != null) {
            int offset_up = 0;
            int offset_down = 0;
            List<Node> children = node.children;
            int size = children.size();
            int mid = children.size() / 2;
            if (size == 0 || size == 1) {
                node.treeParm.offset_down = 0;
                node.treeParm.offset_up = 0;
            } else if (children.size() % 2 == 0) {
                /**
                 * TODO:计算上偏移
                 */
                offset_up += getDownOffSet(children.get(0));
                for (int i = 1; i < mid; i++) {
                    offset_up += getUpOffSet(children.get(i));
                    offset_up += getDownOffSet(children.get(i));
                    offset_up += GAP * 2;
                }
                offset_up += GAP;

                /**
                 * TODO:计算下偏移
                 */
                offset_down += getUpOffSet(children.get(size - 1));
                for (int i = mid; i < size - 1; i++) {
                    offset_down += getUpOffSet(children.get(i));
                    offset_down += getDownOffSet(children.get(i));
                    offset_down += GAP * 2;
                }
                offset_down += GAP;

                node.treeParm.offset_down = offset_down;
                node.treeParm.offset_up = offset_up;


            } else {
                //上偏移
                offset_up += getDownOffSet(children.get(0));
                for (int i = 1; i < mid; i++) {
                    offset_up += getUpOffSet(children.get(i));
                    offset_up += getDownOffSet(children.get(i));
                    offset_up += GAP * 2;
                }
                offset_up += GAP * 2;

                //下偏移
                offset_down += getUpOffSet(children.get(size - 1));
                for (int i = mid + 1; i < size - 1; i++) {
                    offset_down += getUpOffSet(children.get(i));
                    offset_down += getDownOffSet(children.get(i));
                    offset_down += GAP * 2;
                }
                offset_down += GAP * 2;

                node.treeParm.offset_down = offset_down;
                node.treeParm.offset_up = offset_up;

            }
        } else {

        }

    }

    /**
     * 计算一个节点及之后节点的总下偏移量
     *
     * @param node
     * @return
     */
    public static int getDownOffSet(Node node) {
        int res = 0;
        int size;
        List<Node> children = node.children;
        size = children.size();

        res += node.treeParm.offset_down;

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
     *
     * @param node
     * @return
     */
    public static int getUpOffSet(Node node) {
        int res = 0;
        int size;
        List<Node> children = node.children;
        size = children.size();

        res += node.treeParm.offset_up;

        if (size == 0) {
            return 0;
        } else {
            node = children.get(0);
            res += getUpOffSet(node);
        }

        return res;
    }

    /**
     * 返回兄弟节点的ID
     *
     * @param node
     * @return
     */

    private static List<String> getDownBrotherID(Node node) {

        if (node == null) {
            return null;
        }
        if (node.parent == null) {
            return null;
        }
        Node father = map_IDtoClass.get(node.parent);
        List<String> ids = new ArrayList<>();
        if (father != null) {
            ids = father.getChildrenID();
            if (ids != null) {
                if (ids.size() > 1) {
                    int index = ids.indexOf(node.id);
                    for (int i = 0; i <= index; i++) {
                        ids.remove(0);
                    }
                    return ids;
                } else return null;
            } else return null;
        }
        return null;
    }

    /**
     * 根据绘制的view重新调整x坐标
     *
     * @param node
     * @param lastadd
     */
    public static void adjustX(Node node, int lastadd) {
        if (node.treeParm.width > 100) {
            int add = node.treeParm.width - 100 + lastadd;
            if (node.hasChildren()) {
                for (Node child :
                        node.children) {
                    if (child.hasChildren()) {
                        child.treeParm.leftpoint_x += add;
                        child.treeParm.rightpoint_x += add;
                        child.treeParm.center_x += add;
                        adjustX(child, add);
                    } else {
                        child.treeParm.leftpoint_x += add;
                        child.treeParm.rightpoint_x += add;
                        child.treeParm.center_x += add;
                    }
                }
            }
        } else {
            if (node.hasChildren()) {
                for (Node child :
                        node.children) {
                    if (child.hasChildren()) {
                        child.treeParm.leftpoint_x += lastadd;
                        child.treeParm.rightpoint_x += lastadd;
                        child.treeParm.center_x += lastadd;
                        adjustX(child, lastadd);
                    } else {
                        child.treeParm.leftpoint_x += lastadd;
                        child.treeParm.rightpoint_x += lastadd;
                        child.treeParm.center_x += lastadd;
                    }
                }
            }
        }
    }
}