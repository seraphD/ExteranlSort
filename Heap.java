/**
 * 
 * @author Lihui Zhang/lihuiz@vt.edu
 * @author Haosu wang/whaosu@vt.edu
 * @version 1.0
 */
public class Heap {
    /**
     * size of the heap
     */
    private int size;
    /**
     * insert position in the heap
     */
    private int cur;
    /**
     * elements in the heap
     */
    private Record[] value;
    /**
     * last position in the heap
     */
    private int last;

    /**
     * constructor of heap
     * @param size    block numbers of the heap
     */
    Heap(int size) {
        this.size = size;
        this.value = new Record[size];
        this.cur = 0;
        this.last = size;
    }
    
    /**
     * 
     * @return   size of the heap
     */
    public int getSize() {
        return size;
    }
    
    /**
     * 
     * @return   records array
     */
    public Record[] getVal() {
        return value;
    }
    
    /**
     * 
     * @param pos    position in the array
     * @return       a record
     */
    public Record getRecord(int pos) {
        if (pos >= cur) {
            return null;
        }
        
        return value[pos];
    }
    
    /**
     * set record
     * @param pos   position in the array
     * @param c     record to be set
     */
    public void setRecord(int pos, Record c) {
        value[pos] = c;
        
        if (cur <= pos) {
            cur = pos + 1;
        }
    }
    
    /**
     * 
     * @return        if the heap is full
     */
    public boolean isFull() {
        return cur == last;
    }
    
    /**
     * 
     * @return        if the heap is empty
     */
    public boolean isEmpty() {
        return cur == 0;
    }
    
    /**
     * append a record in the end of value array
     * @param c       appended record
     */
    public void append(Record c) {
        value[cur] = c;
        cur++;
    }
    
    /**
     * 
     * @return the first record in the heap
     */
    public Record top() {
        if (!isEmpty()) {
            return value[0];
        }
        else {
            return null;
        }
    }
    
    /**
     * pop the first record from the heap
     * and replace top with the new record
     * @param n       new record
     * @return        the popped record
     */
    public Record pop(Record n) {
        Record c = value[0];
        value[0] = n;
        
        if (n.compareTo(c) < 0) {
            swap(0, cur - 1);
            cur--;
            last = cur;
        }
        
        heapify(0);
        
        return c;
    }
    
    /**
     * pop the top record
     * @return       the popped record
     */
    public Record pop() {
        Record c = value[0];
        
        swap(0, cur - 1);
        cur--;
        heapify(0);
        
        return c;
    }
    
    /**
     * remove a record from heap's end
     * @return      removed record
     */
    public Record remove() {
        if (isEmpty()) {
            return null;
        }
        
        cur--;
        Record c = value[cur];
        return c;
    }
    
    /**
     * swap two records in value array
     * @param i        the record to be swapped
     * @param inx      the swapping record
     */
    private void swap(int i, int inx) {
        Record temp = value[i];
        value[i] = value[inx];
        value[inx] = temp;
    }
    
    /**
     * heapify the value array from the position i
     * @param i        starting position
     */
    public void heapify(int i) {
        int leftInx = 2 * i + 1;
        int rightInx = 2 * i + 2;
        int inx = i;
        
        Record c = value[i];
        Record minist = c;
        
        if (leftInx < cur) {
            Record left = value[leftInx];
            
            if (left.compareTo(c) < 0) {
                minist = left;
                inx = leftInx;
            }
        }
        
        if (rightInx < cur) {
            Record right = value[rightInx];
            
            if (right.compareTo(minist) < 0) {
                minist = right;
                inx = rightInx;
            }
        }
        
        if (inx != i) {
            swap(i, inx);
            heapify(inx);
        }
        
    }
    
    /**
     * heapify the whole array
     */
    public void heapify() {
        int i = cur / 2;
        
        while (i > -1) {
            heapify(i);
            i--;
        }
    }
    
    /**
     * check if the array is sorted
     * @param array      target array
     * @return           is the array sorted
     */
    public static boolean isSorted(Record[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i].compareTo(array[i - 1]) < 0) {
                return false;
            }
        }
        
        return true;
    }

    
    /**
     * clone the heap
     * @return          a copy of the object
     */
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    /**
     * empty a heap
     */
    public void empty() {
        cur = 0;
    }
    
    /**
     * reset working memory
     * move records behind the last to front
     */
    public void reset() {
        for (int i = last; i < size; i++) {
            value[cur] = value[i];
            cur++;
        }
        
        last = size;
        heapify();
    }
}
