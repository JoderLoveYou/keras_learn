package fpGrowth;

import java.util.*;

/**
 * Created by Joder_X on 2018/6/10.
 */
public class FPTree {

    private FPTreeNode tree;
    private Map<String,FPTreeNode> headerTable;
    private int minSuport,minConfidence;
    private int datasize;

    public FPTree() {
    }

    public FPTree(int minSuport, int minConfidence,int datasize) {
        this.minSuport = minSuport;
        this.minConfidence = minConfidence;
        this.datasize = datasize;
    }

    public FPTreeNode getTree() {
        return tree;
    }

    public void setTree(FPTreeNode tree) {
        this.tree = tree;
    }

    public Map<String, FPTreeNode> getHeaderTable() {
        return headerTable;
    }

    public void setHeaderTable(Map<String, FPTreeNode> headerTable) {
        this.headerTable = headerTable;
    }

    public int getMinSuport() {
        return minSuport;
    }

    public void setMinSuport(int minSuport) {
        this.minSuport = minSuport;
    }

    public int getMinConfidence() {
        return minConfidence;
    }

    public void setMinConfidence(int minConfidence) {
        this.minConfidence = minConfidence;
    }

    public int getDatasize() {
        return datasize;
    }

    public void setDatasize(int datasize) {
        this.datasize = datasize;
    }
}
