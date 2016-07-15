/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request;

import static cn.strong.fastdfs.client.Consts.ERRNO_OK;
import static cn.strong.fastdfs.client.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.client.Consts.HEAD_LEN;
import static cn.strong.fastdfs.utils.Utils.writeFixLength;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;
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
	public void encode(ByteBufAllocator alloc, List<Object> out, Charset charset) {
		byte[] pathBytes = spath.path.getBytes(charset);
		int length = FDFS_GROUP_LEN + pathBytes.length;
		byte cmd = cmd();
		ByteBuf buf = alloc.buffer(length + HEAD_LEN);
		buf.writeLong(length);
		buf.writeByte(cmd);
		buf.writeByte(ERRNO_OK);
		writeFixLength(buf, spath.group, FDFS_GROUP_LEN, charset);
		buf.writeBytes(pathBytes);
		out.add(buf);
	}

	protected abstract byte cmd();

}
