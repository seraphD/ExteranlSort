import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * {Project Description Here}
 */

/**
 * The class containing the main method.
 *
 * @author Lihui Zhang/lihuiz@vt.edu
 * @author Haosu Wang/whaosu@vt.edu
 * @version 1.0
 */

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class Externalsort {
    
    /**
     * read a record from input file
     * @param fis           file input stream
     * @param recordSize    record size
     * @return              a record
     * @throws IOException 
     */
    public static Record readRecord(FileInputStream fis, int recordSize)
        throws IOException {
        if (fis.available() <= 0) {
            return null;
        }
        
        byte[] c = new byte[recordSize];
        
        fis.read(c);
        
        Record r = new Record(c);
        
        return r;
    }
    
    /**
     * using random file access read a record
     * @param raf                random file access object
     * @param recordSize         size of a record
     * @throws IOException
     */
    public static void readRecord(RandomAccessFile raf, int recordSize) 
        throws IOException {
        
        byte[] b = new byte[recordSize];
        
        raf.read(b);
        ByteBuffer buff = ByteBuffer.wrap(b);
        
        long val = buff.getLong(0);
        double key = buff.getDouble(recordSize / 2);
        
        System.out.print(val + " " + key);
    }
    
    /**
     * if creating a run is over
     * @param pos           current position in each block
     * @param run           number of runs are being merged
     * @param mergedRun     number of runs being merged
     * @param size          block size
     * @return              is combining runs over
     */
    public static boolean isOver(int[] pos, int run, int mergedRun, int size) {
        // bug check
        for (int i = 0; i < mergedRun; i++) {
            if (pos[i] < size) {
                return false;
            }
        }
        
        return true;
    }
    
    
    /**
     * choose the smallest record in work memory
     * @param work          working memory
     * @param pos           position array
     * @param size          size of block
     * @param mergedRun     number of runs being merged
     * @return              index of the smallest record
     */
    public static int choose(Heap work, int[] pos, int size, int mergedRun) {
        Record min = null;
        int inx = -1;
        
        for (int i = 0; i < mergedRun; i++) {
            if (pos[i] == size) {
                continue;
            }
            
            Record c = work.getRecord(i * size + pos[i]);
            
            if (min == null) {
                min = c;
                inx = i;
            }
            else {
                if (c.compareTo(min) < 0) {
                    min = c;
                    inx = i;
                }
            }
        }
        
        return inx;
    }
    
    /**
     * write a record to byte buffer
     * @param src            byte buffer
     * @param outputBuffer   the output buffer
     * @param recordSize     number of bytes of a record
     */
    private static void writeRunFileBuffer(
        ByteBuffer src, Heap outputBuffer, int recordSize) {
        Record[] o = outputBuffer.getVal();
        
        for (int i = 0; i < o.length; i++) {
            byte[] record = o[i].getCompleteRecord();
            
            src.put(record);
        }
    }

    /**
     * read a block
     * @param b                   block to be filled
     * @param fis                 file input stream
     * @param recordSize          byte length of a record
     * @throws IOException
     */
    public static void readBlock(Heap b, FileInputStream fis, int recordSize)
        throws IOException {
        
        while (!b.isFull()) {
            Record c = readRecord(fis, recordSize);
            
            if (c == null) {
                break;
            }
            
            b.append(c);
        }
    }
    
    /**
     * read a block a run into working when combining runs
     * @param work              working memory
     * @param raf               randomAccessFile
     * @param run               number of current run
     * @param block             number of block in the run
     * @param heapSize          heap size
     * @param blockSize         block size
     * @param runSize           length of the run
     * @param recordSize        length of a record
     * @throws IOException
     */
    public static void readBlock(Heap work, RandomAccessFile raf, 
        int run, int block, int heapSize, 
        int blockSize, int[] runSize, int recordSize) 
        throws IOException {
        
        long pos = block;
        for (int i = 0; i < run; i++) {
            pos += runSize[i];
        }
        
        pos = pos * blockSize * recordSize;
        raf.seek(pos);
        
        for (int i = 0; i < blockSize; i++) {
            byte[] b = new byte[recordSize];
            
            int n = raf.read(b);
            
            if (n == -1) {
                break;
            }
            
            Record c = new Record(b);
            
            int inx = (run % heapSize) * blockSize + i;
            work.setRecord(inx, c);
        }
    }
    
    /**
     * copy files
     * @param src               src file name
     * @param des               target file name
     * @throws IOException
     */
    public static void copy(String src, String des) throws IOException {
        File s = new File(src);
        File d = new File(des);
        
        FileChannel inputChannel = new FileInputStream(s).getChannel();  
        FileChannel outputChannel = new FileOutputStream(d).getChannel();  
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        
        inputChannel.close();
        outputChannel.close();
    }
    
    /**
     * print first record in each block
     * @param raf               random file access object
     * @param blockSize         block size
     * @param recordSize        size of a record
     * @param block             how much block in the file
     * @throws IOException
     */
    private static void printRecords(
        RandomAccessFile raf, int blockSize, int recordSize, int block) 
        throws IOException {
        
        long pos = 0;
        long jump = blockSize * recordSize;
        
        for (int i = 0; i < block; i++) {
            raf.seek(pos);
            readRecord(raf, recordSize);
            
            if ((i + 1) % 5 == 0) {
                System.out.println();
            }
            else {
                System.out.print(" ");
            }
            
            pos += jump;
        }
        
    }
    

    /**
     * @param args
     *     Command line parameters
     * @throws CloneNotSupportedException 
     * @throws IOException 
     */
    public static void main(String[] args) 
        throws CloneNotSupportedException, IOException {
        
        String path = args[0];
        // a block will contain 512 records
        int blockSize = 512;
        
        // a record will be 16 bytes
        int recordSize = 16;
        
        // the working memory will consist of 8 heaps
        int heapSize = 8;
        
        File file = new File(path);
        File runFile = new File("runFile.bin");
        
        if (!runFile.exists()) {
            runFile.createNewFile();
        }
        
        FileOutputStream fos = new FileOutputStream(runFile);
        FileInputStream fis = new FileInputStream(file);
        FileChannel channel = fos.getChannel();
        
        int runSize = heapSize * blockSize * recordSize;
        ByteBuffer src = ByteBuffer.allocate(runSize);
        
        // input buffer
        Heap inputBuffer = new Heap(blockSize);
        // output buffer
        Heap outputBuffer = new Heap(blockSize);
        // working memory
        Heap work = new Heap(heapSize * blockSize);
        
        // number of merged blocks in a run
        int cnt = 0;
        
        // number of runs
        int run = 0;
        
        while (fis.available() > 0 || !work.isEmpty()) {
            src.clear();
            
            // fill the working memory
            readBlock(work, fis, recordSize);
            work.heapify();
            
            // create a run
            while (cnt < heapSize) {
                // fill output buffer
                while (!outputBuffer.isFull()) {
                    
                    // fill inputBuffer if it's empty
                    if (inputBuffer.isEmpty()) {
                        readBlock(inputBuffer, fis, recordSize);
                    }
                    
                    Record c = inputBuffer.remove();
                    
                    Record o;
                    if (c != null) {
                        o = work.pop(c);
                    }
                    else {
                        if (work.isEmpty()) {
                            break;
                        }
                        
                        o = work.pop();
                    }
                    
                    outputBuffer.append(o);
                }
                
                writeRunFileBuffer(src, outputBuffer, recordSize);
                outputBuffer.empty();
                cnt++;
            }
            
            // write a run
            src.flip();
            while (src.hasRemaining()) {
                channel.write(src);
            }
            
            cnt = 0;
            run++;
            
            work.reset();
        }
        channel.close();
        
        RandomAccessFile raf = new RandomAccessFile("runFile.bin", "rw");
        
        // current position in each block
        int[] pos = new int[heapSize];
        // current block in each run
        int[] blkCnt = new int[heapSize];
        // length of current run
        int[] l = new int[run];
        
        for (int i = 0; i < run; i++) {
            l[i] = heapSize;
        }
        
        // bug fix
        // different length run
        while (run > 1) {
            DataOutputStream dosRun = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream("tempRun.bin")));
            
            int newLength = (int)Math.ceil((double)run / heapSize);
            int[] newRunLength = new int[newLength];
            // multiway-merging
            for (int i = 0; i < newLength; i++) {
                int mergedRun = Math.min(heapSize, run - heapSize * i);
                
                for (int j = 0; j < mergedRun; j++) {
                    pos[j] = 0;
                    blkCnt[j] = 0;
                    
                    // bug check
                    int curRun = i * heapSize + j;
                    readBlock(
                        work, raf, curRun, 0, 
                        heapSize, blockSize, l, recordSize);
                }
                
                while (!isOver(pos, run, mergedRun, blockSize)) {
                    int inx = choose(work, pos, blockSize, mergedRun);
                    int recordInx = inx * blockSize + pos[inx];
                    
                    pos[inx]++;
                    if (pos[inx] == blockSize) {
                        blkCnt[inx]++;
                        if (blkCnt[inx] < l[inx]) {
                            readBlock(
                                work, raf, inx, blkCnt[inx], 
                                heapSize, blockSize, l, recordSize);
                            
                            pos[inx] = 0;
                        }
                        
                    }
                    
                    Record c = work.getRecord(recordInx);
                    outputBuffer.append(c);
                    
                    if (outputBuffer.isFull()) {
                        cnt++;
                        while (!outputBuffer.isEmpty()) {
                            byte[] b;
                            b = outputBuffer.pop().getCompleteRecord();
                            dosRun.write(b);
                        }
                        
                    }
                }
                
                newRunLength[i] = cnt;
                cnt = 0;
            }
            
            run = (int)Math.ceil((double)run / 8);
            l = newRunLength;
            
            dosRun.flush();
            dosRun.close();
            
            copy("tempRun.bin", "runFile.bin");
        }
        
        copy("runFile.bin", args[0]);
        
        printRecords(raf, blockSize, recordSize, l[0]);
        
        fis.close();
        fos.close();
        raf.close();
    }

}
