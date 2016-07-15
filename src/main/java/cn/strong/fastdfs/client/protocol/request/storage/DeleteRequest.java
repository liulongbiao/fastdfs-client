/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request.storage;

import cn.strong.fastdfs.client.CommandCodes;
import cn.strong.fastdfs.client.protocol.request.AbstractStoragePathRequest;
import cn.strong.fastdfs.model.StoragePath;

/**
 * 删除请求
 * 
 * @author liulongbiao
 *
 */
public class DeleteRequest extends AbstractStoragePathRequest {

	public DeleteRequest(StoragePath spath) {
		super(spath);
	}

	@Override
	protected byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_DELETE_FILE;
	}

}
