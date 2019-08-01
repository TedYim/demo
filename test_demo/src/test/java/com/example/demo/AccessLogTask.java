package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class AccessLogTask implements Runnable {

    static final AccessLogTask LOG_TASK = new AccessLogTask();

    static Logger logger = LoggerFactory.getLogger(AccessLogTask.class);

//	RequestLogService requestLogService;


    static {
        System.out.println("AccessLogTask静态变量!!!");
    }
//
//
	public static void init(){
        System.out.println("AccessLogTask初始化!!!");
    }


    //List<AccessLogTaskHand> accessLogTaskHands=new ArrayList<AccessLogTaskHand>();
    private boolean flag        = true;
    private int     handlerSize = 5;

    private AccessLogTask() {
        //最好从redis读取
//		flag=redis读取
        /*for(int i=0;i<handlerSize;i++){
            accessLogTaskHands.add(new AccessLogTaskHand());
		}
		new Thread(this).start();*/
        System.err.println("AccessLogTask启动!!!!!!");
    }


    public void run() {
        //
        while (flag) {
            //自动检测是否需要采集日志
        }

    }

	/*class AccessLogTaskHand implements Runnable{
        public AccessLogTaskHand() {
			new Thread(this).start();
		}
		Vector<Object> vectory=new Vector<Object>();

		public void run() {
			while(true){
				try {
						if(vectory.size()==0){
							Thread.sleep(new Random().nextInt(1000));
							continue;
						}
						Object object=vectory.remove(0);
						logger.info("-------------------------------------"+object.toString());
						if(object instanceof RequestLog &&requestLogService!=null){
							requestLogService.saveAndFlush((RequestLog)object);
						}
					}catch (InterruptedException e) {
						e.printStackTrace();
				}
			}
		}
	}*/

    public static void addAccessLog(Object object) {
        System.err.println("添加元素成功!!!!!!" + object);
        if (object == null) {
            return;
        }
        /*if(LOG_TASK.flag){
            AccessLogTaskHand hand=LOG_TASK.accessLogTaskHands.get(new Random().nextInt(LOG_TASK.handlerSize));
			hand.vectory.add(object);
		}else{
			logger.info("-------------------------------------"+object.toString());
		}*/
    }
}
