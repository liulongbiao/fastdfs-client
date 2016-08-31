/**
 * 
 */
package cn.strong.fastdfs.sink;

/**
 * Sink 进度监听器
 * 
 * @author liulongbiao
 *
 */
public interface SinkProgressListener {

	/**
	 * 处理初始化总长度事件
	 * 
	 * @param length
	 */
	void onInitLength(long length);

	/**
	 * 处理进度事件
	 * 
	 * @param progress
	 */
	void onProgress(long progress);
}
