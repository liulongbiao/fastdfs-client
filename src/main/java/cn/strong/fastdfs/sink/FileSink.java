/**
 * 
 */
package cn.strong.fastdfs.sink;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;

import cn.strong.fastdfs.utils.RxIOUtils;
import io.netty.buffer.ByteBuf;

/**
 * 将内容写入文件
 * 
 * @author liulongbiao
 *
 */
public class FileSink implements Sink {

	final File file;
	final FileChannel channel;

	public FileSink(File file) {
		this.file = file;
		this.channel = RxIOUtils.openFileChannel(file);
	}

	@Override
	public void close() throws IOException {
		RxIOUtils.closeQuietly(channel);
	}

	@Override
	public void write(ByteBuf buf) {
		try {
			buf.readBytes(channel, buf.readableBytes());
		} catch (IOException e) {
			throw new RuntimeException("write ByteBuf to file error", e);
		}
	}

	/**
	 * 获取底层的文件
	 * 
	 * @return
	 */
	public File getFile() {
		return this.file;
	}

}
