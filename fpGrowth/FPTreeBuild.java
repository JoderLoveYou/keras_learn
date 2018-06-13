package fpGrowth;

import java.util.*;

/**
 * Created by Joder_X on 2018/6/11.
 */
public class FPTreeBuild {

    private DataSet datasets;
    private double minSuport;
    private double minConfidence;

    public FPTreeBuild addDataSet(DataSet datasets){
        this.datasets = datasets;
        return this;
    }

    public FPTreeBuild addMinSuport(double minSuport){
        this.minSuport = minSuport;
        return this;
    }

    public FPTreeBuild addMinConfidence(double minConfidence){
        this.minConfidence = minConfidence;
        return this;
    }

    public FPTree build(){
        if (minConfidence<0||minSuport<0)throw new IllegalArgumentException("支持度和置信度不能小于0");
        FPTree fpTree = new FPTree(minSuport,minConfidence,datasets.size());
        fpTree.setHeaderTable(new HashMap<>());
        Iterator<Set> iterator = datasets.iterator();
        while (iterator.hasNext()){
            Set<String> set = iterator.next();
            for (String s:set){
                if (fpTree.getHeaderTable().containsKey(s))
                    fpTree.getHeaderTable().get(s).add(datasets.get(set));
                else fpTree.getHeaderTable().put(s,new FPTreeNode(s,datasets.get(set),null));
            }
        }
        Iterator<Map.Entry<String,FPTreeNode>> it = fpTree.getHeaderTable().entrySet().iterator();
        while (it.hasNext()){
            FPTreeNode node = it.next().getValue();
            if (node.getCount()<fpTree.getMinCount()){
                it.remove();
            }
        }
        if (fpTree.getHeaderTable().size()==0)return null;
        FPTreeNode tree = new FPTreeNode("HEAD",0,null);
        fpTree.setTree(tree);
        iterator = datasets.iterator();
        while (iterator.hasNext()){
            Set set = iterator.next();
            List<Item> arr = new ArrayList<>(set.size());
            for (Object obj:set)
                if (fpTree.getHeaderTable().containsKey(obj.toString())) {
                    arr.add(new Item(obj.toString(),fpTree.getHeaderTable().get(obj.toString()).getCount()));
                }
            Item[] items = new Item[arr.size()];
            items = arr.toArray(items);
            Arrays.sort(items);
            updateTree(items,fpTree,tree,datasets.get(set));
        }
        return fpTree;
    }

    public FPTree createTree(DataSet datasets,
                             double minSuport,double minConfidence){
        return addDataSet(datasets).addMinSuport(minSuport).
                addMinConfidence(minConfidence).build();
    }

    private void updateTree(Item[] arr,FPTree fpTree,FPTreeNode tree,int count){
        updateTree(arr,fpTree,tree,count,0);
    }

    private void updateTree(Item[] arr,FPTree fpTree,FPTreeNode tree,int count,int index){
        if (index>=arr.length)return;
        String key = arr[index].getName();
        if (tree.getChildren().containsKey(key)) {
            tree.getChildren().get(key).add(count);

        }
        else {
            FPTreeNode node = new FPTreeNode(key,count,tree);
            tree.getChildren().put(key,node);
            if (fpTree.getHeaderTable().get(key).getNodeList()==null){
                fpTree.getHeaderTable().get(key).setNodeList(node);

            }else {
                updateHeader(node,fpTree.getHeaderTable().get(key).getNodeList());
//                System.out.println(findPrefixPath(key));
            }
        }
        updateTree(arr,fpTree,tree.getChildren().get(key),count,index+1);
    }

    private void updateHeader(FPTreeNode node,FPTreeNode list) {
        while (list.getNodeList()!=null)
            list = list.getNodeList();
        list.setNodeList(node);
    }

    public DataSet findPrefixPath(FPTree fpTree,String name){
        DataSet dataSet = new DataSet();
        FPTreeNode node = fpTree.getHeaderTable().get(name);
        while (node!=null){
            Set<String> set = new HashSet<>();
            ascendTree(node,set);
            if (set.size()>1){
                set.remove("HEAD");
                dataSet.put(set,node.getCount());
            }
            node = node.getNodeList();
        }
        return dataSet;
    }

    private void ascendTree(FPTreeNode node, Set<String> set) {
        while (node.getParent()!=null){
            set.add(node.getParent().getName());
            node = node.getParent();
        }

    }

    public ResultSet mineTree(FPTree fpTree){
        List<String> preFix = new ArrayList<>();
        List<List> freqItemList = new ArrayList<>();
        ResultSet resultSet = new ResultSet(fpTree.getMinSuport(),fpTree.getMinConfidence());
        mineTree(fpTree,preFix,freqItemList,resultSet,fpTree.getDatasize());
        return resultSet;
    }

    private void mineTree(FPTree fpTree,List<String> preFix,List<List>freqItemList,ResultSet resultSet,int datasize){
        Item[] arr = new Item[fpTree.getHeaderTable().size()];
        int i = 0;
        for (String key:fpTree.getHeaderTable().keySet()){
            arr[i++] = new Item(key,fpTree.getHeaderTable().get(key).getCount());
        }
        Arrays.sort(arr);
        for (i=arr.length-1;i>=0;i--){
            int count = fpTree.getHeaderTable().get(arr[i].getName()).getCount();
            double support = (count*1.)/datasize;
            double parentSupport = resultSet.getParentSupport(preFix);
            ResultItem resultItem = new ResultItem(preFix,arr[i].getName(),
                    parentSupport<=0?1.:support/parentSupport,support);
            if (resultSet.add(resultItem)){
                List<String> newFreqSet = new ArrayList<>(preFix);
                newFreqSet.add(arr[i].getName());
                freqItemList.add(newFreqSet);
                DataSet condPattBases = findPrefixPath(fpTree, arr[i].getName());
                FPTree tree = createTree(condPattBases, fpTree.getMinSuport(), fpTree.getMinConfidence());
                if (tree != null) {
                    mineTree(tree, newFreqSet, freqItemList, resultSet, datasize);
                }
            }
        }
    }

}
