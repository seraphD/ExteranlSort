import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import student.TestCase;

/**
 * order test
 * @author Lihui Zhang / lihuiz@vt.edu
 * @author Haosu Wang / whaosu@vt.edu
 * @version 1.0
 */
public class OrderTest extends TestCase {
    
    /**
     * set up for tests
     */
    public void setUp() {
        //nothing to set up.
    }
    
    /**
     * test worst case
     * @throws IOException
     * @throws CloneNotSupportedException
     */
    public void testOrder() throws IOException, CloneNotSupportedException {
        int blockSize = 512;
        int block = 400;
        double[] keys = new double[blockSize * block];
        String[] args = new String[2];
        args[0] = "orderTest.bin";
        args[1] = "" + block;
        
        for (int i = 0; i < blockSize * block; i++) {
            keys[i] = blockSize * block - i;
            // keys[i] = i;
        }
        
        GenFileProj3.generateBinFile(args, keys);
        // GenFileProj3.generateBinFile(args);
        
        String[] args1 = new String[1];
        args1[0] = args[0];
        
        Externalsort sorter = new Externalsort();
        assertNotNull(sorter);
        Externalsort.main(args1);
        
        File f = new File(args[0]);
        FileInputStream fis = new FileInputStream(f);
        
        assertTrue("test failed", ExternalsortTest.isSorted(fis, 16, block));
        
        fis.close();
    }
    
}
