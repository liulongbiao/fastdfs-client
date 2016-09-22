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
@FunctionalInterface
public interface SinkProgressListener {

	/**
	 * 处理进度事件
	 * 
	 * @param progress
	 */
	void onProgress(long progress, long total);
}
