/**
 * 
 */
package cn.strong.fastdfs.client.protocol.response;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeKey;

import java.nio.charset.Charset;

/**
 * 响应读取器
 * 
 * @author liulongbiao
 *
 */
public interface Receiver {
	AttributeKey<Receiver> RECEIVER = AttributeKey.valueOf("receiver");
	
	/**
	 * 预期的响应长度， 若长度可变则返回 -1
	 * 
	 * @return
	 */
	default long expectLength() {
		return -1;
	}

	/**
	 * 设置响应长度
	 * 
	 * @param length
	 */
	default void setLength(long length) {
		// ignore
	}

	/**
	 * 尝试读取内容
	 * 
	 * @param in
	 * @param charset
	 * @return 是否读取完毕
	 */
	boolean tryRead(ByteBuf in, Charset charset);
}
