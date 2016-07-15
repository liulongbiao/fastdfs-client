/**
 * 
 */
package cn.strong.fastdfs.core;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;

import cn.strong.fastdfs.client.FastdfsTemplate;
import cn.strong.fastdfs.client.protocol.request.tracker.FindDownloadStoragesRequest;
import cn.strong.fastdfs.client.protocol.request.tracker.GetDownloadStorageRequest;
import cn.strong.fastdfs.client.protocol.request.tracker.GetUpdateStorageRequest;
import cn.strong.fastdfs.client.protocol.request.tracker.GetUploadStorageRequest;
import cn.strong.fastdfs.client.protocol.response.StorageServerInfoListReceiver;
import cn.strong.fastdfs.client.protocol.response.StorageServerInfoReceiver;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.model.StorageServerInfo;
import cn.strong.fastdfs.utils.Seed;
import cn.strong.fastdfs.utils.Utils;

/**
 * TrackerClient
 * 
 * @author liulongbiao
 *
 */
public class TrackerClient {

	private static Logger LOG = LoggerFactory.getLogger(TrackerClient.class);

	private FastdfsTemplate template;
	private Seed<InetSocketAddress> seed;

	public TrackerClient(FastdfsTemplate template, Seed<InetSocketAddress> seed) {
		this.template = Objects.requireNonNull(template, "tracker template is null");
		this.seed = Objects.requireNonNull(seed, "tracker seed is null");
		LOG.info("TrackerClient inited");
	}

	/**
	 * 获取上传存储服务器地址
	 * 
	 */
	public Observable<StorageServerInfo> getUploadStorage() {
		return getUploadStorage(null);
	}

	/**
	 * 获取上传存储服务器地址
	 * 
	 * @param group
	 */
	public Observable<StorageServerInfo> getUploadStorage(String group) {
		return template.execute(seed.pick(), new GetUploadStorageRequest(group),
				new StorageServerInfoReceiver());
	}

	/**
	 * 获取下载存储服务器地址
	 * 
	 * @param path
	 */
	public Observable<StorageServerInfo> getDownloadStorage(StoragePath path) {
		return template.execute(seed.pick(), new GetDownloadStorageRequest(path),
				new StorageServerInfoListReceiver()).map(Utils::head);
	}

	/**
	 * 获取更新存储服务器地址
	 * 
	 * @param path
	 */
	public Observable<StorageServerInfo> getUpdateStorage(StoragePath path) {
		return template.execute(seed.pick(), new GetUpdateStorageRequest(path),
				new StorageServerInfoListReceiver()).map(Utils::head);
	}

	/**
	 * 请求 tracker 获取所有可用的下载 storage 地址列表
	 * 
	 * @param path
	 * @return
	 */
	public Observable<List<StorageServerInfo>> findDownloadStorages(StoragePath path) {
		return template.execute(seed.pick(), new FindDownloadStoragesRequest(path),
				new StorageServerInfoListReceiver());
	}
}
