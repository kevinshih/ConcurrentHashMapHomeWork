import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Map;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class ConcurrentHashMapHomeWorkWriteFiles extends Thread{
	
	List<String> list = new ArrayList<>();//the sequence of execution
    String token;//what the thread want write
    static ConcurrentHashMap<String, String> conCurrentMap = new ConcurrentHashMap<>();
    
    public ConcurrentHashMapHomeWorkWriteFiles(List<String> list, String token) {
        this.list = list;
        this.token = token;
    }
    
    public static void main(String args[]) throws IOException {
    	createFile();
    	Thread t1 = new Thread(new ConcurrentHashMapHomeWorkWriteFiles(getList("t1"),"123"));
    	Thread t2 = new Thread(new ConcurrentHashMapHomeWorkWriteFiles(getList("t2"),"ABC"));
    	Thread t3 = new Thread(new ConcurrentHashMapHomeWorkWriteFiles(getList("t3"),"Blockchain"));
    	t1.start();
    	t2.start();
    	t3.start();
    	try {
            t1.join(); // wait for t1 end
            t2.join(); // wait for t2 end
            t3.join(); // wait for t3 end
            for (Map.Entry<String, String> entry : conCurrentMap.entrySet ()){//write & print
            	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(entry.getKey())));
				writer.write(entry.getValue());
            	writer.close();
            	System.out.println(entry.getKey()+" :\n"+ entry.getValue());
            }
        } catch (InterruptedException e) {
        	throw new RuntimeException(e);
        } 
    }
    
    /**
     * return a random order list form 1 to 20;
     */
    private static List<String> getList(String name){
    	String[] arr = new String[20];
    	for(int i=0;i<arr.length;) arr[i] = Integer.toString(++i);
    	List<String> list = Arrays.asList(arr);
    	Collections.shuffle(list);
    	System.out.println(name +" list:\n"+list.toString());
    	return list;
    }
    
    @Override
    public void run() {
        	for(String s:list) {
        		String path = s+".txt";
        		conCurrentMap.compute(path, (key, val) 
                        -> (val + token+"\n").replace("null",""));
        	}
    }
    
    private static void createFile() {
        try {
	        for (int i = 0; i < 20; i++) { //create 20 blank txt files
	            String path = (i + 1) + ".txt";
	            File file = new File(path);
	            if (!file.isFile()) file.createNewFile();
	        }
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
}
