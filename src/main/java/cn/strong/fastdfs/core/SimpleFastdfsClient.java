/**
 * 
 */
package cn.strong.fastdfs.core;

import static cn.strong.fastdfs.model.StoragePath.fromFullPath;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import cn.strong.fastdfs.client.Consts;
import cn.strong.fastdfs.client.FastdfsTemplate;
import cn.strong.fastdfs.ex.FastdfsException;
import cn.strong.fastdfs.model.Metadata;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.sink.ByteArraySink;
import cn.strong.fastdfs.sink.FileSink;
import cn.strong.fastdfs.sink.SinkProgressListener;
import cn.strong.fastdfs.utils.Seed;

/**
 * 简单 FastDFS 客户端
 * <ul>
 * <li>使用同步方法，降低未学过 CompletableFuture 的同学的学习曲线</li>
 * <li>存储路径使用全路径</li>
 * </ul>
 * 
 * @author liulongbiao
 *
 */
public class SimpleFastdfsClient {
	final FastdfsClient delegate;

	public SimpleFastdfsClient(FastdfsClient delegate) {
		this.delegate = delegate;
	}

	public SimpleFastdfsClient(FastdfsTemplate template, Seed<InetSocketAddress> seed) {
		this.delegate = new FastdfsClient(template, seed);
	}

	/**
	 * 等待单个结果的 Observable
	 * 
	 * @param observable
	 * @return
	 */
	private static <T> T await(CompletableFuture<T> f) {
		try {
			return f.get();
		} catch (Exception e) {
			throw new FastdfsException("get result from CompletableFuture error", e);
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param bytes
	 *            文件内容
	 * @param ext
	 *            文件扩展名
	 * @return 服务器存储路径
	 */
	public String upload(byte[] bytes, String ext) {
		return upload(bytes, ext, null);
	}

	/**
	 * 上传文件
	 * 
	 * @param bytes
	 *            文件内容
	 * @param ext
	 *            文件扩展名
	 * @param group
	 *            文件分组
	 * @return 服务器存储路径
	 */
	public String upload(byte[] bytes, String ext, String group) {
		return await(delegate.upload(bytes, bytes.length, ext, group).thenApply(StoragePath::getFullPath));
	}

	/**
	 * 上传本地文件
	 * 
	 * @param file
	 *            本地文件
	 * @return 服务器存储路径
	 */
	public String upload(File file) {
		return upload(file, null);
	}

	/**
	 * 上传本地文件
	 * 
	 * @param file
	 *            本地文件
	 * @param group
	 *            文件分组
	 * @return 服务器存储路径
	 */
	public String upload(File file, String group) {
		if (file == null || !file.exists()) {
			throw new FastdfsException("file does not exist.");
		}
		return await(delegate.upload(file, group).thenApply(StoragePath::getFullPath));
	}

	/**
	 * 上传文件，其中文件内容字段 content 的支持以下类型：
	 * 
	 * <ul>
	 * <li>{@link java.io.File}</li>
	 * <li>{@link java.io.InputStream}</li>
	 * <li><code>byte[]</code></li>
	 * <li>{@link java.nio.channels.ReadableByteChannel}</li>
	 * <li>{@link io.netty.channel.FileRegion}</li>
	 * <li>{@link io.netty.handler.stream.ChunkedInput}</li>
	 * <li>{@link io.netty.buffer.ByteBuf}</li>
	 * </ul>
	 * 
	 * @param content
	 *            上传内容
	 * @param size
	 *            内容长度
	 * @param ext
	 *            扩展名
	 * @param group
	 *            分组
	 * @return 服务器存储路径
	 */
	public String upload(Object content, long size, String ext, String group) {
		return await(delegate.upload(content, size, ext, group).thenApply(StoragePath::getFullPath));
	}

	/**
	 * 上传 Appender 文件
	 * 
	 * @param bytes
	 *            文件内容
	 * @param ext
	 *            文件扩展名
	 * @return 服务器存储路径
	 */
	public String uploadAppender(byte[] bytes, String ext) {
		return uploadAppender(bytes, ext, null);
	}

	/**
	 * 上传 Appender 文件
	 * 
	 * @param bytes
	 *            文件内容
	 * @param ext
	 *            文件扩展名
	 * @param group
	 *            文件分组
	 * @return 服务器存储路径
	 */
	public String uploadAppender(byte[] bytes, String ext, String group) {
		return await(delegate.uploadAppender(bytes, bytes.length, ext, group).thenApply(
				StoragePath::getFullPath));
	}

	/**
	 * 上传本地文件为 appender 文件
	 * 
	 * @param file
	 *            本地文件
	 * @return 服务器存储路径
	 */
	public String uploadAppender(File file) {
		return uploadAppender(file, null);
	}

	/**
	 * 上传本地文件为 appender 文件
	 * 
	 * @param file
	 *            本地文件
	 * @param group
	 *            文件分组
	 * @return 服务器存储路径
	 */
	public String uploadAppender(File file, String group) {
		if (file == null || !file.exists()) {
			throw new FastdfsException("file does not exist.");
		}
		return await(delegate.uploadAppender(file, group).thenApply(StoragePath::getFullPath));
	}

	/**
	 * 上传 Appender 文件，其中文件内容字段 content 的支持以下类型：
	 * 
	 * <ul>
	 * <li>{@link java.io.File}</li>
	 * <li>{@link java.io.InputStream}</li>
	 * <li><code>byte[]</code></li>
	 * <li>{@link java.nio.channels.ReadableByteChannel}</li>
	 * <li>{@link io.netty.channel.FileRegion}</li>
	 * <li>{@link io.netty.handler.stream.ChunkedInput}</li>
	 * <li>{@link io.netty.buffer.ByteBuf}</li>
	 * </ul>
	 * 
	 * @param content
	 *            上传内容
	 * @param size
	 *            内容长度
	 * @param ext
	 *            扩展名
	 * @param group
	 *            分组
	 * @return 服务器存储路径
	 */
	public String uploadAppender(Object content, long size, String ext, String group) {
		return await(delegate.upload(content, size, ext, group).thenApply(StoragePath::getFullPath));
	}

	/**
	 * 下载文件
	 * 
	 * @param path
	 *            服务器存储路径
	 * @return 文件内容
	 */
	public byte[] download(String path) {
		return download(path, (SinkProgressListener) null);
	}

	/**
	 * 下载文件
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param listener
	 *            进度监听
	 * @return 文件内容
	 */
	public byte[] download(String path, SinkProgressListener listener) {
		try (ByteArraySink sink = new ByteArraySink()) {
			delegate.download(fromFullPath(path), sink, listener).get();
			return sink.getBytes();
		} catch (FastdfsException e) {
			throw e;
		} catch (Exception ex) {
			throw new FastdfsException("download file to bytes error", ex);
		}
	}

	/**
	 * 下载文件到指定文件中
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param file
	 *            待写入的文件
	 */
	public void download(String path, File file) {
		download(path, file, (SinkProgressListener) null);
	}

	/**
	 * 下载文件到指定文件中
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param file
	 *            待写入的文件
	 * @param listener
	 *            进度监听
	 */
	public void download(String path, File file, SinkProgressListener listener) {
		try (FileSink sink = new FileSink(file)) {
			delegate.download(fromFullPath(path), sink, listener).get();
		} catch (FastdfsException e) {
			throw e;
		} catch (Exception ex) {
			throw new FastdfsException("download file error", ex);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            服务器存储路径
	 */
	public void delete(String path) {
		await(delegate.delete(fromFullPath(path)));
	}

	/**
	 * 追加文件内容
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param bytes
	 *            追加内容
	 */
	public void append(String path, byte[] bytes) {
		await(delegate.append(fromFullPath(path), bytes));
	}

	/**
	 * 修改文件内容
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param offset
	 *            修改起始偏移量
	 * @param bytes
	 *            修改内容
	 */
	public void modify(String path, int offset, byte[] bytes) {
		await(delegate.modify(fromFullPath(path), offset, bytes));
	}

	/**
	 * 截取文件内容
	 * 
	 * @param path
	 *            服务器存储路径
	 */
	public void truncate(String path) {
		truncate(path, 0);
	}

	/**
	 * 截取文件内容
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param truncatedSize
	 *            截取字节数
	 */
	public void truncate(String path, int truncatedSize) {
		await(delegate.truncate(fromFullPath(path), truncatedSize));
	}

	/**
	 * 设置文件元数据
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param metadata
	 *            元数据
	 */
	public void setMetadata(String path, Metadata metadata) {
		setMetadata(path, metadata, Consts.STORAGE_SET_METADATA_FLAG_OVERWRITE);
	}

	/**
	 * 设置文件元数据
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param metadata
	 *            元数据
	 * @param flag
	 *            设置标识
	 */
	public void setMetadata(String path, Metadata metadata, byte flag) {
		await(delegate.setMetadata(fromFullPath(path), metadata, flag));
	}

	/**
	 * 获取文件元数据
	 * 
	 * @param path
	 * @return
	 */
	public Metadata getMetadata(String path) {
		return await(delegate.getMetadata(fromFullPath(path)));
	}
}
