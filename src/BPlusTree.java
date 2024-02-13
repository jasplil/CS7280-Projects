import java.util.*;
import java.util.Map.Entry;

public class BPlusTree<K extends Comparable<K>, T> {
    public Node<K, T> root;
    public static final int LEAF_ORDER = 5;

    public void insert(K key, T value) {
        LeafNode<K, T> leaf = new LeafNode<K, T>(key, value);
        Entry<K, Node<K, T>> entry = new AbstractMap.SimpleEntry<K, Node<K,T>>(key, leaf);;

        // If the root is null or empty, create a new root
        if (root == null || root.keys.size() == 0) {
            root = entry.getValue();
        }

        // Get the new entry to insert into the tree
        Entry<K, Node<K,T>> newEntry = getEntry(root, entry, null);

        // When the root is split, create a new root
        if (newEntry != null) {
            InternalNode<K, T> newRoot = new InternalNode<K, T>(newEntry.getKey(), root, newEntry.getValue());
            root = newRoot;

            System.out.println("New Root: " + root.keys);
        }

        return;
    }

    private Entry<K, Node<K,T>> getEntry(Node<K,T> node, Entry<K, Node<K,T>> entry,
                                              Entry<K, Node<K,T>> newEntry) {
        if (node.isLeaf) {
            return insertLeaf(node, entry, newEntry);
        } else {
            return insertInternal(node, entry, newEntry);
        }
    }

    public Entry<K, Node<K, T>> insertLeaf(Node<K, T> node, Map.Entry<K, Node<K,T>> entry,
                                                   Map.Entry<K, Node<K,T>> newEntry) {
        LeafNode<K, T> leafNode = (LeafNode<K, T>) node;
        LeafNode<K, T> newLeafNode = (LeafNode<K, T>) entry.getValue();

        // Directly insert the new child into the leaf node
        leafNode.insertSorted(entry.getKey(), newLeafNode.values.get(0));

        // If the size is greater than LEAF_ORDER, split the leaf node
        if (leafNode.isOverflowed()) {
            System.out.println("Overflowed: " + leafNode.keys);
            newEntry = splitLeaf(leafNode);
            System.out.println("New Leaf Node: " + newEntry.getValue().keys);
            if (leafNode == root) {
                InternalNode<K, T> newRoot = new InternalNode<>(newEntry.getKey(), leafNode, newEntry.getValue());
                root = newRoot;
                return null;
            }

            return newEntry;
        } else {
            System.out.println("Leaf Node: " + leafNode.keys);
            return null;
        }
    }

    /**
     * Split the leaf node into two nodes
     * @param leafNode the leaf node to be split
     * @return the new key and the new leaf node
     */
    public Map.Entry<K, Node<K, T>> splitLeaf(LeafNode<K,T>  leafNode) {
        List<K> newKeys = new ArrayList<>();
        List<T> newValues = new ArrayList<>();

        // Add the second half of the keys and values to the new leaf node
        for (int i = Math.abs(LEAF_ORDER / 2); i < LEAF_ORDER; i++) {
            newKeys.add(leafNode.keys.get(i));
            newValues.add(leafNode.values.get(i));
        }

        // Remove the second half of the keys and values from the original leaf node
        for (int i = Math.abs(LEAF_ORDER / 2); i < LEAF_ORDER; i++) {
            leafNode.keys.remove(LEAF_ORDER / 2);
            leafNode.values.remove(LEAF_ORDER / 2);
        }

        // Create a new leaf node and link it to the parent leaf node
        K splitKey = newKeys.get(0);
        LeafNode<K,T> rightNode = new LeafNode<>(newKeys, newValues);

        // Link to neighbor leaf nodes
        LeafNode<K,T> tmp = leafNode.next;
        leafNode.next = rightNode;
        leafNode.next.prev = rightNode;
        rightNode.prev = leafNode;
        rightNode.next = tmp;

        Entry<K, Node<K,T>> newEntry = new AbstractMap.SimpleEntry<>(splitKey, rightNode);

        return newEntry;
    }

    public Entry<K, Node<K, T>> insertInternal(Node<K, T> node, Map.Entry<K, Node<K,T>> entry,
                                               Entry<K, Node<K,T>> newEntry) {
        System.out.println("Internal Node: " + node.keys);
        InternalNode<K,T> index = (InternalNode<K,T>) node;
        int i = 0;
        while(i < index.keys.size()) {
            if(entry.getKey().compareTo(index.keys.get(i)) < 0) {
                break;
            }
            i++;
        }
        // Recursively, insert entry
        newEntry = getEntry((Node<K,T>) index.children.get(i), entry, newEntry);

        // Usual case, didn't split child
        if (newEntry == null) {
            return null;
        } else {
            int j = 0;
            while (j < index.keys.size()) {
                if (newEntry.getKey().compareTo(index.keys.get(j)) < 0) {
                    break;
                }
                j++;
            }

            index.insertSorted(newEntry, j);

            // Usual case, put newChildEntry on it, set newChildEntry to null, return
            if (!index.isOverflowed()) {
                return null;
            } else{
                newEntry = splitIndexNode(index);

                // Root was just split
                if (index == root) {
                    // Create new node and make tree's root-node pointer point to newRoot
                    InternalNode<K,T> newRoot = new InternalNode<K,T>(newEntry.getKey(), root,
                            newEntry.getValue());
                    root = newRoot;
                    return null;
                }
                return newEntry;
            }
        }
    }

    public Entry<K, Node<K,T>> splitIndexNode(InternalNode<K,T> index) {
        ArrayList<K> newKeys = new ArrayList<K>();
        ArrayList<Node<K,T>> newChildren = new ArrayList<Node<K,T>>();

        int minKeys = (int) Math.ceil((double) LEAF_ORDER / 2);
        K splitKey = index.keys.get(minKeys);
        index.keys.remove(minKeys);

        newChildren.add(index.children.get(minKeys + 1));
        index.children.remove(minKeys + 1);

        while (index.keys.size() > minKeys) {
            newKeys.add(index.keys.get(minKeys));
            index.keys.remove(minKeys);
            newChildren.add(index.children.get(minKeys + 1));
            index.children.remove(minKeys + 1);
        }

        InternalNode<K,T> rightNode = new InternalNode<K,T>(newKeys, newChildren);
        Entry<K, Node<K,T>> newChildEntry = new AbstractMap.SimpleEntry<K, Node<K,T>>(splitKey, rightNode);

        return newChildEntry;
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

        InternalNode<K,T> index = (InternalNode<K,T>)node;

        if (key.compareTo(index.keys.get(0)) < 0) {
            return internalSearch((Node<K,T>)index.children.get(0), key);
        } else if (key.compareTo(index.keys.get(node.keys.size() - 1)) >= 0) {
            return internalSearch((Node<K,T>)index.children.get(index.children.size() - 1), key);
        } else {
            for (int i = 0; i < index.keys.size() - 1; i++) {
                if (key.compareTo(index.keys.get(i)) >= 0 && key.compareTo(index.keys.get(i + 1)) < 0) {
                    return internalSearch((Node<K,T>)index.children.get(i+1), key);
                }
            }
        }

        return null;
    }
}
