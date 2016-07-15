/**
 * 
 */
package cn.strong.fastdfs.client.protocol.response;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * 空内容响应接收器
 * 
 * @author liulongbiao
 *
 */
public class EmptyReceiver extends AbstractReceiver<Void> {

	@Override
	public long expectLength() {
		return 0;
	}

	@Override
	public boolean tryRead(ByteBuf in, Charset charset) {
		subject.onCompleted();
		return true;
	}
}
