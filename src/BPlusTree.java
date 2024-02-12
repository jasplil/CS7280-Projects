import java.util.*;
import java.util.Map.Entry;

public class BPlusTree<K extends Comparable<K>, T> {
    public Node<K, T> root;
    public static final int LEAF_ORDER = 3;

    public void insert(K key, T value) {
        LeafNode<K, T> leaf = new LeafNode<K, T>(key, value);
        Entry<K, Node<K, T>> entry = new AbstractMap.SimpleEntry<K, Node<K,T>>(key, leaf);;

        if (root == null || root.keys.size() == 0) {
            root = entry.getValue();
        }

        if (root.isLeaf) {
            insertLeaf(root, entry, null);
        } else {
            //TODO: Implement insertInternal
//            insertInternal(root, entry, null);
        }
    }

    public Entry<K, Node<K, T>> insertInternal(Node<K, T> node, Map.Entry<K, Node<K,T>> entry,
                                                   Entry<K, Node<K,T>> newChildEntry) {
//        InternalNode<K, T> index = (InternalNode<K, T>) node;

        return null;
    }

    public Map.Entry<K, Node<K, T>> insertLeaf(Node<K, T> node, Map.Entry<K, Node<K,T>> entry,
                                                   Map.Entry<K, Node<K,T>> newChildEntry) {
        // Directly insert the new child into the leaf node
        LeafNode<K, T> leafNode = (LeafNode<K, T>) node;
        LeafNode<K, T> newLeafNode = (LeafNode<K, T>) entry.getValue();

        leafNode.insertSorted(entry.getKey(), newLeafNode.values.get(0));

        // If the size is greater than LEAF_ORDER, split the leaf node
//        if (leafNode.isOverflowed()) {
//            return splitLeaf(leafNode);
//        } else {
//            return null;
//        }

        return null;
    }

    /**
     * Split the leaf node into two nodes
     * @param leafNode the leaf node to be split
     * @return the new key and the new leaf node
     */
    public Map.Entry<K, Node<K, T>> splitLeaf(LeafNode<K, T> leafNode) {
        List<K> newKeys = new ArrayList<>();
        List<T> newValues = new ArrayList<>();

        // Add the second half of the keys and values to the new leaf node
        for (int i = LEAF_ORDER / 2; i < LEAF_ORDER; i++) {
            newKeys.add(leafNode.keys.get(i));
            newValues.add(leafNode.values.get(i));
        }

        // Remove the second half of the keys and values from the original leaf node
        for (int i = LEAF_ORDER / 2; i < LEAF_ORDER; i++) {
            leafNode.keys.remove(LEAF_ORDER / 2);
            leafNode.values.remove(LEAF_ORDER / 2);
        }

        // Create a new leaf node and link it to the original leaf node
        K splitKey = newKeys.get(0);
        LeafNode<K, T> newLeafNode = new LeafNode<>(newKeys, newValues);
        newLeafNode.next = leafNode.next;
        leafNode.next = newLeafNode;

        return Map.entry(splitKey, newLeafNode);
    }

    /**
     * Search the value of a key in the B+ tree
     * @param key the key to search for
     * @return the value of the key
     */
    public T search(K key) {
        if (key == null || root == null) {
            return null;
        }

        LeafNode<K,T> leaf = (LeafNode<K,T>)internalSearch(root, key);

        for (int i = 0; i < leaf.keys.size(); i++) {
            if (key.compareTo(leaf.keys.get(i)) == 0) {
                return leaf.values.get(i);
            }
        }

        return null;
    }

    private Node<K, T> internalSearch(Node<K, T> node, K key) {
        if (node.isLeaf) {
            return node;
        }

        InternalNode<K, T> internalNode = (InternalNode<K, T>) node;
        int index = 0;
        for (K k : internalNode.keys) {
            if (key.compareTo(k) < 0) {
                break;
            }
            index++;
        }

        System.out.println("index: " + index);

        return node;
    }
}
