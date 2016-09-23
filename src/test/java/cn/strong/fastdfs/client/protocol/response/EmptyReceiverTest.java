package cn.strong.fastdfs.client.protocol.response;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.junit.Ignore;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class EmptyReceiverTest {

	@Test
	@Ignore
	public void test() {
		EmptyReceiver receiver = new EmptyReceiver();
		receiver.promise().whenComplete((v, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println("received");
			}
		});
		ByteBuf buf = Unpooled.buffer(0);
		receiver.tryRead(buf, UTF_8);
	}

}
