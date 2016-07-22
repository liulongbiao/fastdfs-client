/**
 * 
 */
package cn.strong.fastdfs.client.protocol.response;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * Rx 响应接收器
 * 
 * @author liulongbiao
 *
 */
public class RxReceiver extends AbstractReceiver<ByteBuf> {

	private long length;
	private long readed = 0;

	@Override
	public void setLength(long length) {
		this.length = length;
	}

	@Override
	public boolean tryRead(ByteBuf in, Charset charset) {
		if (length <= readed) {
			if (!subject.hasThrowable()) {
				subject.onCompleted();
			}
			return true;
		}
		int len = (int) Math.min(length - readed, in.readableBytes());
		ByteBuf buf = in.readBytes(len);
		subject.onNext(buf);
		readed += len;
		if (length <= readed) {
			subject.onCompleted();
			return true;
		} else {
			return false;
		}
	}
}
