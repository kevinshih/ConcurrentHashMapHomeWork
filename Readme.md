

### Homework1 :

### 目的: 三個threads寫入不同的字串到20個不同的files



### 解法說明:

程式會產生20個不同的files

```java
createFile()
```

開3個不同的thread同步寫不同的字串, 寫檔順序getList有做shuffle

```java
Thread t1 = new Thread(new ConcurrentHashMapHomeWorkWriteFiles(getList("t1"),"123"));
Thread t2 = new Thread(new ConcurrentHashMapHomeWorkWriteFiles(getList("t2"),"ABC"));
Thread t3 = new Thread(new ConcurrentHashMapHomeWorkWriteFiles(getList("t3"),"Blockchain"));
```

```java
@Override
    public void run() {
        	for(String s:list) {
        		String path = s+".txt";
        		conCurrentMap.compute(path, (key, val) 
                        -> (val + token+"\n").replace("null",""));
        	}
    }
```

最後寫檔及印出

```java
for (Map.Entry<String, String> entry : conCurrentMap.entrySet ()){//write & print
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(entry.getKey())));
	writer.write(entry.getValue());
    writer.close();
    System.out.println(entry.getKey()+" :\n"+ entry.getValue());
}
```



### 程式參考:

HomeWork1/ConcurrentHashMapHomeWorkWriteFiles.java



### 結果顯示:

<img src="C:\Users\kevin.shih\AppData\Roaming\Typora\typora-user-images\image-20220602121018385.png" alt="image-20220602121018385" style="zoom: 67%;" />



![image-20220602121959846](C:\Users\kevin.shih\AppData\Roaming\Typora\typora-user-images\image-20220602121959846.png)



### 小結:

這裡的作法是全部寫到map之後, 最後再一次寫入檔案與印出



### Homework2:

呈上題, 作法調整成一旦thread更新資料的當下就寫入檔案, 最後再一次印出

```java
@Override
    public void run() {
        	for(String s:list) {
        		String path = s+".txt";
        		conCurrentMap.compute(path, (key, val) 
                        -> {
                        	try {
                        		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path))));
								writer.write((val + token+"\n").replace("null",""));
								writer.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        	return (val + token+"\n").replace("null","");});
        	}
    }
```



### 程式參考:

HomeWork2/ConcurrentHashMapHomeWorkWriteFiles2.java



### 結果顯示:

![image-20220602133604377](C:\Users\kevin.shih\AppData\Roaming\Typora\typora-user-images\image-20220602133604377.png)

![image-20220602133830794](C:\Users\kevin.shih\AppData\Roaming\Typora\typora-user-images\image-20220602133830794.png)



### 小結:

視不同情境應有不同的做法, 頻繁寫入與一次寫入, 在數據大的時候才比較會呈現不同的狀況



### Homework3:

有關rabbit mq consumer失敗後的處理機制

除了我提到可以發信通知管理員之外

也可運用retry機制

並設定嘗試retry幾次, 每隔多久retry一次

如還是無法成功可再發信

學習到Spring boot設定相關參考: https://www.javainuse.com/messaging/rabbitmq/error

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        retry:
          enabled: true
          initial-interval: 3s
          max-attempts: 6
          max-interval: 10s
          multiplier: 2
```

### 小結:

目前我這裡使用rabbit mq過程較少遇到consumer失敗的情況

藉由這次經驗, 往後也可添加在專案裡面

感謝 =)



### Kevin



