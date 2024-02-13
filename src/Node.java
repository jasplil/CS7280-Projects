import java.util.*;

/**
 * Node class for BPlusTree
 */

public class Node<K extends Comparable<K>, T> {
    protected boolean isLeaf;
    protected List<K> keys;

    public boolean isOverflowed() {
        return keys.size() > BPlusTree.LEAF_ORDER - 1;
    }
}
