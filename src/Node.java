import java.util.*;

/**
 * Node class for BPlusTree
 */

public class Node<K extends Comparable<K>, T> {
    protected boolean isLeaf;
    protected List<K> keys;

    public boolean isOverflowed() {
        return keys.size() > 3;
    }

    public boolean isUnderflowed() {
        return keys.size() < 2;
    }
}
