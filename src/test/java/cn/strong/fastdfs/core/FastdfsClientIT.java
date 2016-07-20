/**
 * 
 */
package cn.strong.fastdfs.core;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import rx.Observable;
import rx.subjects.ReplaySubject;

import cn.strong.fastdfs.client.FastdfsTemplate;
import cn.strong.fastdfs.client.Settings;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.utils.IOUtils;
import cn.strong.fastdfs.utils.Seed;

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
		IOUtils.closeQuietly(template);
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
				.fromFullPath("group1/M00/04/02/wKgURFT9aMOARe1WALF9eCfe4O8163.mp3");
		CountDownLatch latch = new CountDownLatch(1);
		Observable<ByteBuf> content = client.download(spath);
		Observable.using(() -> {
			try {
				File file = File.createTempFile("test", "mp3");
				System.out.println("file: " + file.getAbsolutePath());
				return FileChannel.open(file.toPath(), StandardOpenOption.WRITE);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}, ch -> {
			ReplaySubject<Integer> subject = ReplaySubject.create();
			content.subscribe(buf -> {
				try {
					int length = buf.readableBytes();
					subject.onNext(length);
					buf.readBytes(ch, length);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}, ex -> {
				subject.onError(ex);
			}, () -> {
				subject.onCompleted();
			});
			return subject;
		}, ch -> {
			IOUtils.closeQuietly(ch);
		}).doAfterTerminate(latch::countDown).subscribe(len -> {
			System.out.println("received: " + len);
		}, ex -> {
			ex.printStackTrace();
		}, () -> {
			System.out.println("completed");
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testDownload2() throws InterruptedException {
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/04/02/wKgURFT9aMOARe1WALF9eCfe4O8163.mp3");
		File file = new File("D:\\tmp\\test.mp3");
		CountDownLatch latch = new CountDownLatch(1);
		IOUtils.write(client.download(spath), file).doAfterTerminate(latch::countDown)
				.subscribe(len -> {
					System.out.println("received: " + len);
				}, ex -> {
					ex.printStackTrace();
				}, () -> {
					System.out.println("completed");
				});
		latch.await();
		System.out.println("file size: " + file.length());
	}
}
