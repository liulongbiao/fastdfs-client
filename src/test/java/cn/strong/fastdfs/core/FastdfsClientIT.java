/**
 * 
 */
package cn.strong.fastdfs.core;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.strong.fastdfs.client.FastdfsTemplate;
import cn.strong.fastdfs.client.Settings;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.sink.FileSink;
import cn.strong.fastdfs.utils.RxIOUtils;
import cn.strong.fastdfs.utils.Seed;
import io.netty.util.CharsetUtil;

/**
 * @author liulongbiao
 *
 */
public class FastdfsClientIT {

	private FastdfsTemplate template;
	private FastdfsClient client;

	@Before
	public void setup() {
		template = new FastdfsTemplate(new Settings());
		List<InetSocketAddress> hosts = Arrays
				.asList(new InetSocketAddress("192.168.20.68", 22122));
		Seed<InetSocketAddress> seed = Seed.create(hosts, Seed.PICK_ROUND_ROBIN);
		client = new FastdfsClient(template, seed);
	}

	@After
	public void destroy() {
		RxIOUtils.closeQuietly(template);
	}

	@Test
	@Ignore
	public void testUpload() throws InterruptedException {
		byte[] bytes = "Hello Fastdfs".getBytes(CharsetUtil.UTF_8);
		CountDownLatch latch = new CountDownLatch(1);
		client.upload(bytes, bytes.length, "txt", null)
				.subscribe(path -> {
					System.out.println("path: " + path);
				}, ex -> {
					ex.printStackTrace();
					latch.countDown();
				}, () -> {
					System.out.println("completed");
					latch.countDown();
				});
		latch.await();
	}

	@Test
	@Ignore
	public void testDownload() throws Exception {
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/15/92/wKgURFfGh0eAMEisAAAADTVhaBw940.inf");

		File file = File.createTempFile("test", ".inf");
		System.out.println(file.getAbsolutePath());
		try (FileSink sink = new FileSink(file)) {
			CountDownLatch latch = new CountDownLatch(1);
			client.download(spath, sink, null).doAfterTerminate(latch::countDown).subscribe(len -> {
				System.out.println("received: " + len);
			}, ex -> {
				ex.printStackTrace();
			}, () -> {
				System.out.println("completed");
			});
			latch.await();
		}
	}

	@Test
	@Ignore
	public void testDownload2() throws Exception {
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/04/02/wKgURFT9aMOARe1WALF9eCfe4O8163.mp3");
		File file = new File("D:\\tmp\\test.mp3");
		try (FileSink sink = new FileSink(file)) {
			CountDownLatch latch = new CountDownLatch(1);
			client.download(spath, sink, null).doAfterTerminate(latch::countDown).subscribe(len -> {
				System.out.println("received: " + len);
			}, ex -> {
				ex.printStackTrace();
			}, () -> {
				System.out.println("completed");
			});
			latch.await();
		}
		System.out.println("file size: " + file.length());
	}
}
