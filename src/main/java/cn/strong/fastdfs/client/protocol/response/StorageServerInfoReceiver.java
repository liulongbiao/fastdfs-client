package cn.strong.fastdfs.client.protocol.response;

import static cn.strong.fastdfs.client.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.client.Consts.FDFS_HOST_LEN;
import static cn.strong.fastdfs.client.Consts.FDFS_STORAGE_STORE_LEN;
import static cn.strong.fastdfs.utils.Utils.readString;

import java.nio.charset.Charset;

import cn.strong.fastdfs.model.StorageServerInfo;
import io.netty.buffer.ByteBuf;

/**
 * 存储服务器信息响应接受器
 *
 * Created by liulongbiao on 16-7-14.
 */
public class StorageServerInfoReceiver extends AbstractReceiver<StorageServerInfo> {

    @Override
    public long expectLength() {
        return FDFS_STORAGE_STORE_LEN;
    }

    @Override
	public boolean tryRead(ByteBuf in, Charset charset) {
		String group = readString(in, FDFS_GROUP_LEN, charset);
		String host = readString(in, FDFS_HOST_LEN, charset);
        int port = (int) in.readLong();
        byte idx = in.readByte();
		promise.complete(new StorageServerInfo(group, host, port, idx));
        return true;
    }
}
