/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request;

import static cn.strong.fastdfs.client.Consts.ERRNO_OK;
import static cn.strong.fastdfs.client.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.client.Consts.HEAD_LEN;
import static cn.strong.fastdfs.utils.Utils.writeFixLength;
import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import cn.strong.fastdfs.model.StoragePath;

/**
 * 请求内容为存储路径的请求
 * 
 * @author liulongbiao
 *
 */
public abstract class AbstractStoragePathRequest implements Request {

	private StoragePath spath;

	public AbstractStoragePathRequest(StoragePath spath) {
		this.spath = spath;
	}

	@Override
	public void encode(ChannelHandlerContext ctx, List<Object> out) {
		int length = FDFS_GROUP_LEN + spath.path.getBytes(UTF_8).length;
		byte cmd = cmd();
		ByteBuf buf = ctx.alloc().buffer(length + HEAD_LEN);
		buf.writeLong(length);
		buf.writeByte(cmd);
		buf.writeByte(ERRNO_OK);
		writeFixLength(buf, spath.group, FDFS_GROUP_LEN, UTF_8);
		ByteBufUtil.writeUtf8(buf, spath.path);
		out.add(buf);
	}

	protected abstract byte cmd();

}
