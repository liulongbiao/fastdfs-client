/**
 * 
 */
package cn.strong.fastdfs.client.protocol.response;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * 空内容响应接收器
 * 
 * @author liulongbiao
 *
 */
public class EmptyReceiver implements Receiver<Void> {

	private PublishSubject<Void> subject = PublishSubject.create();

	@Override
	public long expectLength() {
		return 0;
	}

	@Override
	public boolean tryRead(ByteBuf in, Charset charset) {
		subject.onCompleted();
		return true;
	}

	@Override
	public Observable<Void> observable() {
		return subject;
	}
}
