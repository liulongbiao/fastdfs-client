/**
 * 
 */
package cn.strong.fastdfs.client;

import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.util.concurrent.Future;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

import rx.Observable;

import cn.strong.fastdfs.client.protocol.request.Request;
import cn.strong.fastdfs.client.protocol.response.Receiver;
import cn.strong.fastdfs.ex.FastdfsException;

/**
 * FastDFS 连接交互模板类
 * 
 * @author liulongbiao
 *
 */
public class FastdfsTemplate implements Closeable {

	final NioEventLoopGroup group;
	final FastdfsChannelPoolMap poolMap;

	public FastdfsTemplate(Settings settings) {
		if (settings == null) {
			settings = new Settings();
		}
		group = new NioEventLoopGroup(settings.getEventLoopThreads());
		poolMap = new FastdfsChannelPoolMap(group, settings);
	}

	/**
	 * 访问 Fastdfs 服务器
	 * 
	 * @param addr
	 * @param sender
	 * @param decoder
	 * @return
	 */
	public <T> Observable<T> execute(InetSocketAddress addr, Request request, Receiver<T> receiver) {
		FixedChannelPool pool = poolMap.get(addr);
		pool.acquire().addListener((Future<Channel> f) -> {
			if (f.isCancelled()) {
				receiver.tryError(new FastdfsException("connection is canceled"));
			} else if (!f.isSuccess()) {
				receiver.tryError(f.cause());
			} else {
				Channel ch = f.getNow();
				receiver.observable().doAfterTerminate(() -> {
					pool.release(ch);
				});
				try {
					ch.attr(Receiver.RECEIVER).set(receiver);
					ch.writeAndFlush(request);
				} catch (Exception e) {
					receiver.tryError(e);
				}
			}
		});
		return receiver.observable();
	}

	@PreDestroy
	public void close() throws IOException {
		if (poolMap != null) {
			try {
				poolMap.close();
			} catch (Exception e) {
				// ignore
			}
		}
		if (group != null) {
			group.shutdownGracefully();
		}
	}
}
