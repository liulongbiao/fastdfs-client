/**
 * 
 */
package cn.strong.fastdfs.core;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.strong.fastdfs.client.FastdfsTemplate;
import cn.strong.fastdfs.client.Settings;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.sink.FileSink;
import cn.strong.fastdfs.utils.IOUtils;
import cn.strong.fastdfs.utils.TrackerAddress;
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
		client = new FastdfsClient(template, TrackerAddress.createSeed("192.168.20.68:22122"));
	}

	@After
	public void destroy() {
		IOUtils.closeQuietly(template);
	}

	@Test
	@Ignore
	public void testUpload() throws InterruptedException {
		byte[] bytes = "Hello Fastdfs".getBytes(CharsetUtil.UTF_8);
		CountDownLatch latch = new CountDownLatch(1);
		client.upload(bytes, bytes.length, "txt", null).whenComplete((data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println("path: " + data);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testDownload() throws Exception {
		StoragePath spath = StoragePath
				.valueOf("group1/M00/00/D9/wKgURFfkhdqAPTSXAAAADSghvvI438.txt");

		File file = File.createTempFile("test", ".inf");
		System.out.println(file.getAbsolutePath());
		try (FileSink sink = new FileSink(file)) {
			CountDownLatch latch = new CountDownLatch(1);
			client.download(spath, sink, (p, t) -> {
				System.out.println("progress: " + p + "/" + t);
			}).whenComplete((data, ex) -> {
				if (ex != null) {
					ex.printStackTrace();
				} else {
					System.out.println("completed");
				}
				latch.countDown();
			});
			latch.await();
		}
	}

	@Test
	@Ignore
	public void testDownload2() throws Exception {
		StoragePath spath = StoragePath
				.valueOf("group1/M00/00/D9/wKgURFfkhzaANK7AALF9eC6mayQ365.mp3");
		File file = new File("D:\\tmp\\test.mp3");
		try (FileSink sink = new FileSink(file)) {
			CountDownLatch latch = new CountDownLatch(1);
			client.download(spath, sink, (p, t) -> {
				System.out.println("progress: " + p + "/" + t);
			}).whenComplete((data, ex) -> {
				if (ex != null) {
					ex.printStackTrace();
				} else {
					System.out.println("completed");
				}
				latch.countDown();
			});
			latch.await();
		}
		System.out.println("file size: " + file.length());
	}
}
