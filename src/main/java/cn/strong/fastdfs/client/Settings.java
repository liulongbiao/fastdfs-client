/**
 * 
 */
package cn.strong.fastdfs.client;

/**
 * 连接设置
 * 
 * @author liulongbiao
 *
 */
public class Settings {

	private int eventLoopThreads = 0; // 线程数量
	private int maxConnPerHost = 10; // 每个IP最大连接数
	private int timeout = 10000; // 超时时间(毫秒)
	private int maxIdleSeconds = 300; // 最大闲置时间(秒)
	private String charset = "UTF-8"; // 字符集

	public int getEventLoopThreads() {
		return eventLoopThreads;
	}

	public void setEventLoopThreads(int eventLoopThreads) {
		this.eventLoopThreads = eventLoopThreads;
	}

	public int getMaxConnPerHost() {
		return maxConnPerHost;
	}

	public void setMaxConnPerHost(int maxConnPerHost) {
		this.maxConnPerHost = maxConnPerHost;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getMaxIdleSeconds() {
		return maxIdleSeconds;
	}

	public void setMaxIdleSeconds(int maxIdleSeconds) {
		this.maxIdleSeconds = maxIdleSeconds;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
