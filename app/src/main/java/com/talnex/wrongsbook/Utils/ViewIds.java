package com.talnex.wrongsbook.Utils;

import com.talnex.wrongsbook.Beans.Node;
import com.talnex.wrongsbook.MindMap.DrawGeometryView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewIds {
    public static Map<Node ,Integer> map_NodetoViewID = new HashMap<>();
    public static Map<Integer, Node> map_ViewIdtoNodeId = new HashMap<>();
    public static List<Integer> list_Lines = new ArrayList<>();

    public static int getViewIDfromNode(Node node){
        return map_NodetoViewID.get(node);
    }
    public static void putViewID(Node node,int viewID){
        map_NodetoViewID.put(node,viewID);
    }

    public static Node getNodefromViewId(int viewid){
        return map_ViewIdtoNodeId.get(viewid);
    }
    public static void putNode(int viewID,Node node){
        map_ViewIdtoNodeId.put(viewID,node);
    }

}
