/**
 * 
 */
package cn.strong.fastdfs.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.strong.fastdfs.client.FastdfsTemplate;
import cn.strong.fastdfs.client.Settings;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.model.StorageServerInfo;
import cn.strong.fastdfs.sink.ByteArraySink;
import cn.strong.fastdfs.utils.IOUtils;

/**
 * @author liulongbiao
 *
 */
public class StorageClientIT {

	private FastdfsTemplate template;
	private StorageClient client;

	@Before
	public void setup() {
		template = new FastdfsTemplate(new Settings());
		client = new StorageClient(template);
	}

	@After
	public void destroy() {
		IOUtils.closeQuietly(template);
	}

	@Test
	@Ignore
	public void testUploadFile() throws InterruptedException, IOException {
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		CountDownLatch latch = new CountDownLatch(1);
		client.upload(info, new File("pom.xml")).whenComplete((data, ex) -> {
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
	public void testUploadInputStream() throws InterruptedException, IOException {
		File file = new File("pom.xml");
		InputStream input = new FileInputStream(file);
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		CountDownLatch latch = new CountDownLatch(1);
		client.upload(info, input, file.length(), "xml").whenComplete((data, ex) -> {
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
	public void testUploadBytes() throws InterruptedException, IOException {
		byte[] bytes = "Hello fastdfs".getBytes();
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		CountDownLatch latch = new CountDownLatch(1);
		client.upload(info, bytes, bytes.length, "inf").whenComplete((data, ex) -> {
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
	public void testUploadAppenderBytes() throws InterruptedException, IOException {
		byte[] bytes = "Hello fastdfs".getBytes();
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		CountDownLatch latch = new CountDownLatch(1);
		client.uploadAppender(info, bytes, bytes.length, "inf").whenComplete((data, ex) -> {
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
	public void testAppenderBytes() throws InterruptedException, IOException {
		byte[] bytes = "\nappend fastdfs".getBytes();
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/0A/97/wKgURFeI0ZyEeorsAAAAADVhaBw380.inf");
		CountDownLatch latch = new CountDownLatch(1);
		client.append(info, spath, bytes).whenComplete((data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println("appended");
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testModify() throws InterruptedException, IOException {
		byte[] bytes = "modify fastdfs".getBytes();
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/0A/96/wKgURFeIsj6AIL3lAAAADTVhaBw169.inf");
		CountDownLatch latch = new CountDownLatch(1);
		client.modify(info, spath, 12, bytes).whenComplete((data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println("modified");
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testDelete() throws InterruptedException, IOException {
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FF/wKgURFbQHjuACGt4AAAADTVhaBw716.inf");
		CountDownLatch latch = new CountDownLatch(1);
		client.delete(info, spath).whenComplete((data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println("deleted");
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testTruncate() throws InterruptedException, IOException {
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/0A/97/wKgURFeI0ZyEeorsAAAAADVhaBw380.inf");
		CountDownLatch latch = new CountDownLatch(1);
		client.truncate(info, spath, 10).whenComplete((data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println("truncated");
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testDownload() throws InterruptedException, IOException {
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/15/92/wKgURFfGh0eAMEisAAAADTVhaBw940.inf");
		CountDownLatch latch = new CountDownLatch(1);
		ByteArraySink sink = new ByteArraySink();
		client.download(info, spath, sink, (p, t) -> {
			System.out.println("progress: " + p + "/" + t);
		}).whenComplete((data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println("data: " + new String(sink.getBytes(), StandardCharsets.UTF_8));
			}
			latch.countDown();
		});
		latch.await();
	}
}
