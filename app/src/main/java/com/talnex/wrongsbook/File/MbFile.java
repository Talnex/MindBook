package com.talnex.wrongsbook.File;

import com.talnex.wrongsbook.Beans.Node;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class MbFile {
    public static String FILENAME = "hello";
    public static String Mbfile = "/sdcard/mindbook/";
    static String filenameTemp = Mbfile + FILENAME + ".mb";
    public static int height = 1;
    public static String MdFile = "/sdcard/mindbook/1.md";
    public static String newMdFile = "/sdcard/mindbook/2.md";
    private static String mdfileString = "";

    public static boolean hasfile() {
        File file = new File(Mbfile);
        return file.exists();
    }

    public static void writeTreeFile(String text) {
        File file = new File(Mbfile);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {

            }
        }
        File dir = new File(filenameTemp);
        if (!dir.exists()) {
            try {
                dir.createNewFile();
            } catch (Exception e) {
            }
        }

        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(filenameTemp, false);//
            // 创建FileWriter对象，用来写入字符流
            bw = new BufferedWriter(fw); // 将缓冲对文件的输出

            bw.write(text + "\n"); // 写入文件
            bw.newLine();
            bw.flush(); // 刷新该流的缓冲
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                bw.close();
                fw.close();
            } catch (IOException e1) {

            }
        }


    }

    public static String readTreeFile() {
        try {
            FileInputStream fis = new FileInputStream(filenameTemp);
            byte[] buff = new byte[1024];
            int hasRead = 0;
            StringBuffer sb = new StringBuffer();
            while ((hasRead = fis.read(buff)) > 0) {
                sb.append(new String(buff, 0, hasRead));
            }
            fis.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取Md文件并转化成Node类型，采用递归的方法
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    public static Node readMdFile(String path) throws IOException {
        File file = new File(path);
        Node root = new Node(null);
        root.setId("root");
        BufferedReader reader = null;
        Stack<Node> nodeStack = new Stack<>();
        try {
            reader = new BufferedReader(new FileReader(file));

            root.setInfo(reader.readLine().substring(2));
            reader.readLine();
            nodeStack.push(root);

            String temp;
            int current = 1;
            int last = 0;
            String info;
            while ((temp = reader.readLine()) != null) {
                last = current;
                current = getMean(temp).charAt(0);
                info = getMean(temp).substring(1);
                if (current != 0) {
                    Node node = new Node(null);
                    node.setInfo(info);
                    if (current > last) {
                        nodeStack.peek().addChild(node);
                        node.setParent(nodeStack.peek().id);
                        nodeStack.push(node);
                    } else if (current == last) {
                        nodeStack.pop();
                        node.setParent(nodeStack.peek().id);
                        nodeStack.peek().addChild(node);
                        nodeStack.push(node);
                    } else {
                        for (int i = 0; i <= last - current; i++) {
                            nodeStack.pop();
                        }
                        node.setParent(nodeStack.peek().id);
                        nodeStack.peek().addChild(node);
                        nodeStack.push(node);
                    }

                    reader.readLine();
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return root;
    }

    /**
     * 获取一行Md文本的含义及内容
     * @param line 一行文本数据
     * @return 层级+内容
     */
    public static String getMean(String line) {
        if (line.charAt(0) == '#') {
            int i;
            for (i = 0; line.charAt(i) != ' '; i++) {
            }
            return i + line.substring(i + 1, line.length());

        } else if (line.charAt(0) == '-') {
            return 0 + line.substring(2, line.length());
        }
        return null;
    }


    /**
     * 将导图节点导出成Md文件所需要的String并存在mdfileString里
     * @param node
     */
    public static void exportMdFile(Node node) {
        for (int i = 1; i <= height; i++) {
            mdfileString = mdfileString + '#';
        }
        mdfileString += " ";
        mdfileString += node.info;
        mdfileString += "\n\n";
        if (node.hasChildren()) {
            height++;
            for (Node child :
                    node.children) {
                exportMdFile(child);
            }
            height--;
        }
    }

    /**
     * 将mdfileString写入到文件里
     * @param path
     * @throws IOException
     */
    public static void writeMdFile(String path) throws IOException {
        FileWriter writer = new FileWriter(path);
        writer.write(mdfileString);
        writer.close();
    }


    public static void ziptoMbFile() {

    }

    public static void unzipMbFile() {

    }
}
