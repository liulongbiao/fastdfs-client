/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request.storage;

import cn.strong.fastdfs.client.CommandCodes;
import cn.strong.fastdfs.client.protocol.request.AbstractStoragePathRequest;
import cn.strong.fastdfs.model.StoragePath;

/**
 * 获取文件属性请求
 * 
 * @author liulongbiao
 *
 */
public class GetMetadataRequest extends AbstractStoragePathRequest {

	public GetMetadataRequest(StoragePath spath) {
		super(spath);
	}

	@Override
	public byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_GET_METADATA;
	}

}