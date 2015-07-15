package com.gizwits.weixin.newGokitdog.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class AsynTaskQueue {
	private static AsynTaskQueue instance;
	private ThreadPoolExecutor threadPoolExecutor;
	private BlockingQueue<Runnable> queue;
	
	public static AsynTaskQueue shared() {
		if (instance == null) {
			instance = new AsynTaskQueue();
		}
		return instance;
	}
	
	private AsynTaskQueue() {
		
		setQueue(new LinkedBlockingQueue<Runnable>());
		threadPoolExecutor =  new ThreadPoolExecutor(2, 5, 1 , TimeUnit.DAYS, getQueue());
	}
	
	
	public void addTask(Runnable task) {
		threadPoolExecutor.execute(task);
	}

	public BlockingQueue<Runnable> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<Runnable> queue) {
		this.queue = queue;
	}
}
