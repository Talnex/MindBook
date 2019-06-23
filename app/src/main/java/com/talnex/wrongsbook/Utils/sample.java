package com.talnex.wrongsbook.Utils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.talnex.wrongsbook.Beans.Node;

public class sample {
    public static Node getANode(){
        Node root = new Node(null);
        root.setId("root");

        Node node0 = new Node(root.id);
        node0.setId("node0");

        Node node = new Node(node0.id);
        node.setId("node");
        Node node1 = new Node(node.id);
        node1.setId("node1");
        Node node2 = new Node(node.id);
        node2.setId("node2");
        Node node3 = new Node(node1.id);
        node3.setId("node3");
        Node node4 = new Node(node1.id);
        node4.setId("node4");
        Node node5 = new Node(node2.id);
        node5.setId("node5");
        Node node6 = new Node(node2.id);
        node6.setId("node6");
        node6.type = 1;

        node.addChild(node1);
        node.addChild(node2);

        node1.addChild(node3);
        node1.addChild(node4);

        node2.addChild(node5);
        node2.addChild(node6);

        Node n = new Node(node0.id);
        n.setId("n");
        Node n1 = new Node(n.id);
        n1.setId("n1");
        Node n2 = new Node(n.id);
        n2.setId("n2");

        Node n3 = new Node(n1.id);
        n3.setId("n3");
        Node n4 = new Node(n1.id);
        n4.setId("n4");

        n1.addChild(n3);
        n1.addChild(n4);

        n.addChild(n1);
        n.addChild(n2);

        node0.addChild(node);
        node0.addChild(n);

        Node o = new Node(node0.id);
        o.setId("o");
        Node o1 = new Node(o.id);
        o1.setId("o1");
        Node o2 = new Node(o.id);
        o2.setId("o2");
        Node o3 = new Node(o1.id);
        o3.setId("o3");
        Node o4 = new Node(o1.id);
        o4.setId("o4");

        o1.addChild(o3);
        o1.addChild(o4);

        o.addChild(o1);
        o.addChild(o2);

        node0.addChild(o);


        Node d = new Node(node0.id);
        d.setId("d");
        Node d1 = new Node(d.id);
        d1.setId("d1");
        Node d2 = new Node(d.id);
        d2.setId("d2");
        d2.type = 1;

        Node d3 = new Node(d2.id);
        d3.setId("d3");
        d2.addChild(d3);

        d.addChild(d1);
        d.addChild(d2);

        node0.addChild(d);


        Node a = new Node(root.id);
        a.setId("a");
        Node b = new Node(a.id);
        b.setId("b");
        Node c = new Node(a.id);
        b.type = 1;
        c.setId("c");
        Node b1 = new Node(b.id);
        b1.setId("b1");
        Node b2 = new Node(b.id);
        b2.setId("b2");
        Node b3 = new Node(b1.id);
        b3.setId("b3");
        Node b4 = new Node(b1.id);
        b4.setId("b4");
        Node b5 = new Node(b1.id);
        b4.type = 1;
        b5.setId("b5");
        Node b6 = new Node(b1.id);
        b6.setId("b6");
        b6.type = 1;
        Node b7 = new Node(b2.id);
        b7.setId("b7");
        Node b8 = new Node(b2.id);
        b8.setId("b8");


        Node c1 = new Node(c.id);
        c1.setId("c1");
        Node c2 = new Node(c.id);
        c2.setId("c2");
        Node c3 = new Node(c.id);
        c3.setId("c3");
        c3.type = 1;
        Node c4 = new Node(c3.id);
        c4.setId("c4");
        Node c5 = new Node(c3.id);
        c5.setId("c5");
        Node c6 = new Node(c3.id);
        c6.setId("c6");

        b1.addChild(b3);
        b1.addChild(b4);
        b1.addChild(b5);
        b1.addChild(b6);

        b2.addChild(b7);
        b2.addChild(b8);

        b.addChild(b1);
        b.addChild(b2);

        c3.addChild(c4);
        c3.addChild(c5);
        c3.addChild(c6);

        c.addChild(c1);
        c.addChild(c2);
        c.addChild(c3);

        a.addChild(b);
        a.addChild(c);

        root.addChild(node0);
        root.addChild(a);

        return root;
    }
}
