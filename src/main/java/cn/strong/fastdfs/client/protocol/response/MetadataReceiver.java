/**
 * 
 */
package cn.strong.fastdfs.client.protocol.response;

import static cn.strong.fastdfs.client.Consts.FDFS_FIELD_SEPERATOR;
import static cn.strong.fastdfs.client.Consts.FDFS_RECORD_SEPERATOR;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import rx.Observable;
import rx.subjects.PublishSubject;

import cn.strong.fastdfs.model.Metadata;
import cn.strong.fastdfs.utils.Utils;

/**
 * 元数据响应接收器
 * 
 * @author liulongbiao
 *
 */
public class MetadataReceiver implements Receiver {

	private PublishSubject<Metadata> subject = PublishSubject.create();
	private int length;

	@Override
	public void setLength(long length) {
		this.length = (int) length;
	}

	@Override
	public boolean tryRead(ByteBuf in, Charset charset) {
		Metadata result = new Metadata();
		String content = Utils.readString(in, length, charset);
		String[] pairs = content.split(FDFS_RECORD_SEPERATOR);
		for (String pair : pairs) {
			String[] kv = pair.split(FDFS_FIELD_SEPERATOR, 2);
			if (kv.length == 2) {
				result.append(kv[0], kv[1]);
			}
		}
		subject.onNext(result);
		subject.onCompleted();
		return true;
	}

	public Observable<Metadata> toObservable() {
		return subject;
	}

}
