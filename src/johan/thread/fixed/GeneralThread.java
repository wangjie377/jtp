package johan.thread.fixed;

import org.apache.log4j.Logger;

/**
 * <code>GeneralThread.java</code>
 * <p>
 * 功能:
 * 
 * @author johanfong 56683216@qq.com 时间 2013-3-8 下午11:42:57
 * @version 1.0 </br>最后修改人 无
 */
public class GeneralThread extends Thread {
	public GeneralThread(IProcessor processor, int sleepMs) {
		this.processor = processor;
		this.sleepMs = (sleepMs == 0) ? 100 : sleepMs;
		active = true;
	}

	public GeneralThread(IProcessor processor, int sleepMs, int stackSize) {
		super(null, null, processor.getName(), stackSize);
		this.processor = processor;
		this.sleepMs = (sleepMs == 0) ? 100 : sleepMs;
		active = true;
	}

	/**
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		logger.info("the Thread[" + processor.getName() + "] started.");
		while (active) {
			try {
				processor.process();
			} catch (InterruptedException e) {
				break;
			} catch (Exception e) {
				logger.info("Thread[" + processor.getName() + "] process failed", e);
			}
			try {
				sleep(sleepMs);
			} catch (InterruptedException e) {
				break;
			}
		}
		active = false;
		logger.info("the Thread[" + processor.getName() + "] now exited.");
	}

	public void stopWork() {
		active = false;
		try {
			this.join();
		} catch (InterruptedException e) {
			logger.error("fail to waiting the thread[" + processor.getName() + "] finish using join()!", e);
		}
	}

	private IProcessor processor;

	private int sleepMs;

	protected volatile boolean active;

	private static final Logger logger = Logger.getLogger(GeneralThread.class);
}
