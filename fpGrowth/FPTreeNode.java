package fpGrowth;

import java.util.*;

/**
 * Created by Joder_X on 2018/6/10.
 */
public class FPTreeNode{

    private String name;
    private int count;
    private FPTreeNode nodeList;
    private FPTreeNode parent;
    private Map<String,FPTreeNode> children;

    public FPTreeNode() {
        children = new HashMap<>();
    }

    public FPTreeNode(String name, int count, FPTreeNode parent) {
        this();
        this.name = name;
        this.count = count;
        this.parent = parent;
    }

    public void add(int count){
        this.count+=count;
    }
    public void display(){
        display(1);
    }

    private void display(int index){
        index = index>1?index:1;
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<index;i++)sb.append('\t');
        sb.append(name).append(' ').append(count);
        System.out.println(sb);
        for (String key:children.keySet()){
            children.get(key).display(index+1);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public FPTreeNode getNodeList() {
        return nodeList;
    }

    public void setNodeList(FPTreeNode nodeList) {
        this.nodeList = nodeList;
    }

    public FPTreeNode getParent() {
        return parent;
    }

    public void setParent(FPTreeNode parent) {
        this.parent = parent;
    }

    public Map<String, FPTreeNode> getChildren() {
        return children;
    }

    public void setChildren(Map<String, FPTreeNode> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FPTreeNode node = (FPTreeNode) o;

        return name != null ? name.equals(node.name) : node.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FPTreeNode{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", nodeList=" + nodeList +
                ", parent=" + parent +
                ", children=" + children +
                '}';
    }
}
