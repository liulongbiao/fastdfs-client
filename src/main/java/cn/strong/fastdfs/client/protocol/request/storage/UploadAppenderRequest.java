/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request.storage;

import java.io.File;

import cn.strong.fastdfs.client.CommandCodes;

/**
 * 上传追加文件请求
 * 
 * @author liulongbiao
 *
 */
public class UploadAppenderRequest extends UploadRequest {

	public UploadAppenderRequest(File file, byte storePathIndex) {
		super(file, storePathIndex);
	}

	public UploadAppenderRequest(Object content, long length, String ext, byte storePathIndex) {
		super(content, length, ext, storePathIndex);
	}

	@Override
	protected byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE;
	}

}
