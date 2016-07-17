/**
 * 
 */
package cn.strong.fastdfs.core;

import static cn.strong.fastdfs.model.StoragePath.fromFullPath;

import java.io.ByteArrayOutputStream;
import java.io.File;

import rx.Observable;

import cn.strong.fastdfs.client.Consts;
import cn.strong.fastdfs.ex.FastdfsException;
import cn.strong.fastdfs.model.Metadata;
import cn.strong.fastdfs.model.StoragePath;

/**
 * 简单 FastDFS 客户端
 * <ul>
 * <li>使用同步方法，降低未学过 RxJava 的同学的学习曲线</li>
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

	private static <T> T awaitSingle(Observable<T> observable) {
		try {
			return observable.toBlocking().single();
		} catch (Exception e) {
			throw new FastdfsException("get single result from observable error", e);
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
		return awaitSingle(delegate.upload(bytes, bytes.length, ext, group).map(
				StoragePath::getFullPath));
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
		return awaitSingle(delegate.upload(file, group).map(StoragePath::getFullPath));
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
		return awaitSingle(delegate.uploadAppender(bytes, bytes.length, ext, group).map(
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
		return awaitSingle(delegate.uploadAppender(file, group).map(StoragePath::getFullPath));
	}

	/**
	 * 下载文件
	 * 
	 * @param path
	 *            服务器存储路径
	 * @return 文件内容
	 */
	public byte[] download(String path) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		delegate.download(fromFullPath(path)).toBlocking().forEach(buf -> {
			try {
				buf.readBytes(output, buf.readableBytes());
			} catch (Exception e) {
				throw new FastdfsException("read downloaded bytes error", e);
			}
		});
		return output.toByteArray();
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            服务器存储路径
	 */
	public void delete(String path) {
		StoragePath spath = fromFullPath(path);
		awaitSingle(delegate.delete(spath));
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
		StoragePath spath = fromFullPath(path);
		awaitSingle(delegate.append(spath, bytes));
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
		StoragePath spath = fromFullPath(path);
		awaitSingle(delegate.modify(spath, offset, bytes));
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
		StoragePath spath = fromFullPath(path);
		awaitSingle(delegate.truncate(spath, truncatedSize));
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
		StoragePath spath = fromFullPath(path);
		awaitSingle(delegate.setMetadata(spath, metadata, flag));
	}

	/**
	 * 获取文件元数据
	 * 
	 * @param path
	 * @return
	 */
	public Metadata getMetadata(String path) {
		StoragePath spath = fromFullPath(path);
		return awaitSingle(delegate.getMetadata(spath));
	}
}
