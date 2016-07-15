/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request;

import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 请求发送器
 * 
 * @author liulongbiao
 *
 */
public interface Request {

	/**
	 * 编码内容，并放到 out 中
	 * 
	 * @param alloc
	 * @param out
	 * @param charset
	 */
	void encode(ByteBufAllocator alloc, List<Object> out, Charset charset);
}
