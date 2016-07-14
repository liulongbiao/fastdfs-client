package cn.strong.fastdfs.client.protocol.response;

import cn.strong.fastdfs.model.StorageServerInfo;
import io.netty.buffer.ByteBuf;
import rx.Observable;
import rx.subjects.PublishSubject;

import static cn.strong.fastdfs.client.Consts.*;
import static cn.strong.fastdfs.utils.Utils.readString;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 存储服务器信息响应接受器
 *
 * Created by liulongbiao on 16-7-14.
 */
public class StorageServerInfoReceiver implements Receiver {

    private PublishSubject<StorageServerInfo> subject = PublishSubject.create();

    @Override
    public long expectLength() {
        return FDFS_STORAGE_STORE_LEN;
    }

    @Override
    public boolean tryRead(ByteBuf in) {
        String group = readString(in, FDFS_GROUP_LEN, UTF_8);
        String host = readString(in, FDFS_HOST_LEN, UTF_8);
        int port = (int) in.readLong();
        byte idx = in.readByte();
        subject.onNext(new StorageServerInfo(group, host, port, idx));
        subject.onCompleted();
        return true;
    }

    public Observable<StorageServerInfo> toObservable() {
        return subject;
    }
}
