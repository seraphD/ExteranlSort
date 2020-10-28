import student.TestCase;
import java.util.*;

/**
 * heap test
 * @author Lihui Zhang / lihuiz@vt.edu
 * @author Haosu Wang / whaosu@vt.edu
 * @version 1.0
 */
public class HeapTest extends TestCase {
    /**
     * testing heap size
     */
    static private int heapSize = 512;

    /** Initialize the random variable */
    static private Random value = new Random(); // Hold the Random class object
    
    /**
     * 
     * @return    a random long number
     */
    static long randLong() {
        return value.nextLong();
    }

    
    /**
     * 
     * @return    a random double
     */
    static double randDouble() {
        return value.nextDouble();
    }
    
    /**
     * set up for tests
     */
    public void setUp() {
        //nothing to set up.
    }
    
    /**
     * convert a long number to byte array
     * @param num        converted long number
     * @return           byte array
     */
    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }
    
    /**
     * convert a double to bytes array
     * @param d        converted double
     * @return         byte array
     */
    public static byte[] double2Bytes(double d) {
        long v = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte) ((v >> 8 * i) & 0xff);
        }       
        return byteRet;
    }
    
    /**
     * generate a record with random key and value
     * @return          a record
     */
    public static Record generateRecord() {
        long val = (long)(randLong());
        double val2 = (double)(randDouble());
        
        byte[] v = long2Bytes(val);
        byte[] v2 = double2Bytes(val2);
        byte[] record = new byte[16];
        
        for (int i = 0; i < 8; i++) {
            record[i] = v[i];
            record[i + 8] = v2[i];
        }
        
        return new Record(record);
        
    }
    
    /**
     * 
     * @return     a block full of records with random keys
     */
    public Heap generateBlock() {
        Heap heap = new Heap(heapSize);
        
        for (int i = 0; i < heapSize; i++) {
            Record c = generateRecord();
            heap.append(c);
        }
        
        heap.heapify();
        
        return heap;
    }
    
    /**
     * test if heap sort work
     */
    public void testSort() {
        Heap heap = generateBlock();
        
        Record[] sorted = new Record[heapSize];
        
        for (int i = 0; i < heapSize; i++) {
            sorted[i] = heap.pop();
        }
        
        assertTrue("sort failed", Heap.isSorted(sorted));
    }
    
}
