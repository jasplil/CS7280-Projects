import java.util.*;

/**
 * BPlusTreeInternal class stores N nodes and N+1 children pointers
 */
public class InternalNode<K extends Comparable<K>, T> extends Node<K, T> {
    protected List<Node<K,T>> children;

    /**
     * Constructor
     */
    public InternalNode(K key, Node<K,T> left, Node<K,T> right) {
        isLeaf = false;
        keys = new ArrayList<>();
        children = new ArrayList<>();

        keys.add(key);
        children.add(left);
        children.add(right);
    }

    /**
     * Constructor
     * @param keys
     * @param children
     */
    public InternalNode(List<K> keys, List<Node<K,T>> children) {
        isLeaf = false;
        this.keys = keys;
        this.children = children;
    }

    /**
     * Insert a key and its right child into the internal node at the specified index
     * @param entry the key and its right child
     * @param index the index where the key and its right child should be inserted
     */
    public void insertSorted(Map.Entry<K, Node<K,T>> entry, int index) {
        K key = entry.getKey();
        Node<K,T> child = entry.getValue();

        if (index >= keys.size()) {
            keys.add(key);
            children.add(child);
        } else {
            keys.add(index, key);
            children.add(index + 1, child);
        }
    }
}
