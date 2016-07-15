/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request.storage;

import static cn.strong.fastdfs.client.Consts.ERRNO_OK;
import static cn.strong.fastdfs.client.Consts.FDFS_FIELD_SEPERATOR;
import static cn.strong.fastdfs.client.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.client.Consts.FDFS_LONG_LEN;
import static cn.strong.fastdfs.client.Consts.FDFS_RECORD_SEPERATOR;
import static cn.strong.fastdfs.client.Consts.HEAD_LEN;
import static cn.strong.fastdfs.utils.Utils.writeFixLength;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.strong.fastdfs.client.CommandCodes;
import cn.strong.fastdfs.client.protocol.request.Request;
import cn.strong.fastdfs.model.Metadata;
import cn.strong.fastdfs.model.StoragePath;

/**
 * 设置文件属性请求
 * 
 * @author liulongbiao
 *
 */
public class SetMetadataRequest implements Request {

	public final StoragePath spath;
	public final Metadata metadata;
	public final byte flag;

	public SetMetadataRequest(StoragePath spath, Metadata metadata, byte flag) {
		this.spath = Objects.requireNonNull(spath);
		this.metadata = metadata;
		this.flag = flag;
	}

	@Override
	public void encode(ByteBufAllocator alloc, List<Object> out, Charset charset) {
		byte[] pathBytes = spath.path.getBytes(charset);
		byte[] metadatas = toBytes(metadata, charset);
		int length = 2 * FDFS_LONG_LEN + 1 + FDFS_GROUP_LEN + pathBytes.length + metadatas.length;
		byte cmd = CommandCodes.STORAGE_PROTO_CMD_SET_METADATA;
		ByteBuf buf = alloc.buffer(length + HEAD_LEN);
		buf.writeLong(length);
		buf.writeByte(cmd);
		buf.writeByte(ERRNO_OK);

		buf.writeLong(pathBytes.length);
		buf.writeLong(metadatas.length);
		buf.writeByte(flag);
		writeFixLength(buf, spath.group, FDFS_GROUP_LEN, charset);
		buf.writeBytes(pathBytes);
		buf.writeBytes(metadatas);

		out.add(buf);
	}

	/*
	 * 将元数据编码为字节数组
	 */
	private byte[] toBytes(Metadata metadata, Charset charset) {
		Map<String, String> map = metadata.toMap();
		if (map.isEmpty()) {
			return new byte[0];
		}

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (!first) {
				sb.append(FDFS_RECORD_SEPERATOR);
			}
			sb.append(entry.getKey());
			sb.append(FDFS_FIELD_SEPERATOR);
			sb.append(entry.getValue());
			first = false;
		}
		return sb.toString().getBytes(charset);
	}

}
