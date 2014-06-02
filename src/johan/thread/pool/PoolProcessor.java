package johan.thread.pool;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <code>BaseProcessor.java</code>
 * <p>
 * 功能:
 * 
 * @author 冯建华 56683216@qq.com 时间 2013-3-20 上午10:10:39
 * @version 1.0 </br>最后修改人 无
 */
public abstract class PoolProcessor<T> implements IPoolProcessor<T> {

	protected int minWorker, maxWorker, maxJob, warnJobCount;

	protected int sleepMs;

	protected static final int WORKER_RETRY_TIMES = 3;

	private Map<PoolThread, PoolThread> workers;

	protected ArrayBlockingQueue<T> jobs;

	public PoolProcessor(int minWorker, int maxWorker, int maxJob, int sleepMs) {
		this.minWorker = minWorker > 0 ? minWorker : 1;
		this.maxWorker = maxWorker > this.minWorker ? maxWorker : this.minWorker;
		this.maxJob = maxJob > this.minWorker ? maxJob : this.minWorker;
		this.warnJobCount = this.maxJob * 3 / 4 + 1;
		this.sleepMs = sleepMs == 0 ? 10 : sleepMs;
	}

	@Override
	public void start() {
		jobs = new ArrayBlockingQueue<T>(maxJob);
		workers = new ConcurrentHashMap<PoolThread, PoolThread>(this.maxWorker);
		for (int i = 0; i < minWorker; i++) {
			PoolThread worker = new PoolThread(this, sleepMs, WORKER_RETRY_TIMES);
			workers.put(worker, worker);
			worker.start();
		}
	}

	@Override
	public void stop() {
		for (PoolThread worker : workers.values())
			worker.stopWork();
	}

	@Override
	public boolean execute(T t) {
		boolean ok = false;
		try {
			jobs.add(t);
			ok = true;
		} catch (IllegalStateException e) {
		}
		if (jobs.size() >= warnJobCount) {
			if (jobs.size() % WORKER_RETRY_TIMES == 0)// in case of too much
				increaseWorker();
		}
		return ok;

	}

	@Override
	public void executeTruely(T t) {
		int retry = 0;
		while (true) {
			try {
				jobs.add(t);
				break;
			} catch (IllegalStateException e) {
				System.out.println("Thread[" + getName() + "] queue waiting.. " + (++retry));
				try {
					Thread.sleep(sleepMs);
				} catch (InterruptedException e1) {
				}
			}
		}
		if (jobs.size() >= warnJobCount) {
			if (jobs.size() % WORKER_RETRY_TIMES == 0)// in case of too much
				increaseWorker();
		}
	}

	public int getWorkerCount() {
		return workers.size();
	}

	public void increaseWorker() {
		PoolThread worker = new PoolThread(this, sleepMs, WORKER_RETRY_TIMES);
		workers.put(worker, worker);
		worker.start();
	}

	@Override
	public synchronized boolean reduceWorker(PoolThread worker) {
		if (workers.size() > minWorker) {
			worker.stopWork();
			workers.remove(worker);
			return true;
		}
		return false;
	}

	@Override
	public boolean process() throws Exception {
		T t = jobs.poll();
		if (t == null)
			return false;
		return doJob(t);
	}

}
