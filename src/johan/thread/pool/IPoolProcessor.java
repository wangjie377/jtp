package johan.thread.pool;

/**
 * <code>IProcessor.java</code>
 * <p>
 * 功能:
 * 
 * @author johanfong 56683216@qq.com 时间 2013-3-8 下午11:48:29
 * @version 1.0 </br>最后修改人 无
 */
public interface IPoolProcessor<T> {
	public boolean process() throws Exception;

	public boolean doJob(T t) throws Exception;

	public String getName();

	public void start();

	public void stop();

	public boolean reduceWorker(PoolThread worker);

	public boolean execute(T t);
	
	public void executeTruely(T t);
}
