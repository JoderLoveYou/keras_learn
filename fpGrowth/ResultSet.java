package fpGrowth;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Joder_X on 2018/6/11.
 */
public class ResultSet implements Iterable {

    private List<ResultItem> result;
    private Map<String,Integer> index;
    private double minSupport,minConfidence;
    public ResultSet() {
        result = new ArrayList<>();
        index = new HashMap<>();
    }

    public ResultSet(double minSupport,double minConfidence) {
        this();
        this.minConfidence = minConfidence;
        this.minSupport = minSupport;
    }

    public boolean add(ResultItem item){
        if (item.getSupport()>=minSupport&&item.getConfidence()>=minConfidence){
            result.add(item);
            index.put(item.getString(),result.size()-1);
            return true;
        }
        return false;
    }

    public int indexOf(String[] list){
        return indexOf(Arrays.asList(list));
    }

    public int indexOf(List<String> list){
        StringBuilder sb = new StringBuilder();
        for (String s:list)sb.append(s).append(",");
        if (sb.length()>1)
            sb = sb.deleteCharAt(sb.length()-1);
        return index.getOrDefault(sb.toString(),-1);
    }

    /**
     *
     * @param prefixNum 选择前缀大于等于prefixNum
     * @param suffux 选择后缀为suffux，如果suffux不为空
     * @param flag 是否重新建立index，如果后面不用indexOf等函数，flag最好为false
     * @return
     */
    public ResultSet filter(int prefixNum,String suffux,boolean flag){
        result = result.stream()
                        .filter(x->x.getPrefix()
                        .size()>=prefixNum)
                        .collect(Collectors.toList());
        if (suffux!=null&&!"".equals(suffux.trim())){
            result = result.stream()
                    .filter(x->suffux.equals(x.getSuffix()))
                    .collect(Collectors.toList());
        }
        if (flag){
            index = new HashMap<>();
            for (int i=0;i<result.size();i++)index.put(result.get(i).getString(),i);
        }
        return this;
    }

    /**
     * 过滤小于置信度和支持度
     * @param confidence
     * @param support
     * @return
     */
    public ResultSet filter(double confidence,double support,boolean flag){
        result = result.stream()
                .filter(x->x.getConfidence()>=confidence && x.getSupport()>=support)
                .collect(Collectors.toList());
        if (flag){
            index = new HashMap<>();
            for (int i=0;i<result.size();i++)index.put(result.get(i).getString(),i);
        }
        return this;
    }

    public ResultItem get(int index){
        return result.get(index);
    }

    public double getParentSupport(List<String> parentList){
        int index = indexOf(parentList);
        return index!=-1?get(index).getSupport():-1.;
    }

    public List<ResultItem> get(Set<String> set){
        List<ResultItem> result = this.result.stream()
                .filter(x->set.containsAll(x.getPrefix())&&!set.contains(x.getSuffix()))
                .collect(Collectors.toList());
        return result;

    }

    @Override
    public Iterator iterator() {
        return result.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ResultItem item:result)sb.append(item.toString()).append(",\n");
        return "ResultSet{" +
                "result=\n" + sb.toString() +
                '}';
    }

    public void print(){
        System.out.println("ResultSet{");
        for (ResultItem item:result){
            System.out.println(item);
        }
        System.out.println("}");
    }
}
