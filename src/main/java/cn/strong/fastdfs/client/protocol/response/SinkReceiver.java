/**
 * 
 */
package cn.strong.fastdfs.client.protocol.response;

import java.nio.charset.Charset;
import java.util.Objects;

import cn.strong.fastdfs.sink.Sink;
import cn.strong.fastdfs.sink.SinkProgressListener;
import cn.strong.fastdfs.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

/**
 * 基于 Sink 的接收器
 * 
 * @author liulongbiao
 *
 */
public class SinkReceiver extends AbstractReceiver<Void> {

	private long length;
	private long readed = 0;
	private Sink sink;
	private SinkProgressListener listener;

	public SinkReceiver(Sink sink) {
		this(sink, null);
	}

	public SinkReceiver(Sink sink, SinkProgressListener listener) {
		this.sink = Objects.requireNonNull(sink, "sink is required");
		this.listener = listener;
	}

	@Override
	public void setLength(long length) {
		this.length = length;
	}

	@Override
	public boolean tryRead(ByteBuf in, Charset charset) {
		if (length <= readed) {
			complete();
			return true;
		}
		int len = (int) Math.min(length - readed, in.writerIndex() - in.readerIndex());
		ByteBuf buf = null;
		try {
			buf = in.readBytes(len);
			sink.write(buf);
		} finally {
			ReferenceCountUtil.safeRelease(buf);
		}

		readed += len;
		if (listener != null) {
			listener.onProgress(readed, length);
		}

		if (length <= readed) {
			complete();
			return true;
		} else {
			return false;
		}
	}

	private void complete() {
		IOUtils.closeQuietly(sink);
		promise.complete(null);
	}

}
