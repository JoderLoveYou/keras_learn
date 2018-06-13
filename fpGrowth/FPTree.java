package fpGrowth;

import java.util.*;

/**
 * Created by Joder_X on 2018/6/10.
 */
public class FPTree {

    private FPTreeNode tree;
    private Map<String,FPTreeNode> headerTable;
    private double minSuport,minConfidence;
    private int minCount;
    private int datasize;

    public FPTree() {
    }

    public FPTree(double minSuport, double minConfidence, int datasize) {
        this.minSuport = minSuport;
        this.minConfidence = minConfidence;
        this.datasize = datasize;
        this.minCount = (int) (datasize*minSuport);
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

    public double getMinSuport() {
        return minSuport;
    }

    public void setMinSuport(double minSuport) {
        this.minSuport = minSuport;
    }

    public double getMinConfidence() {
        return minConfidence;
    }

    public void setMinConfidence(double minConfidence) {
        this.minConfidence = minConfidence;
    }

    public int getDatasize() {
        return datasize;
    }

    public void setDatasize(int datasize) {
        this.datasize = datasize;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }
}
