import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import student.TestCase;

/**
 * sample test
 * @author Lihui Zhang / lihuiz@vt.edu
 * @author Haosu Wang / whaosu@vt.edu
 * @version 1.0
 */
public class SampleTest extends TestCase {
    /**
     * set up for tests
     */
    public void setUp() {
        //nothing to set up.
    }
    
    /**
     * Read contents of a file into a string
     * 
     * @param path
     *            File name
     * @return the string
     * @throws IOException
     */
    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }
    
    /**
     * test sample input file
     * @throws CloneNotSupportedException
     * @throws IOException
     */
    public void testSample() throws CloneNotSupportedException, IOException {
        String[] args = new String[1];
        args[0] = "sampleInput16_dup.bin";
        String srcFile = "sampleInput16.bin";
        
        Externalsort.copy(srcFile, args[0]);
        
        Externalsort sorter = new Externalsort();
        assertNotNull(sorter);
        Externalsort.main(args);
        
        assertFuzzyEquals(
            readFile("Expected_Std_Out.txt"), 
            systemOut().getHistory());
    }
}
