/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request.storage;

import static cn.strong.fastdfs.client.CommandCodes.STORAGE_PROTO_CMD_DOWNLOAD_FILE;
import static cn.strong.fastdfs.client.Consts.ERRNO_OK;
import static cn.strong.fastdfs.client.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.client.Consts.FDFS_LONG_LEN;
import static cn.strong.fastdfs.client.Consts.HEAD_LEN;
import static cn.strong.fastdfs.utils.Utils.writeFixLength;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import cn.strong.fastdfs.client.protocol.request.Request;
import cn.strong.fastdfs.model.StoragePath;

/**
 * 下载请求
 * 
 * @author liulongbiao
 *
 */
public class DownloadRequest implements Request {

	public static final int DEFAULT_OFFSET = 0;
	public static final int SIZE_UNLIMIT = 0;

	public final StoragePath spath;
	public final int offset;
	public final int size;

	public DownloadRequest(StoragePath spath, int offset, int size) {
		this.spath = Objects.requireNonNull(spath);
		this.offset = offset;
		this.size = size;
	}

	public DownloadRequest(StoragePath spath) {
		this(spath, DEFAULT_OFFSET, SIZE_UNLIMIT);
	}

	@Override
	public void encode(ByteBufAllocator alloc, List<Object> out, Charset charset) {
		byte[] pathBytes = spath.path.getBytes(charset);
		int length = 2 * FDFS_LONG_LEN + FDFS_GROUP_LEN + pathBytes.length;
		byte cmd = STORAGE_PROTO_CMD_DOWNLOAD_FILE;
		ByteBuf buf = alloc.buffer(length + HEAD_LEN);
		buf.writeLong(length);
		buf.writeByte(cmd);
		buf.writeByte(ERRNO_OK);

		buf.writeLong(offset);
		buf.writeLong(size);
		writeFixLength(buf, spath.group, FDFS_GROUP_LEN, charset);
		buf.writeBytes(pathBytes);
		out.add(buf);
	}

}
