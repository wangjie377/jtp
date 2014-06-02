package johan.thread.fixed;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>BaseProcessor.java</code>
 * <p>
 * 功能:
 * 
 * @author 冯建华 56683216@qq.com 时间 2013-3-20 上午10:10:39
 * @version 1.0 </br>最后修改人 无
 */
public abstract class BaseProcessor implements IProcessor {

	protected int workerCount;

	protected int sleepMs;

	private List<GeneralThread> workers = new ArrayList<GeneralThread>();

	public BaseProcessor(int workerCount, int sleepMs) {
		this.workerCount = workerCount;
		this.sleepMs = sleepMs == 0 ? 10 : sleepMs;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void start() {
		for (int i = 0; i < workerCount; i++) {
			GeneralThread worker = new GeneralThread(this, sleepMs);
			workers.add(worker);
			worker.start();
		}
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void stop() {
		for (GeneralThread worker : workers)
			worker.stopWork();
	}

	public int getWorkerCount() {
		return workerCount;
	}

}
