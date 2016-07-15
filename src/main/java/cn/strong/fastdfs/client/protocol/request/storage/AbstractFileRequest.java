/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request.storage;

import static cn.strong.fastdfs.client.Consts.ERRNO_OK;
import static cn.strong.fastdfs.client.Consts.HEAD_LEN;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.handler.stream.ChunkedInput;
import io.netty.handler.stream.ChunkedNioStream;
import io.netty.handler.stream.ChunkedStream;

import java.io.File;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import cn.strong.fastdfs.client.protocol.request.Request;

/**
 * 抽象文件请求
 * 
 * @author liulongbiao
 *
 */
public abstract class AbstractFileRequest implements Request {

	public final Object content;
	public final long size;

	public AbstractFileRequest(File file) {
		if (!file.exists()) {
			throw new IllegalArgumentException("uploaded file " + file.getName() + " not exist");
		}
		long length = file.length();
		this.content = new DefaultFileRegion(file, 0, length);
		this.size = length;
	}

	public AbstractFileRequest(Object content, long size) {
		this.content = toContent(content);
		this.size = size;
	}

	private static Object toContent(Object content) {
		Objects.requireNonNull(content, "uploaded content shoule not be null");
		if (content instanceof ByteBuf || content instanceof FileRegion
				|| content instanceof ChunkedInput) {
			return content;
		} else if (content instanceof File) {
			File file = (File) content;
			if (!file.exists()) {
				throw new IllegalArgumentException("uploaded file " + file.getName() + " not exist");
			}
			return new DefaultFileRegion(file, 0, file.length());
		} else if (content instanceof ReadableByteChannel) {
			return new ChunkedNioStream((ReadableByteChannel) content);
		} else if (content instanceof InputStream) {
			return new ChunkedStream((InputStream) content);
		} else if (content instanceof byte[]) {
			return Unpooled.wrappedBuffer((byte[]) content);
		} else {
			throw new IllegalArgumentException("unknown content type : "
					+ content.getClass().getName());
		}
	}

	@Override
	public void encode(ByteBufAllocator alloc, List<Object> out, Charset charset) {
		ByteBuf meta = meta(alloc, charset);

		ByteBuf head = alloc.buffer(HEAD_LEN);
		head.writeLong(meta.readableBytes() + size);
		head.writeByte(cmd());
		head.writeByte(ERRNO_OK);

		out.add(head);
		out.add(meta);
		out.add(content);
	}
	
	/**
	 * 请求命令
	 * 
	 * @return
	 */
	protected abstract byte cmd();

	/**
	 * 文件内容前的元数据
	 * 
	 * @param alloc
	 * @return
	 */
	protected abstract ByteBuf meta(ByteBufAllocator alloc, Charset charset);

}
