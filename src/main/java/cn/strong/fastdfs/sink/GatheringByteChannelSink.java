/**
 * 
 */
package cn.strong.fastdfs.sink;

import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.util.Objects;

import cn.strong.fastdfs.utils.IOUtils;
import io.netty.buffer.ByteBuf;

/**
 * 基于 GatheringByteChannel 的 Sink 实现
 * 
 * @author liulongbiao
 *
 */
public class GatheringByteChannelSink implements Sink {
	private GatheringByteChannel channel;

	public GatheringByteChannelSink(GatheringByteChannel channel) {
		this.channel = Objects.requireNonNull(channel, "channel is required");
	}

	@Override
	public void close() throws IOException {
		IOUtils.closeQuietly(channel);
	}

	@Override
	public void write(ByteBuf buf) {
		try {
			buf.readBytes(channel, buf.readableBytes());
		} catch (IOException e) {
			throw new RuntimeException("write ByteBuf to channel error", e);
		}
	}

}
