/**
 * 
 */
package cn.strong.fastdfs.client.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import cn.strong.fastdfs.client.protocol.request.Request;

/**
 * Fastdfs 请求发送处理器
 * 
 * @author liulongbiao
 *
 */
@Sharable
public class FastdfsEncoder extends MessageToMessageEncoder<Request> {

	public static FastdfsEncoder INSTANCE = new FastdfsEncoder();

	@Override
	protected void encode(ChannelHandlerContext ctx, Request msg, List<Object> out)
			throws Exception {
		msg.encode(ctx, out);
	}

}
