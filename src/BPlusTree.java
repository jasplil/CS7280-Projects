import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BPlusTree<K extends Comparable<K>, T> {
    public Node<K, T> root;
    public static final int LEAF_ORDER = 3;

    public void insert(K key, T value) {
        LeafNode<K, T> leaf = new LeafNode<K, T>(key, value);
        Map.Entry<K, Node<K, T>> splitEntry = null;

        if (root == null) {
            root = new LeafNode<>(key, value);
        }

        // TBD insert the leaf node into the tree
    }

    public Map.Entry<K, Node<K, T>> insertLeaf(Node<K, T> node, Map.Entry<K, Node<K,T>> entry,
                                                   Map.Entry<K, Node<K,T>> newChildEntry) {
        // Directly insert the new child into the internal node
        LeafNode<K, T> leafNode = (LeafNode<K, T>) node;
        LeafNode<K, T> newLeafNode = (LeafNode<K, T>) entry.getValue();

        leafNode.insertSorted(entry.getKey(), newLeafNode.values.get(0));

        if (leafNode.isOverflowed()) {
            return splitLeaf(leafNode);
        } else {
            return null;
        }
    }

    public Map.Entry<K, Node<K, T>> splitLeaf(LeafNode<K, T> leafNode) {
        List<K> newKeys = new ArrayList<>();
        List<T> newValues = new ArrayList<>();

        for (int i = LEAF_ORDER / 2; i < LEAF_ORDER; i++) {
            newKeys.add(leafNode.keys.get(i));
            newValues.add(leafNode.values.get(i));
        }

        for (int i = LEAF_ORDER / 2; i < LEAF_ORDER; i++) {
            leafNode.keys.remove(LEAF_ORDER / 2);
            leafNode.values.remove(LEAF_ORDER / 2);
        }

        K splitKey = newKeys.get(0);
        LeafNode<K, T> newLeafNode = new LeafNode<>(newKeys, newValues);
        newLeafNode.next = leafNode.next;
        leafNode.next = newLeafNode;

        return Map.entry(splitKey, newLeafNode);
    }
}
