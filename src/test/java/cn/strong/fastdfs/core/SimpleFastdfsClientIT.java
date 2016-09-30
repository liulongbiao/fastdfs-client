package cn.strong.fastdfs.core;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.strong.fastdfs.client.FastdfsTemplate;
import cn.strong.fastdfs.client.Settings;
import cn.strong.fastdfs.utils.IOUtils;
import cn.strong.fastdfs.utils.TrackerAddress;

public class SimpleFastdfsClientIT {

	private FastdfsTemplate template;
	private SimpleFastdfsClient client;

	@Before
	public void setup() {
		template = new FastdfsTemplate(new Settings());
		FastdfsClient delegate = new FastdfsClient(template, TrackerAddress.createSeed("192.168.20.68:22122"));
		client = new SimpleFastdfsClient(delegate);
	}

	@After
	public void destroy() {
		IOUtils.closeQuietly(template);
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
		String path = "group1/M00/00/D9/wKgURFfki12AZURCAAAADSghvvI057.txt";
		byte[] bytes = client.download(path);
		String data = new String(bytes, UTF_8);
		System.out.println("downloaded: " + data);
	}

	@Test
	@Ignore
	public void testDownloadToFile() {
		String path = "group1/M00/00/D9/wKgURFfkhzaANK7AALF9eC6mayQ365.mp3";
		File file = new File("D:\\tmp\\test1.mp3");
		client.download(path, file, (p, t) -> {
			System.out.println("progress: " + p + "/" + t);
		});
	}
}
