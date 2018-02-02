package cn.edu.lingnan.sdk.algorithms.ahoCorasick;

import cn.edu.lingnan.pojo.Vocab;
import java.util.*;

/**
 * Created by Administrator on 2018/1/30.
 * ac自动机算法所放置的包
 * @see cn.edu.lingnan.sdk.algorithms.ahoCorasick
 */
public class AhoCorasick {

    //结点
    public class Node implements Cloneable{
        private char code = 0;//字符
        private Node fail = null;//失配指针。
        private boolean end = false;//标注是否是单词的结尾
        private int depth;//单词中某字所处的深度
        private List<Node> children = new ArrayList<>();
        private Node(){}
        private Node(char code){this.code = code;}

        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
    //根节点
    Node root = new Node();
    /**
     * 初始化ac自动机
     * 输入的需要：经过
     * 字典序排序的文本
     * @param list
     */
    public void append(List<Vocab> list){
        for(Vocab vocab: list){
            String word = vocab.getContent();
            this.init(word);
        }
        this.buildFailPoint(this.root);
    }

    /**
     * 初始化ac自动机
     * 输入的需要：经过
     * 字典序排序的文本
     * @param words
     */
    public void append(String[] words){
        for (String word: words)
            this.init(word);
        this.buildFailPoint(this.root);
    }
    /**
     * 构建字典树
     * @param word
     * @return Node 待刷新失配指针的点
     */
    private Node init(String word){
        Node parent = this.root;
        Node flashNode = null;//待刷新失配指针的点
        for (int count = 0; count < word.length(); count++){
            char code = word.charAt(count);
            int index = this.indexOf(parent, code);
            if (index == -1){//该字符不存在时添加该字符到父节点的孩子节点中
                Node child = new Node(code);
                child.depth = count + 1;//设置结点的深度
                parent.children.add(child);
                parent = child;
                if (flashNode == null) flashNode = parent;
            }
            else
                parent = parent.children.get(index);
        }
        parent.end = true;//标注该字符是单词的结尾

        return flashNode;
    }

    /**
     * 把单个单词对象添加至
     * 字典树当中，并局部构建
     * 失配指针
     * @param vocab
     */
    public void append(Vocab vocab){
        String word = vocab.getContent();
        Node flashNode = this.init(word);
        if (flashNode == null)
            return;
        this.buildFailPoint(flashNode);
    }

    /**
     * 把单个单词字符串添加至
     * 字典树当中，并局部构建
     * 失配指针
     * @param word
     */
    public void append(String word){
        Node flashNode = this.init(word);
        if (flashNode == null)
            return;
        this.buildFailPoint(flashNode);
    }

    /**
     * 构建失配指针
     * @param parent 待刷新父节点
     */
    private void buildFailPoint(Node parent){
        //Node parent = this.root;
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(parent);
        while (!nodes.isEmpty()){
            parent = nodes.remove(0);
            for (Node child: parent.children){
                if (parent == this.root){
                    child.fail = root;
                }
                else{
                    Node grandNode = parent.fail;
                    while (grandNode != null){
                        int index = this.indexOf(grandNode, child.code);
                        if (index != -1) {//找到uncle
                            child.fail = grandNode.children.get(index);
                            break;
                        }
                        else
                            grandNode = grandNode.fail;
                    }
                    if (grandNode == null)
                        child.fail = this.root;
                }
                nodes.add(child);
            }
        }
    }

    /**
     * 查看某字段是否存在该子结点
     * @param parent 父节点
     * @param code  待判定的子元素
     * @return 存在返回index, 否则返回-1
     */
    private int indexOf(Node parent,char code){
        for (int count = 0; count < parent.children.size(); count++){
            Node node = parent.children.get(count);
            if (node.code == code)
                return count;
        }
        return -1;
    }

    /**
     * 识别字符串(最长字串识别)
     * @param text 待识别的字符串序列
     * @param matchListener 识别到时进行回调
     */
    public void find(String text, MatchListener matchListener){
        Node parent = this.root;
        int start = -1, end = -1;
        text = text.concat(".");
        for (int count = 0; count < text.length(); count++){
            char letter = text.charAt(count);
            int index = this.indexOf(parent, letter);
            if (parent.end == true) {end = count;}
            if (index != -1) {
                if (start == -1){ start = count;}
                parent = parent.children.get(index);
            }
            else{//失配,指针回归
                parent = parent.fail;
                while(parent != null){
                    index = this.indexOf(parent, letter);
                    if (index != -1){
                        if (start != -1 && parent == this.root ) {
                            //System.out.println("失配：" + text.substring(start, end) + ":" + parent.code);
                            matchListener.match(text.substring(start, end), start, end);
                        }
                        start = count;
                        if (parent != this.root)
                            start = count - parent.depth;
                        parent = parent.children.get(index);
                        break;
                    }
                    else
                        //System.out.println("失配：else" + text.substring(start, end));
                        parent = parent.fail;
                }
                //找到关键词,输出
                if (parent == null){
                    if (start != -1) {
                        //System.out.println("失配：" + text.substring(start, end));
                        matchListener.match(text.substring(start, end), start, end);
                    }
                    parent = this.root;
                    start = -1;
                }
            }
        }
    }
}