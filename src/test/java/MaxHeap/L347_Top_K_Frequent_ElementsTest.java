package MaxHeap;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.junit.jupiter.api.Assertions.*;

class L347_Top_K_Frequent_ElementsTest {
    @Test
    void should_return_top_k_frequent_elements1() {
        int[] nums = {1, 1, 1, 2, 2, 3};
        int k = 2;
        L347_Top_K_Frequent_Elements r = new L347_Top_K_Frequent_Elements();
        List<Integer> result = r.topKFrequent(nums, k);
        assertEquals(Arrays.asList(2, 1), result);
    }

    @Test
    void should_return_top_k_frequent_elements2() {
        int[] nums = {1};
        int k = 1;
        L347_Top_K_Frequent_Elements r = new L347_Top_K_Frequent_Elements();
        List<Integer> result = r.topKFrequent(nums, k);
        assertEquals(Arrays.asList(1), result);
    }

    @Test
    void should_return_top_k_frequent_elements3() {
        int[] nums = {4, 1, -1, 2, -1, 2, 3};
        int k = 2;
        L347_Top_K_Frequent_Elements r = new L347_Top_K_Frequent_Elements();
        List<Integer> result = r.topKFrequent(nums, k);
        assertEquals(Arrays.asList(-1, 2), result);
    }
}