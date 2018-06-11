package fpGrowth;

import java.util.List;

/**
 * Created by Joder_X on 2018/6/11.
 */
public class ResultItem {

    private double confidence;
    private double support;
    private List<String> prefix;
    private String suffix;

    public ResultItem() {
    }

    public ResultItem(List<String> prefix, String suffix,double confidence, double support) {
        this.confidence = confidence;
        this.support = support;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public void setPrefix(List<String> prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return "ResultItem{" +
                "prefix=" + prefix +
                ", suffix='" + suffix + '\'' +
                ", confidence=" + confidence +
                ", support=" + support +
                '}';
    }

    public String getString(){
        StringBuilder sb = new StringBuilder();
        for (String s:prefix)sb.append(s).append(",");
        return sb.append(suffix).toString();
    }
}
