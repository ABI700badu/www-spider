package com.spider.core.engine;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 此队列实现的功能：
 * 1: 保证队列里面元素的唯一性：只要被添加过一次就不再被添加
 * 2: 多线程安全
 * 3: 入队与出队互不影响
 */
public class BlockedUniQueue {
	/**
	 * 用于保存队列元素
	 */
	private  LinkedList<Object> mQueue=new LinkedList<Object>();
	/**
	 * 用于记录当前元素的位置
	 */
	private final AtomicInteger mCusor=new AtomicInteger(0);
	/**
	 * 用于设置队列的最大值
	 */
	private  int mLimit=1000;
	/**
	 * 用于获取队列元素的索引
	 */
	private int headIndex=0;
	/**
	 * 一个用于同步队头的锁
	 */
	private final Lock headLock=new ReentrantLock();
	/**
	 * 一个阻塞队列
	 */
	private final Condition empty =headLock.newCondition();
	/**
	 * 给队列限制最大的添加数量
	 * @param limit
	 */
	protected void setPosLimit(int limit){
		mLimit=limit;
	}
	/**
	 * 入队列
	 * @param object 入队元素
	 */
	public void enQueue(String object){
		if(object==null){
			return;
		}
		headLock.lock();
		try {
			if (!mQueue.contains(object) && mCusor.get() < mLimit) {
				mQueue.add(object);
				mCusor.incrementAndGet();
				//此时消费者可以消费
				empty.signal();
			}
		}finally {
			headLock.unlock();
		}
	}
	/**
	 * 判断是不是添加的已经达到最大限制
	 * 注意此方法绝对安全，它一定正确的指示了当前游标是不是超过了limit
	 * 但是它的值存在跳跃性，可能此时Cusor比limit要大很多，取决于并发线程的add操作
	 * @return
	 */
	public boolean isBeyondLimit(){
		return mCusor.get() >= mLimit;
	}
	/**
	 * 用于保证多线程能够获取到
	 *若20秒为获取到任何数据将会爆发异常
	 * @return
	 */
	public Object blockingTake() throws InterruptedException {
		long nanos=TimeUnit.MILLISECONDS.toNanos(20000);
		headLock.lockInterruptibly();
		try {
			while (headIndex>=mQueue.size()){//空
				if(nanos <= 0){
					return null;
				}
				//挂起消费者线程
				nanos=empty.awaitNanos(nanos);
			}
			return mQueue.get(headIndex++);
		}finally {
			headLock.unlock();
		}
	}
}
