/**
 * 
 */
package cn.strong.fastdfs.sink;

import java.io.Closeable;

import io.netty.buffer.ByteBuf;

/**
 * 内容处理
 * 
 * @author liulongbiao
 *
 */
public interface Sink extends Closeable {
	/**
	 * 写出内容
	 * 
	 * @param buf
	 */
	void write(ByteBuf buf);
}
