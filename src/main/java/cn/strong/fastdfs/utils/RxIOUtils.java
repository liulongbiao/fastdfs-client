/**
 * 
 */
package cn.strong.fastdfs.utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

/**
 * IO 工具类
 * 
 * @author liulongbiao
 *
 */
public class RxIOUtils {
	/**
	 * 关闭
	 * 
	 * @param closeable
	 */
	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * open FileChannel to write
	 * 
	 * @param file
	 * @return
	 */
	public static FileChannel openFileChannel(File file) {
		RxIOUtils.initFileToWrite(file);
		try {
			return FileChannel.open(file.toPath(), StandardOpenOption.WRITE);
		} catch (IOException e) {
			throw new RuntimeException("open file channel error", e);
		}
	}

	/**
	 * 初始化待写入的文件
	 * 
	 * @param file
	 */
	public static void initFileToWrite(File file) {
		if (file == null) {
			throw new RuntimeException("file to write is not specified");
		}
		File dir = file.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException("cannot create file : " + file.getAbsolutePath());
			}
		}
		if (!file.canWrite()) {
			throw new RuntimeException("file " + file.getAbsolutePath()
					+ " donot have write permission");
		}
	}

}
