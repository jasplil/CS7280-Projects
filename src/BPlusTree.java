import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

public class BPlusTree<K extends Comparable<K>, T> {
    public Node<K, T> root;
    public static final int LEAF_ORDER = 5;
    public static final int MIN_KEYS = (int) (Math.ceil(LEAF_ORDER / 2));

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
        }

        return;
    }

    /**
     * Get the entry to insert into the tree
     * @param node
     * @param entry
     * @param newEntry
     * @return
     */
    private Entry<K, Node<K,T>> getEntry(Node<K,T> node, Entry<K, Node<K,T>> entry,
                                              Entry<K, Node<K,T>> newEntry) {
        if (node.isLeaf) {
            LeafNode<K, T> leafNode = (LeafNode<K, T>) node;
            LeafNode<K, T> newLeafNode = (LeafNode<K, T>) entry.getValue();

            // Directly insert the new child into the leaf node
            leafNode.insertSorted(entry.getKey(), newLeafNode.values.get(0));

            // If the size is greater than LEAF_ORDER, split the leaf node
            if (leafNode.isOverflowed()) {
                newEntry = splitLeaf(leafNode);
                if (leafNode == root) {
                    InternalNode<K, T> newRoot = new InternalNode<>(newEntry.getKey(), leafNode, newEntry.getValue());
                    root = newRoot;
                    return null;
                }

                return newEntry;
            } else {
                return null;
            }
        } else {
            InternalNode<K,T> index = (InternalNode<K,T>) node;
            int i = 0;
            while (i < index.keys.size()) {
                if(entry.getKey().compareTo(index.keys.get(i)) < 0) {
                    break;
                }
                i++;
            }
            // Recursively, insert entry
            newEntry = getEntry((Node<K,T>) index.children.get(i), entry, newEntry);

            // No split occurred
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

                // Insert newChildEntry at index j
                index.insertSorted(newEntry, j);

                // If the index node is not overflowed, return null
                if (!index.isOverflowed()) {
                    return null;
                } else {
                    // Split the index node
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
        for (int i = MIN_KEYS; i <= 2 * MIN_KEYS; i++) {
            newKeys.add(leafNode.keys.get(i));
            newValues.add(leafNode.values.get(i));
        }

        // Remove the second half of the keys and values from the original leaf node
        for (int i = MIN_KEYS; i <= 2 * MIN_KEYS; i++) {
            leafNode.keys.remove(leafNode.keys.size() - 1);
            leafNode.values.remove(leafNode.values.size() - 1);
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

    public Entry<K, Node<K,T>> splitIndexNode(InternalNode<K,T> index) {
        ArrayList<K> newKeys = new ArrayList<K>();
        ArrayList<Node<K,T>> newChildren = new ArrayList<Node<K,T>>();

        K splitKey = index.keys.get(MIN_KEYS);
        index.keys.remove(MIN_KEYS);

        newChildren.add(index.children.get(MIN_KEYS + 1));
        index.children.remove(MIN_KEYS + 1);

        while (index.keys.size() > MIN_KEYS) {
            newKeys.add(index.keys.get(MIN_KEYS));
            index.keys.remove(MIN_KEYS);
            newChildren.add(index.children.get(MIN_KEYS + 1));
            index.children.remove(MIN_KEYS + 1);
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
                System.out.println("the key is " + leaf.keys.get(i) +
                        ", the value is " + leaf.values.get(i).toString());
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

        // Ensure we have keys before attempting to get them
        if (index.keys.size() > 0) {
            if (key.compareTo(index.keys.get(0)) < 0) {
                return internalSearch((Node<K,T>)index.children.get(0), key);
            } else if (key.compareTo(index.keys.get(index.keys.size() - 1)) >= 0) {
                return internalSearch((Node<K,T>)index.children.get(index.children.size() - 1), key);
            } else {
                for (int i = 0; i < index.keys.size() - 1; i++) {
                    if (key.compareTo(index.keys.get(i)) >= 0 && key.compareTo(index.keys.get(i + 1)) < 0) {
                        return internalSearch((Node<K,T>)index.children.get(i+1), key);
                    }
                }
            }
        } else {
            // If there are no keys, we can only go to the first child (if it exists)
            if (!index.children.isEmpty()) {
                return internalSearch((Node<K,T>)index.children.get(0), key);
            }
        }

        // If we reach here, it means something is wrong with the tree structure.
        throw new IllegalStateException("The B+ tree structure is invalid.");
    }

    public void printTree() {
        LinkedBlockingQueue<Node<K,T>> queue;
        // Use bfs to print the tree
        queue = new LinkedBlockingQueue<Node<K,T>>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node<K,T> target = queue.poll();
            if (target.isLeaf) {
                LeafNode<K,T> leaf = (LeafNode<K,T>) target;
                System.out.println("Leaf: " + leaf.keys);
            } else {
                InternalNode<K,T> index = ((InternalNode<K,T>) target);
                System.out.println("Index: " + index.keys);
                for (Node<K, T> child : index.children) {
                    if (child.isLeaf) {
                        LeafNode<K, T> leafChild = (LeafNode<K, T>) child;
                        System.out.println("Leaf child keys: " + leafChild.keys);
                    } else {
                        InternalNode<K, T> internalChild = (InternalNode<K, T>) child;
                        System.out.println("Internal child keys: " + internalChild.keys);
                        // If you want to add the internal nodes' children to the queue for further printing, you can do so:
                        queue.addAll(internalChild.children);
                    }
                }
            }
        }
    }

    public void display(K key) {
        if (root == null) {
            System.out.println("The tree is empty.");
            return;
        }

        Node<K, T> node = findSubtree(root, key);
        if (node != null) {
            displaySubtree(node, 0); // Helper function to print the subtree
        } else {
            System.out.println("Key not found in the tree.");
        }
    }

    private Node<K, T> findSubtree(Node<K, T> node, K key) {
        if (node.isLeaf) {
            LeafNode<K, T> leaf = (LeafNode<K, T>) node;
            if (leaf.keys.contains(key)) {
                return leaf;
            }
        } else {
            InternalNode<K, T> internal = (InternalNode<K, T>) node;
            for (int i = 0; i < internal.keys.size(); i++) {
                if (key.compareTo(internal.keys.get(i)) < 0) {
                    return findSubtree(internal.children.get(i), key);
                }
            }
            if (key.compareTo(internal.keys.get(internal.keys.size() - 1)) >= 0) {
                return findSubtree(internal.children.get(internal.children.size() - 1), key);
            }
        }
        return null; // If the key is not found
    }

    private void displaySubtree(Node<K, T> node, int level) {
        // Indentation for levels to represent the tree structure
        String indentation = " ".repeat(level * 4); // 4 spaces per level

        if (node instanceof InternalNode) {
            InternalNode<K, T> internalNode = (InternalNode<K, T>) node;
            System.out.println(indentation + "InternalNode keys: " + internalNode.keys);
            for (Node<K, T> child : internalNode.children) {
                displaySubtree(child, level + 1); // Recurse one level deeper
            }
        } else if (node instanceof LeafNode) {
            LeafNode<K, T> leafNode = (LeafNode<K, T>) node;
            System.out.println(indentation + "LeafNode keys: " + leafNode.keys);
            // Optionally, print values if needed
            // System.out.println(indentation + "LeafNode values: " + leafNode.values);
        }
    }


}
