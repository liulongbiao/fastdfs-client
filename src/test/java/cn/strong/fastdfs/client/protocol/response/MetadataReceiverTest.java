package cn.strong.fastdfs.client.protocol.response;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.charset.Charset;

import org.junit.Test;

import cn.strong.fastdfs.client.Consts;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class MetadataReceiverTest {

	@Test
	public void test() {
		Charset charset = Charset.isSupported("GBK") ? Charset.forName("GBK") : UTF_8;
		System.out.println("charset: " + charset.name());
		MetadataReceiver receiver = new MetadataReceiver();
		receiver.promise().whenComplete((data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println("received: ");
				data.toMap().forEach((k, v) -> {
					System.out.println(k + " : " + v);
				});
			}
		});
		ByteBuf buf = Unpooled.buffer();
		StringBuilder sb = new StringBuilder();
		sb.append("键1");
		sb.append(Consts.FDFS_FIELD_SEPERATOR);
		sb.append("值1");
		sb.append(Consts.FDFS_RECORD_SEPERATOR);
		sb.append("key2");
		sb.append(Consts.FDFS_FIELD_SEPERATOR);
		sb.append("value1");
		byte[] bytes = sb.toString().getBytes(charset);
		buf.writeBytes(bytes);
		
		receiver.setLength(bytes.length);
		receiver.tryRead(buf, charset);
	}

}
