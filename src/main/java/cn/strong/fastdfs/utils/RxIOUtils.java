/**
 * 
 */
package cn.strong.fastdfs.utils;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;

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
	 * 将 Observable<ByteBuf> 写到文件中
	 * 
	 * @param content
	 *            内容
	 * @param file
	 *            文件
	 * @return 读取字节数 Observable
	 */
	public static Observable<Integer> write(Observable<ByteBuf> content, File file) {
		return Observable.using(() -> {
			return openFileChannel(file);
		}, fc -> {
			ReplaySubject<Integer> subject = ReplaySubject.create();
			content.observeOn(Schedulers.io(), true).subscribe(buf -> {
				try {
					int length = buf.readableBytes();
					buf.readBytes(fc, length);
					subject.onNext(length);
				} catch (Exception e) {
					subject.onError(e);
				}
			}, ex -> {
				subject.onError(ex);
			}, () -> {
				subject.onCompleted();
			});
			return subject;
		}, fc -> {
			RxIOUtils.closeQuietly(fc);
		});
	}

	private static FileChannel openFileChannel(File file) {
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
	private static void initFileToWrite(File file) {
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

	/**
	 * 将 Observable<ByteBuf> 转换为 byte[]
	 * 
	 * @param content
	 * @return
	 */
	public static Observable<byte[]> toBytes(Observable<ByteBuf> content) {
		return Observable.using(() -> {
			return new ByteArrayOutputStream(1024);
		}, output -> {
			ReplaySubject<byte[]> subject = ReplaySubject.create();
			content.observeOn(Schedulers.io(), true).subscribe(buf -> {
				try {
					buf.readBytes(output, buf.readableBytes());
				} catch (Exception e) {
					subject.onError(e);
				}
			}, ex -> {
				subject.onError(ex);
			}, () -> {
				subject.onNext(output.toByteArray());
				subject.onCompleted();
			});
			return subject;
		}, output -> {
			RxIOUtils.closeQuietly(output);
		});
	}
}
