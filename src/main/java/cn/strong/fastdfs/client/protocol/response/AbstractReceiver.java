/**
 * 
 */
package cn.strong.fastdfs.client.protocol.response;

import java.util.concurrent.CompletableFuture;

/**
 * 抽象接收器类
 * 
 * @author liulongbiao
 *
 */
public abstract class AbstractReceiver<T> implements Receiver<T> {
	protected CompletableFuture<T> promise = new CompletableFuture<>();

	@Override
	public void tryError(Throwable ex) {
		promise.completeExceptionally(ex);
	}

	@Override
	public CompletableFuture<T> promise() {
		return promise;
	}
}
