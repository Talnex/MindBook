package com.talnex.wrongsbook.Utils;

import java.util.HashMap;
import java.util.Map;

public class ViewIds {
    private static Map<String ,Integer> map = new HashMap<>();
    public static int getViewIDfromNodeId(String ID){
        return map.get(ID);
    }
    public static void putViewID(String nodeID,int viewID){
        map.put(nodeID,viewID);
    }
}
