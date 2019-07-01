package com.talnex.wrongsbook.File;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.talnex.wrongsbook.Beans.Node;
import com.talnex.wrongsbook.Beans.TreeParm;
import com.talnex.wrongsbook.Utils.TreeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonUtil {

    //规定每段显示的长度
    private static int LOG_MAXLENGTH = 2000;

    public static void e(String TAG, String msg) {
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {
            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Log.d(TAG + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.d(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }

    public static String nodetoJson(Node node){
        Collection<Node> res = TreeUtil.map_IDtoClass.values();
        List<Node> list = new ArrayList<Node>(res);
        JSONArray ja = JSONArray.parseArray(JSON.toJSONString(list));
        return ja.toJSONString();
    }

    public static Node jsontoNode(String json){

        Map<String, JSONObject> stringJSONObjectHashMap = new HashMap<>();
        Map<String, Node> stringNodeMap = new HashMap<>();


        //分割原始json
        JSONArray jsonArray = JSONArray.parseArray(json);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            stringJSONObjectHashMap.put(jsonObject.getString("id"), jsonObject);
        }

        List<String> childrenids;

        for (JSONObject obj :
                stringJSONObjectHashMap.values()) {
            Node node = new Node(obj.getString("parent"));
            node.setId(obj.getString("id"));
            node.setDescription(obj.getString("description"));
            node.setInfo(obj.getString("info"));
            node.setRank(obj.getIntValue("rank"));
            node.setType(obj.getIntValue("type"));
            node.setUrl(obj.getString("url"));
            node.setTreeParm(JSON.toJavaObject(obj, TreeParm.class));
            node.setNo(obj.getIntValue("no"));
            stringNodeMap.put(node.id, node);

        }

        for (JSONObject obj :
                stringJSONObjectHashMap.values()) {
            childrenids = JSON.parseArray(obj.getString("childrenID"), String.class);
            if (childrenids != null) {
                for (String childid :
                        childrenids) {
                    stringNodeMap.get(obj.getString("id")).addChild(stringNodeMap.get(childid));
                }
            }
        }
        return stringNodeMap.get("root");
    }



}
