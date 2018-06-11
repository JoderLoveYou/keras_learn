package fpGrowth;

/**
 * Created by Joder_X on 2018/6/10.
 */
public class Item implements Comparable<Item> {

    private String name;
    private int value;

    public Item() {
    }

    public Item(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * 从大到小
     * @param o
     * @return
     */
    @Override
    public int compareTo(Item o) {
        return o.value-this.value;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
