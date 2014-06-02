package johan.thread.pool;

/**
 * <code>GeneralThread.java</code>
 * <p>
 * 功能:
 * 
 * @author johanfong 56683216@qq.com 时间 2013-3-8 下午11:42:57
 * @version 1.0 </br>最后修改人 无
 */
@SuppressWarnings("rawtypes")
public class PoolThread extends Thread {
	public PoolThread(IPoolProcessor processor, int sleepMs, int retryTimes) {
		super();
		this.processor = processor;
		this.sleepMs = (sleepMs == 0) ? 100 : sleepMs;
		active = true;
		this.retryTimes = (retryTimes > 0) ? retryTimes : 1;
	}

	public PoolThread(IPoolProcessor processor, int sleepMs, int retryTimes, int stackSize) {
		super(null, null, processor.getName(), stackSize);
		this.processor = processor;
		this.sleepMs = (sleepMs == 0) ? 100 : sleepMs;
		active = true;
		this.retryTimes = (retryTimes > 0) ? retryTimes : 1;
	}

	@Override
	public void run() {
		System.out.println("the Thread[" + processor.getName() + "] started.");
		while (active) {
			try {
				if (processor.process()) {
					retry = 0;
					continue;
				} else {
					if (++retry > retryTimes) {
						if (processor.reduceWorker(this)) {
							break;
						}
					}
				}
			} catch (InterruptedException e) {
				break;
			} catch (Exception e) {
				System.err.println("Thread[" + processor.getName() + "] process failed.\n" + e.getMessage());
			}
			try {
				sleep(sleepMs);
			} catch (InterruptedException e) {
				break;
			}
		}
		active = false;
		System.out.println("the Thread[" + processor.getName() + "] now exited.");
	}

	public void stopWork() {
		active = false;
		try {
			this.join();
		} catch (InterruptedException e) {
			System.err.println("fail to wait the thread[" + processor.getName() + "] finished using join()! Error Message:\n"
					+ e.getMessage());
		}
	}

	private IPoolProcessor processor;

	private int sleepMs, retryTimes, retry;

	protected volatile boolean active;

}
