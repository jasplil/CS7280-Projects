import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
//
public class BPlusTreeTest {
    @Test
    public void testInsert() {
        BPlusTree<Integer, Integer> tree = new BPlusTree<>();
        tree.insert(5, 1);
        tree.insert(8, 36);
        tree.insert(10, 38);
        tree.insert(15, 3);
        tree.insert(16, 35);
        tree.insert(17, 6);
        tree.insert(18, 12);
        tree.insert(20, 12);
    }

    @Test
    public void testSplitKey() {
        BPlusTree<Integer, Integer> tree = new BPlusTree<>();
        tree.insert(5, 1);
        tree.insert(8, 36);
        tree.insert(10, 38);
        tree.insert(15, 38);
        tree.insert(16, 38);
    }

    @Test
    public void testSearch() {
        BPlusTree<Integer, Integer> tree = new BPlusTree<>();
        tree.insert(29, 1);
        tree.insert(41, 36);
        tree.insert(44, 38);
        tree.insert(62, 3);
        tree.insert(46, 35);
        tree.insert(49, 6);
        tree.insert(27, 12);
        tree.insert(76, 12);
        tree.insert(91, 12);
        tree.insert(30, 12);
        tree.insert(100, 12);
        tree.insert(47, 12);
        tree.insert(34, 12);
        tree.insert(53, 12);
        tree.insert(9, 12);
        tree.insert(45, 12);

        tree.printTree();
    }

}
