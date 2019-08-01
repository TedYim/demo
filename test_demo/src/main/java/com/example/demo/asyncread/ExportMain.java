package com.example.demo.asyncread;

public class ExportMain {
	public static void main(String[] args) {
		//请求过来。。。
		AbstractQueryTask<MemberData> task=new MemberDownLoadQueryTask(100);
		ExecutorServiceUtil.service.submit(task);
		while(!task.isFinish()){
			MemberData memberData=task.next();
			if(memberData!=null){
				System.out.println(memberData);
			}
			
		}
		
		
	}
}
