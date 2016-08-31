package cn.strong.fastdfs.client.protocol.response;

import java.math.BigDecimal;

import cn.strong.fastdfs.sink.SinkProgressListener;

/**
 * 简单进度监听
 * 
 * @author liulongbiao
 *
 */
public class SimpleProgressListener implements SinkProgressListener {
	double total;

	@Override
	public void onInitLength(long length) {
		System.out.println("init total: " + length);
		this.total = length;
	}

	@Override
	public void onProgress(long progress) {
		if (total > 0) {
			System.out.println("progress: " + BigDecimal.valueOf((progress / total * 100)) + "%");
		}
	}
}