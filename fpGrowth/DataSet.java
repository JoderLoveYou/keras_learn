package fpGrowth;

import java.io.*;
import java.util.*;

/**
 * Created by Joder_X on 2018/6/10.
 */
public class DataSet implements Iterable {

    private Map<Set<String>,Integer> data;

    public DataSet() {
        data = new HashMap<>();
    }

    public static DataSet readFile(String filename,String separate) throws IOException {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        DataSet dataSet = new DataSet();
        String s = null;
        while ((s=br.readLine())!=null){
            Set<String> set = new HashSet<>();
            String[] line = s.split(separate);
            Arrays.stream(line).forEach(x->set.add(x));
            dataSet.data.put(set,dataSet.data.getOrDefault(set,1));
        }
        br.close();
        return dataSet;
    }
    public static DataSet readFile(String filename) throws IOException{
        return readFile(filename,",");
    }

    public Integer get(Set<String> set){
        return data.get(set);
    }

    public Integer put(Set<String> set,Integer count){
        return data.put(set,count);
    }

    public int size(){
        return data.size();
    }

    @Override
    public Iterator iterator() {
        return data.keySet().iterator();
    }

    @Override
    public String toString() {
        return "DataSet{" +
                "data=" + data +
                '}';
    }
}
