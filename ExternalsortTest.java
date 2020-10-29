import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import student.TestCase;

/**
 * 
 * @author Lihui Zhang/lihuiz@vt.edu
 * @author Haosu wang/whaosu@vt.edu
 * @version 1.0
 */
public class ExternalsortTest extends TestCase {
    
    
    /**
     * set up for tests
     */
    public void setUp() {
        //nothing to set up.
    }
    
    /**
     * Get code coverage of the class declaration.
     * @throws CloneNotSupportedException 
     * @throws IOException 
     */
    public void testExternalsortInit() 
        throws CloneNotSupportedException, IOException {
        String[] args1 = new String[1];
        String[] args2 = new String[2];
        
        args1[0] = "testSort.bin";
        args2[0] = "testSort.bin";
        args2[1] = "500";
        GenFileProj3.generateBinFile(args2);
        
        Externalsort sorter = new Externalsort();
        assertNotNull(sorter);
        Externalsort.main(args1);
    }
    
    /**
     * test is the file sorted
     * @throws IOException
     * @throws CloneNotSupportedException
     */
    public void testSort() throws IOException, CloneNotSupportedException {
        String[] args1 = new String[1];
        String[] args2 = new String[2];
        
        args1[0] = "testSort.bin";
        args2[0] = "testSort.bin";
        int block = 80;
        args2[1] = "" + block;
    
        GenFileProj3.generateBinFile(args2);
        
        Externalsort sorter = new Externalsort();
        assertNotNull(sorter);
        Externalsort.main(args1);
        
        File f = new File(args1[0]);
        FileInputStream fis = new FileInputStream(f);
        
        assertTrue("test failed", isSorted(fis, 16, block));
        
        fis.close();
    }
    
    /**
     * check if records are all in ascending order
     * @param fis                 file input stream
     * @param recordSize          size of a record
     * @param block               number of blocks in file 
     * @return                    is the file sorted
     * @throws IOException
     */
    public static boolean isSorted(
        FileInputStream fis, int recordSize, int block) 
        throws IOException {
        
        Record previous = null;
        int cnt = 0;
        
        while (fis.available() > 0) {
            cnt += 1;
            
            if (previous == null) {
                previous = Externalsort.readRecord(fis, 16);
            }
            else {
                Record c = Externalsort.readRecord(fis, 16);
                
                if (previous.compareTo(c) > 0) {
                    return false;
                }
            }
        }
        
        assertEquals(block, cnt / 512);
        
        return true;
    }

}
