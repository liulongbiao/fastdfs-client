package cn.strong.fastdfs.client.protocol.response;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class RxReceiverTest {

	@Test
	public void test() {
		RxReceiver receiver = new RxReceiver();
		receiver.toObservable().subscribe(data -> {
			System.out.println("received: " + data.toString(StandardCharsets.UTF_8));
		}, ex -> {
			ex.printStackTrace();
		}, () -> {
			System.out.println("completed");
		});
		ByteBuf buf = Unpooled.buffer();
		byte[] bytes1 = "aaaa".getBytes();
		byte[] bytes2 = "bbb".getBytes();

		buf.writeBytes(bytes1);
		receiver.setLength(bytes1.length + bytes2.length);
		receiver.tryRead(buf);
		buf.writeBytes(bytes2);
		receiver.tryRead(buf);
	}

}
