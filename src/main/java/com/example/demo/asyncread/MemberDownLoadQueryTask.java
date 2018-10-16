package com.example.demo.asyncread;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MemberDownLoadQueryTask extends AbstractQueryTask<MemberData>{
	
	public MemberDownLoadQueryTask(int pageSize) {
		super(pageSize);
	}
	
	@Override
	public int getTotal() {
		//在这里查询远程服务，查询得到总数
		return 0;
	}

	
	@Override
	FutureTask<List<MemberData>> doRequestTask(int pageSize, int currentSize) {
		RequestTask requestTask=new RequestTask(pageSize, currentSize);
		FutureTask<List<MemberData>> futureTask = new FutureTask<List<MemberData>>(requestTask);
		ExecutorServiceUtil.service.submit(futureTask);
		return futureTask;
	}
	
	class RequestTask implements Callable<List<MemberData>>{
		
		int pageSize;
		
		int currentSize;
		
		public RequestTask(int pageSize, int currentSize) {
			this.pageSize=pageSize;
			this.currentSize=currentSize;
		}

		@Override
		public List<MemberData> call() throws Exception {
			//查询数据库
			return null;
		}
		
	}
	
}
