package cn.strong.fastdfs.client.protocol.response;

import static java.nio.charset.StandardCharsets.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

import org.junit.Ignore;
import org.junit.Test;

public class RxReceiverTest {

	@Test
	@Ignore
	public void test() {
		Charset charset = UTF_8;
		RxReceiver receiver = new RxReceiver();
		receiver.observable().subscribe(data -> {
			System.out.println("received: " + data.toString(charset));
		}, ex -> {
			ex.printStackTrace();
		}, () -> {
			System.out.println("completed");
		});
		ByteBuf buf = Unpooled.buffer();
		byte[] bytes1 = "aaaa".getBytes(charset);
		byte[] bytes2 = "bbb".getBytes(charset);

		buf.writeBytes(bytes1);
		receiver.setLength(bytes1.length + bytes2.length);
		receiver.tryRead(buf, charset);
		buf.writeBytes(bytes2);
		receiver.tryRead(buf, charset);
	}

	@Test
	public void testEmpty() {
		Charset charset = UTF_8;
		RxReceiver receiver = new RxReceiver();
		receiver.observable().subscribe(data -> {
			System.out.println("received: " + data.toString(charset));
		}, ex -> {
			ex.printStackTrace();
		}, () -> {
			System.out.println("completed");
		});
		ByteBuf buf = Unpooled.EMPTY_BUFFER;
		receiver.setLength(0);
		receiver.tryRead(buf, charset);
	}

}
