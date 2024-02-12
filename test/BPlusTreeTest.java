import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
//
public class BPlusTreeTest {
    @Test
    public void testInsert() {
        BPlusTree<Integer, Integer> tree = new BPlusTree<>();
        tree.insert(10, 1);
        tree.insert(2, 36);
    }

    @Test
    public void testSearch() {
        BPlusTree<Integer, Integer> tree = new BPlusTree<>();
        tree.insert(10, 1);
        tree.insert(2, 36);
//        assertEquals(tree.search(10), 1);
//        assertEquals(tree.search(2), 36);
        System.out.println(tree.search(10));
    }
}
