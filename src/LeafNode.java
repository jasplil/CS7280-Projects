import java.util.*;

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

    public void insertSorted(K key, T value) {
        // Find the index to insert
    }
}
