/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * 请求发送器
 * 
 * @author liulongbiao
 *
 */
public interface Request {

	/**
	 * 编码内容，并防止到 out 中
	 * 
	 * @param ctx
	 * @param out
	 */
	void encode(ChannelHandlerContext ctx, List<Object> out);
}
