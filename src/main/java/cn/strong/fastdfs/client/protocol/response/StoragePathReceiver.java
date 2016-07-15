package cn.strong.fastdfs.client.protocol.response;

import static cn.strong.fastdfs.client.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.utils.Utils.readString;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import rx.Observable;
import rx.subjects.PublishSubject;

import cn.strong.fastdfs.ex.FastdfsException;
import cn.strong.fastdfs.model.StoragePath;

/**
 * 存储路径响应接收器
 *
 * Created by liulongbiao on 16-7-14.
 */
public class StoragePathReceiver implements Receiver<StoragePath> {

    private PublishSubject<StoragePath> subject = PublishSubject.create();
    private int length;

    @Override
    public void setLength(long length) {
        this.length = (int) length;
    }

    @Override
	public boolean tryRead(ByteBuf in, Charset charset) {
        if (length <= FDFS_GROUP_LEN) {
            throw new FastdfsException("body length : " + length
                    + ", is lte required group name length 16.");
        }
		String group = readString(in, FDFS_GROUP_LEN, charset);
		String path = readString(in, length - FDFS_GROUP_LEN, charset);
        subject.onNext(new StoragePath(group, path));
        subject.onCompleted();
        return true;
    }

	@Override
	public Observable<StoragePath> observable() {
        return subject;
    }
}
