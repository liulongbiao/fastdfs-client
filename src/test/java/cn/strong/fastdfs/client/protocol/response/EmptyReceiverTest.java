package cn.strong.fastdfs.client.protocol.response;

import static java.nio.charset.StandardCharsets.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.junit.Ignore;
import org.junit.Test;

import rx.functions.Actions;

public class EmptyReceiverTest {

	@Test
	@Ignore
	public void test() {
		EmptyReceiver receiver = new EmptyReceiver();
		receiver.observable().subscribe(Actions.empty(), ex -> {
			ex.printStackTrace();
		}, () -> {
			System.out.println("received");
		});
		ByteBuf buf = Unpooled.buffer(0);
		receiver.tryRead(buf, UTF_8);
	}

}
