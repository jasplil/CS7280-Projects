import java.util.*;

/**
 * LeafNode class stores the actual data
 */
public class LeafNode<K extends Comparable<K>, T> extends Node<K, T> {
    protected List<T> values;
    protected LeafNode<K, T> next;
    protected LeafNode<K, T> prev;

    /**
     * Constructor
     */
    public LeafNode(K key, T value) {
        isLeaf = true;
        keys = new ArrayList<>();
        values = new ArrayList<>();

        keys.add(key);
        values.add(value);
    }

    public LeafNode(List<K> newKeys, List<T> newValues) {
        isLeaf = true;
        keys = new ArrayList<K>(newKeys);
        values = new ArrayList<T>(newValues);
    }

    // Node value is unique
    public void insertSorted(K key, T value) {
        boolean isDuplicate = isDuplicated(key, value);
        if (!isDuplicate) {
            int l = 0;
            int r = keys.size();

            while (l < r) {
                int m = l + (r - l) / 2;
                if (keys.get(m).compareTo(key) < 0) {
                    l = m + 1;
                } else {
                    r = m;
                }
            }

            keys.add(l, key);
            values.add(l, value);
        }
    }

    public boolean isDuplicated(K key, T value) {
        int l = 0;
        int r = keys.size();

        while (l < r) {
            int m = l + (r - l) / 2;
            if (keys.get(m).compareTo(key) == 0) {
                keys.set(m, key);
                values.set(m, value);
                return true;
            } else if (keys.get(m).compareTo(key) < 0) {
                l = m + 1;
            } else {
                r = m;
            }
        }

        return false;
    }
}
