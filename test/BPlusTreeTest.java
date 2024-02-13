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
        tree.insert(18, 6);
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
        tree.insert(10, 1);
        tree.insert(2, 36);
//        assertEquals(tree.search(10), 1);
//        assertEquals(tree.search(2), 36);
    }
}
