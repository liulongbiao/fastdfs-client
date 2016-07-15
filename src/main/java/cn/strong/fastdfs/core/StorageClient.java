/**
 * 
 */
package cn.strong.fastdfs.core;

import static cn.strong.fastdfs.client.protocol.request.storage.DownloadRequest.DEFAULT_OFFSET;
import static cn.strong.fastdfs.client.protocol.request.storage.DownloadRequest.SIZE_UNLIMIT;
import io.netty.buffer.ByteBuf;

import java.io.File;
import java.util.Objects;

import rx.Observable;

import cn.strong.fastdfs.client.FastdfsTemplate;
import cn.strong.fastdfs.client.protocol.request.storage.AppendRequest;
import cn.strong.fastdfs.client.protocol.request.storage.DeleteRequest;
import cn.strong.fastdfs.client.protocol.request.storage.DownloadRequest;
import cn.strong.fastdfs.client.protocol.request.storage.GetMetadataRequest;
import cn.strong.fastdfs.client.protocol.request.storage.ModifyRequest;
import cn.strong.fastdfs.client.protocol.request.storage.SetMetadataRequest;
import cn.strong.fastdfs.client.protocol.request.storage.TruncateRequest;
import cn.strong.fastdfs.client.protocol.request.storage.UploadAppenderRequest;
import cn.strong.fastdfs.client.protocol.request.storage.UploadRequest;
import cn.strong.fastdfs.client.protocol.response.EmptyReceiver;
import cn.strong.fastdfs.client.protocol.response.MetadataReceiver;
import cn.strong.fastdfs.client.protocol.response.RxReceiver;
import cn.strong.fastdfs.client.protocol.response.StoragePathReceiver;
import cn.strong.fastdfs.model.Metadata;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.model.StorageServerInfo;

/**
 * StorageClient
 * 
 * @author liulongbiao
 *
 */
public class StorageClient {
	private FastdfsTemplate template;

	public StorageClient(FastdfsTemplate template) {
		this.template = Objects.requireNonNull(template, "storage template is null");
	}

	/**
	 * 上传文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param file
	 *            文件
	 * @return
	 */
	public Observable<StoragePath> upload(StorageServerInfo storage, File file) {
		return template.execute(storage.getAddress(),
				new UploadRequest(file,	storage.storePathIndex), 
				new StoragePathReceiver());
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
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param content
	 *            上传内容
	 * @param size
	 *            内容长度
	 * @param ext
	 *            扩展名
	 */
	public Observable<StoragePath> upload(StorageServerInfo storage, Object content, long size,
			String ext) {
		return template.execute(storage.getAddress(),
				new UploadRequest(content, size, ext, storage.storePathIndex),
				new StoragePathReceiver());
	}

	/**
	 * 上传可追加文件内容
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param file
	 *            文件
	 */
	public Observable<StoragePath> uploadAppender(StorageServerInfo storage, File file) {
		return template.execute(storage.getAddress(),
				new UploadAppenderRequest(file, storage.storePathIndex),
				new StoragePathReceiver());
	}

	/**
	 * 上传可追加文件内容，其中文件内容字段 content 的支持以下类型：
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
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param content
	 *            上传内容
	 * @param size
	 *            内容长度
	 * @param ext
	 *            扩展名
	 */
	public Observable<StoragePath> uploadAppender(StorageServerInfo storage, Object content,
			long size, String ext) {
		return template.execute(storage.getAddress(),
				new UploadAppenderRequest(content, size, ext, storage.storePathIndex),
				new StoragePathReceiver());
	}

	/**
	 * 追加文件内容
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 * @param bytes
	 *            内容字节数组
	 * @return
	 */
	public Observable<Void> append(StorageServerInfo storage, StoragePath spath, byte[] bytes) {
		return template.execute(storage.getAddress(),
				new AppendRequest(bytes, bytes.length, spath),
				new EmptyReceiver());
	}

	/**
	 * 修改文件内容
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 * @param offset
	 *            偏移量
	 * @param bytes
	 *            内容字节数组
	 * @return
	 */
	public Observable<Void> modify(StorageServerInfo storage, StoragePath spath, int offset,
			byte[] bytes) {
		return template.execute(storage.getAddress(), 
				new ModifyRequest(bytes, bytes.length, spath, offset),
				new EmptyReceiver());
	}

	/**
	 * 删除文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 */
	public Observable<Void> delete(StorageServerInfo storage, StoragePath spath) {
		return template.execute(storage.getAddress(), new DeleteRequest(spath),
				new EmptyReceiver());
	}

	/**
	 * 截取文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 */
	public Observable<Void> truncate(StorageServerInfo storage, StoragePath spath) {
		return truncate(storage, spath, 0);
	}

	/**
	 * 截取文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 * @param truncatedSize
	 *            截取文件大小
	 */
	public Observable<Void> truncate(StorageServerInfo storage, StoragePath spath, int truncatedSize) {
		return template.execute(storage.getAddress(), new TruncateRequest(spath, truncatedSize),
				new EmptyReceiver());
	}
	
	/**
	 * 下载文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 * @return
	 */
	public Observable<ByteBuf> download(StorageServerInfo storage, StoragePath spath) {
		return download(storage, spath, DEFAULT_OFFSET, SIZE_UNLIMIT);
	}

	/**
	 * 下载文件内容
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 * @param offset
	 *            字节偏移量
	 * @param size
	 *            下载字节数
	 * @return
	 */
	public Observable<ByteBuf> download(StorageServerInfo storage, StoragePath spath, int offset, int size) {
		return template.execute(storage.getAddress(), new DownloadRequest(spath, offset, size),
				new RxReceiver());
	}

	/**
	 * 设置文件元数据
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 * @param metadata
	 *            元数据
	 * @param flag
	 *            设置标识
	 * @return
	 */
	public Observable<Void> setMetadata(StorageServerInfo storage, StoragePath spath,
			Metadata metadata, byte flag) {
		return template.execute(storage.getAddress(),
				new SetMetadataRequest(spath, metadata, flag),
				new EmptyReceiver());
	}

	/**
	 * 获取文件元数据
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param path
	 *            服务器存储路径
	 * @return
	 */
	public Observable<Metadata> getMetadata(StorageServerInfo storage, StoragePath path) {
		return template.execute(storage.getAddress(), new GetMetadataRequest(path),
				new MetadataReceiver());
	}
}
