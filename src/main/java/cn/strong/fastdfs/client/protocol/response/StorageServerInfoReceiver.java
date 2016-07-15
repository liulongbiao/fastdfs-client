package cn.strong.fastdfs.client.protocol.response;

import static cn.strong.fastdfs.client.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.client.Consts.FDFS_HOST_LEN;
import static cn.strong.fastdfs.client.Consts.FDFS_STORAGE_STORE_LEN;
import static cn.strong.fastdfs.utils.Utils.readString;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import rx.Observable;
import rx.subjects.PublishSubject;

import cn.strong.fastdfs.model.StorageServerInfo;

/**
 * 存储服务器信息响应接受器
 *
 * Created by liulongbiao on 16-7-14.
 */
public class StorageServerInfoReceiver implements Receiver<StorageServerInfo> {

    private PublishSubject<StorageServerInfo> subject = PublishSubject.create();

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
        subject.onNext(new StorageServerInfo(group, host, port, idx));
        subject.onCompleted();
        return true;
    }

	@Override
	public Observable<StorageServerInfo> observable() {
        return subject;
    }
}
