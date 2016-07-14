package cn.strong.fastdfs.client.protocol.response;

import cn.strong.fastdfs.ex.FastdfsException;
import cn.strong.fastdfs.model.StorageServerInfo;
import io.netty.buffer.ByteBuf;
import rx.Observable;
import rx.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;

import static cn.strong.fastdfs.client.Consts.*;
import static cn.strong.fastdfs.utils.Utils.readString;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 存储服务器信息列表响应接受器
 *
 * Created by liulongbiao on 16-7-14.
 */
public class StorageServerInfoListReceiver implements Receiver {

    private PublishSubject<List<StorageServerInfo>> subject = PublishSubject.create();
    private int length;

    @Override
    public void setLength(long length) {
        this.length = (int) length;
    }

    @Override
    public boolean tryRead(ByteBuf in) {
        if (length < FDFS_STORAGE_LEN) {
            throw new FastdfsException("body length : " + length
                    + " is less than required length " + FDFS_STORAGE_LEN);
        }
        if ((length - FDFS_STORAGE_LEN) % FDFS_HOST_LEN != 0) {
            throw new FastdfsException("body length : " + length
                    + " is invalidate. ");
        }

        int count = (length - FDFS_STORAGE_LEN) / FDFS_HOST_LEN + 1;
        List<StorageServerInfo> result = new ArrayList<>(count);

        String group = readString(in, FDFS_GROUP_LEN, UTF_8);
        String mainHost = readString(in, FDFS_HOST_LEN, UTF_8);
        int port = (int) in.readLong();
        result.add(new StorageServerInfo(group, mainHost, port));

        for (int i = 1; i < count; i++) {
            String host = readString(in, FDFS_HOST_LEN, UTF_8);
            result.add(new StorageServerInfo(group, host, port));
        }

        subject.onNext(result);
        subject.onCompleted();
        return true;
    }

    public Observable<List<StorageServerInfo>> toObservable() {
        return subject;
    }
}
