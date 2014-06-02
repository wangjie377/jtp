package johan.thread.fixed;

/**
 * <code>IProcessor.java</code>
 * <p>
 * ����:
 * 
 * @author johanfong 56683216@qq.com ʱ�� 2013-3-8 ����11:48:29
 * @version 1.0 </br>����޸��� ��
 */
public interface IProcessor {
	public void process() throws Exception;

	public String getName();

	public void start();

	public void stop();
}
