package cn.strong.fastdfs.core;

import java.io.IOException;
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
import cn.strong.fastdfs.utils.IOUtils;
import cn.strong.fastdfs.utils.Seed;

public class TrackerClientIT {

	private FastdfsTemplate template;
	private TrackerClient client;

	@Before
	public void setup() {
		template = new FastdfsTemplate(new Settings());
		List<InetSocketAddress> hosts = Arrays
				.asList(new InetSocketAddress("192.168.20.68", 22122));
		Seed<InetSocketAddress> seed = Seed.create(hosts, Seed.PICK_ROUND_ROBIN);
		client = new TrackerClient(template, seed);
	}

	@After
	public void destroy() {
		IOUtils.closeQuietly(template);
	}

	@Test
	@Ignore
	public void testGetUploadStorageString() throws InterruptedException, IOException {
		CountDownLatch latch = new CountDownLatch(1);
		client.getUploadStorage("group1").whenComplete((data, ex) -> {
			if(ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(data);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testGetDownloadStorage() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		StoragePath spath = StoragePath
				.valueOf("group1/M00/09/FE/wKgURFbQBVSAcFjdAAAADTVhaBw216.inf");
		client.getDownloadStorage(spath).whenComplete((data, ex) -> {
			if(ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(data);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testFindDownloadStorages() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		StoragePath spath = StoragePath
				.valueOf("group1/M00/09/FE/wKgURFbQBVSAcFjdAAAADTVhaBw216.inf");
		client.findDownloadStorages(spath).whenComplete((list, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(list.size());
				list.forEach(System.out::println);
			}
			latch.countDown();
		});
		latch.await();
	}

}
