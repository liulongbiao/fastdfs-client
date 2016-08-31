/**
 * 
 */
package cn.strong.fastdfs.sink;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import cn.strong.fastdfs.utils.RxIOUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author liulongbiao
 *
 */
public class OutoutStreamSink implements Sink {
	final OutputStream output;

	public OutoutStreamSink(OutputStream output) {
		this.output = Objects.requireNonNull(output, "output stream is required");
	}

	@Override
	public void close() throws IOException {
		RxIOUtils.closeQuietly(output);
	}

	@Override
	public void write(ByteBuf buf) {
		try {
			buf.readBytes(output, buf.readableBytes());
		} catch (IOException e) {
			throw new RuntimeException("write ByteBuf to output stream error", e);
		}
	}
}
