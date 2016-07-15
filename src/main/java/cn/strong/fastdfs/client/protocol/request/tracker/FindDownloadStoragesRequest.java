/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request.tracker;

import static cn.strong.fastdfs.client.CommandCodes.TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ALL;

import cn.strong.fastdfs.client.protocol.request.AbstractStoragePathRequest;
import cn.strong.fastdfs.model.StoragePath;

/**
 * 获取可下载的存储服务器列表
 * 
 * @author liulongbiao
 *
 */
public class FindDownloadStoragesRequest extends AbstractStoragePathRequest {

	public FindDownloadStoragesRequest(StoragePath spath) {
		super(spath);
	}

	@Override
	protected byte cmd() {
		return TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ALL;
	}

}
