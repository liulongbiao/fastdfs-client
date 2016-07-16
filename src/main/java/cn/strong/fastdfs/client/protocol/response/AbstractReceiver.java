/**
 * 
 */
package cn.strong.fastdfs.client.protocol.response;

import rx.Observable;
import rx.subjects.ReplaySubject;

/**
 * 抽象接收器类
 * 
 * @author liulongbiao
 *
 */
public abstract class AbstractReceiver<T> implements Receiver<T> {
	protected ReplaySubject<T> subject = ReplaySubject.create();

	@Override
	public void tryError(Throwable ex) {
		subject.onError(ex);
	}

	@Override
	public Observable<T> observable() {
		return subject.asObservable();
	}
}
