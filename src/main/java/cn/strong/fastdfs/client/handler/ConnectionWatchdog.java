/**
 * 
 */
package cn.strong.fastdfs.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.strong.fastdfs.ex.FastdfsTimeoutException;

/**
 * 链接守护处理器
 * 
 * @author liulongbiao
 *
 */
public class ConnectionWatchdog extends ChannelInboundHandlerAdapter {
	private final Logger LOG = LoggerFactory.getLogger(ConnectionWatchdog.class);

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			throw new FastdfsTimeoutException("channel was idle for maxIdleSeconds");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof FastdfsTimeoutException) {
			LOG.info(cause.getMessage(), cause);
		} else {
			LOG.error(cause.getMessage(), cause);
		}
		ctx.close();
	}
}
