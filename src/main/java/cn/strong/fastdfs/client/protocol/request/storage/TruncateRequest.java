/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request.storage;

import static cn.strong.fastdfs.client.Consts.ERRNO_OK;
import static cn.strong.fastdfs.client.Consts.FDFS_PROTO_PKG_LEN_SIZE;
import static cn.strong.fastdfs.client.Consts.HEAD_LEN;
import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import cn.strong.fastdfs.client.CommandCodes;
import cn.strong.fastdfs.client.protocol.request.Request;
import cn.strong.fastdfs.model.StoragePath;

/**
 * 截取请求
 * 
 * @author liulongbiao
 *
 */
public class TruncateRequest implements Request {

	public final StoragePath spath;
	public final int truncatedSize;

	public TruncateRequest(StoragePath spath, int truncatedSize) {
		this.spath = spath;
		this.truncatedSize = truncatedSize;
	}

	@Override
	public void encode(ChannelHandlerContext ctx, List<Object> out) {
		byte[] pathBytes = spath.path.getBytes(UTF_8);
		int length = 2 * FDFS_PROTO_PKG_LEN_SIZE + pathBytes.length;
		byte cmd = CommandCodes.STORAGE_PROTO_CMD_TRUNCATE_FILE;
		ByteBuf buf = ctx.alloc().buffer(length + HEAD_LEN);
		buf.writeLong(length);
		buf.writeByte(cmd);
		buf.writeByte(ERRNO_OK);
		buf.writeLong(pathBytes.length);
		buf.writeLong(truncatedSize);
		buf.writeBytes(pathBytes);

		out.add(buf);
	}

}
