package cn.strong.fastdfs.client.protocol.response;

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
		receiver.toObservable().subscribe(Actions.empty(), ex -> {
			ex.printStackTrace();
		}, () -> {
			System.out.println("received");
		});
		ByteBuf buf = Unpooled.buffer(0);
		receiver.tryRead(buf);
	}

}
