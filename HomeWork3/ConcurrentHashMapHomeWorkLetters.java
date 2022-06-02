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

public class ConcurrentHashMapHomeWorkLetters {
	
	static final String letters = "abcedfghijklmnopqrstuvwxyz"; //26 letters
	
	public static void main(String[] args)   
    {   
		//create file and write shuffle a-z
		createFile();
		
		//concurrentHashMap to count letters 
		compute();
 
    }
	
	private static void compute() {
		ConcurrentHashMap<String, LongAdder> counterMap = new ConcurrentHashMap<>();
        List<Thread> ts = new ArrayList<>();//Thread ArrayList
        for (int i = 1; i <= 26; i++) { //26 Threads concurrent read 26txt files
            int idx = i;
            Thread thread = new Thread(() -> {
                List<String> words = readFile(idx);
                for (String word : words) {
                	counterMap.computeIfAbsent(word, (key) -> new LongAdder()).increment();
                }
            });
            ts.add(thread);
        }
        ts.forEach(t->t.start());//start all thread
        ts.forEach(t-> {
            try {
                t.join();//wait for 26 threads end
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("Print map:\n"+counterMap);//print map for a-z
	}
	
	private static List<String> readFile(int i) {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(i +".txt")))) {
            while(true) {
                String word = in.readLine();
                if(word == null) {
                    break;
                }
                words.add(word);
            }
            return words;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	
	private static void createFile() {
		int length = letters.length();//26
        int count = 200;
        List<String> list = new ArrayList<>(length * count);//26*200
        for (int i = 0; i < length; i++) {//each letter create 200 times to a list
            char ch = letters.charAt(i);
            for (int j = 0; j < count; j++) {
                list.add(String.valueOf(ch));
            }
        }
        Collections.shuffle(list);//to shuffle 26*200 letters
        try {
	        for (int i = 0; i < 26; i++) { //write 200 letters to 26 txt files(means each txt file have 200 letters.)
	            String path = (i + 1) + ".txt";
	            File file = new File(path);
	            if (!file.isFile()) {
	                
						file.createNewFile();
					
	            }
	            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
	            String collect = list.subList(i * count, (i + 1) * count).stream()
	                    .collect(Collectors.joining("\n"));
	            writer.write(collect);
	            writer.close();
	        }
	        System.out.println("Create Files is Finished.");
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
}
