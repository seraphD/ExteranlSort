// WARNING: This program uses the Assertion class. When it is run,
// assertions must be turned on. For example, under Linux, use:
// java -ea Genfile

/**
 * Generate a data file. The size is a multiple of 8192 bytes.
 * Each record is one long and one double.
 */

import java.io.*;
import java.util.*;

/**
 * 
 * @author Lihui Zhang/lihuiz@vt.edu
 * @author Haosu wang/whaosu@vt.edu
 * @version 1.0
 */
public class GenFileProj3 {
    
    /**
     * number of records in a block
     */
    static final int NUMRECS = 512; // Because they are short ints

    /** Initialize the random variable */
    static private Random value = new Random(); // Hold the Random class object
    
    /**
     * @return      a long type number
     */
    static long randLong() {
        return value.nextLong();
    }

    /**
     * 
     * @return      a double number
     */
    static double randDouble() {
        return value.nextDouble();
    }

    /**
     * 
     * @param args       args[0] = file name   args[1] = number of blocks
     * @throws IOException
     */
    public static void generateBinFile(String[] args) throws IOException {
        long val;
        double val2;
        assert (args.length == 2) : "\nUsage: Genfile <filename> <size>"
            + "\nOptions \nSize is measured in blocks of 8192 bytes";

        int filesize = Integer.parseInt(args[1]); // Size of file in blocks
        DataOutputStream file = new DataOutputStream(new BufferedOutputStream(
            new FileOutputStream(args[0])));

        for (int i = 0; i < filesize; i++) {
            for (int j = 0; j < NUMRECS; j++) {
                val = (long)(randLong());
                file.writeLong(val);
                val2 = (double)(randDouble());
                file.writeDouble(val2);
            }
        }
        file.flush();
        file.close();
    }
    
    /**
     * generate testing files using a given keys array
     * @param args              arguments
     * @param keys              keys array
     * @throws IOException 
     */
    public static void generateBinFile(String[] args, double[] keys) 
        throws IOException {
        long val;
        double val2;
        
        int filesize = Integer.parseInt(args[1]); // Size of file in blocks
        DataOutputStream file = new DataOutputStream(new BufferedOutputStream(
            new FileOutputStream(args[0])));

        for (int i = 0; i < filesize; i++) {
            for (int j = 0; j < NUMRECS; j++) {
                val = (long)(randLong());
                file.writeLong(val);
                val2 = keys[i * NUMRECS + j];
                file.writeDouble(val2);
            }
        }
        file.flush();
        file.close();
    }

}
