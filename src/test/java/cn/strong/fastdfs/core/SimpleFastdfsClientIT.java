package cn.strong.fastdfs.core;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.strong.fastdfs.client.FastdfsTemplate;
import cn.strong.fastdfs.client.Settings;
import cn.strong.fastdfs.client.protocol.response.SimpleProgressListener;
import cn.strong.fastdfs.utils.RxIOUtils;
import cn.strong.fastdfs.utils.Seed;

public class SimpleFastdfsClientIT {

	private FastdfsTemplate template;
	private SimpleFastdfsClient client;

	@Before
	public void setup() {
		template = new FastdfsTemplate(new Settings());
		List<InetSocketAddress> hosts = Arrays
				.asList(new InetSocketAddress("192.168.20.68", 22122));
		Seed<InetSocketAddress> seed = Seed.create(hosts, Seed.PICK_ROUND_ROBIN);
		FastdfsClient delegate = new FastdfsClient(template, seed);
		client = new SimpleFastdfsClient(delegate);
	}

	@After
	public void destroy() {
		RxIOUtils.closeQuietly(template);
	}

	@Test
	@Ignore
	public void testUploadByteArrayString() {
		byte[] bytes = "Hello Fastdfs".getBytes(UTF_8);
		String path = client.upload(bytes, "txt");
		System.out.println("path: " + path);
	}

	@Test
	@Ignore
	public void testDownload() {
		String path = "group1/M00/15/93/wKgURFfGi4-AeRRwAAAADSghvvI390.txt";
		byte[] bytes = client.download(path);
		String data = new String(bytes, UTF_8);
		System.out.println("downloaded: " + data);
	}

	@Test
	@Ignore
	public void testDownloadToFile() {
		String path = "group1/M00/15/93/wKgURFfGixqAfTr5AF65c7T1o9k418.zip";
		File file = new File("D:\\tmp\\test.mp3");
		client.download(path, file, new SimpleProgressListener());
	}
}
