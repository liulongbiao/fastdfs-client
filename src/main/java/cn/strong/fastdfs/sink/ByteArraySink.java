/**
 * 
 */
package cn.strong.fastdfs.sink;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.strong.fastdfs.utils.IOUtils;
import io.netty.buffer.ByteBuf;

/**
 * 字节数组 Sink
 * 
 * @author liulongbiao
 *
 */
public class ByteArraySink implements Sink {
	final ByteArrayOutputStream output;

	public ByteArraySink() {
		this.output = new ByteArrayOutputStream();
	}

	@Override
	public void close() throws IOException {
		IOUtils.closeQuietly(output);
	}

	@Override
	public void write(ByteBuf buf) {
		try {
			buf.readBytes(output, buf.readableBytes());
		} catch (IOException e) {
			throw new RuntimeException("write ByteBuf to output stream error", e);
		}
	}

	/**
	 * 获取字节数据
	 * 
	 * @return
	 */
	public byte[] getBytes() {
		return output.toByteArray();
	}
}
