/**
 * 
 */
package cn.strong.fastdfs.client.handler;

import static cn.strong.fastdfs.client.CommandCodes.FDFS_PROTO_CMD_RESP;

import java.nio.charset.Charset;
import java.util.List;

import cn.strong.fastdfs.client.Consts;
import cn.strong.fastdfs.client.protocol.response.Receiver;
import cn.strong.fastdfs.ex.FastdfsException;
import cn.strong.fastdfs.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * FastDFS 响应解码器
 * 
 * @author liulongbiao
 *
 */
public class FastdfsDecoder extends ReplayingDecoder<Void> {

	private boolean head = true;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (head) {
			readHead(ctx, in);
		} else {
			readContent(ctx, in);
		}
	}

	private void readHead(ChannelHandlerContext ctx, ByteBuf in) {
		long length = in.readLong();
		byte cmd = in.readByte();
		byte errno = in.readByte();
		if (errno != Consts.ERRNO_OK) {
			throw new FastdfsException("Fastdfs responsed with an error, errno is " + errno);
		}
		if (cmd != FDFS_PROTO_CMD_RESP) {
			throw new FastdfsException("Expect response command code error : " + cmd);
		}

		Receiver<?> receiver = ensureGetReceiver(ctx);
		long expectLength = receiver.expectLength();
		if (expectLength >= 0) {
			if (length != expectLength) {
				throw new FastdfsException("Expect response length : " + expectLength
						+ " , but receive length : " + length);
			}
		}
		receiver.setLength(length);

		head = false;
		checkpoint();

		// 马上调用一次 readContent，防止 EmptyReceiver 不会走 readContent
		readContent(ctx, in);
	}

	private Receiver<?> ensureGetReceiver(ChannelHandlerContext ctx) {
		Receiver<?> resp = ctx.channel().attr(Receiver.RECEIVER).get();
		if (resp == null) {
			throw new FastdfsException("Receiver is not exist in channel");
		}
		return resp;
	}

	private void readContent(ChannelHandlerContext ctx, ByteBuf in) {
		Receiver<?> receiver = ensureGetReceiver(ctx);
		Charset charset = Utils.ensureGetCharset(ctx.channel());
		boolean ended = receiver.tryRead(in, charset);
		if (ended) {
			head = true;
		}
		checkpoint();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ConnectionWatchdog.onError(ctx, cause);
	}
}
