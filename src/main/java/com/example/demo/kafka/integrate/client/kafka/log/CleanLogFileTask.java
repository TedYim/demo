package com.example.demo.kafka.integrate.client.kafka.log;

import java.io.File;
import java.util.Random;
/****
 * 清理过期的没有被处理的文件
 */
public class CleanLogFileTask implements LogConstant,Runnable{

	long expireTime=24*60*60*1000;
	
	public CleanLogFileTask() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while(true){
			try{
					File file=new File(logDir);
					if(!file.exists()){
						file.mkdirs();
						continue;
					}
					File[] files=file.listFiles();
					if(files.length==0){
						Thread.sleep(1000);
						continue;
					}
					for(File f:files){
						if(f==null){
							continue;
						}
						if(f.exists()&&(System.currentTimeMillis()-f.lastModified()>=expireTime)){
						 	f.deleteOnExit();
						}
					}
					Thread.sleep(new Random().nextInt(1000));
				}catch(Exception e){
					e.printStackTrace();
				}
		}
	}
}
