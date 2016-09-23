/**
 * 
 */
package cn.strong.fastdfs.client.protocol.response;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

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
		promise.complete(null);
		return true;
	}

}
