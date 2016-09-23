package cn.strong.fastdfs.client.protocol.response;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Ignore;
import org.junit.Test;

import cn.strong.fastdfs.sink.ByteArraySink;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class SinkReceiverTest {

	@Test
	@Ignore
	public void test() {
		try (ByteArraySink sink = new ByteArraySink()) {
			Charset charset = UTF_8;
			SinkReceiver receiver = new SinkReceiver(sink, (p, t) -> {
				System.out.println("progress: " + p + "/" + t);
			});
			receiver.promise().whenComplete((data, ex) -> {
				if (ex != null) {
					ex.printStackTrace();
				} else {
					System.out.println(new String(sink.getBytes(), charset));
					System.out.println("completed");
				}
			});

			ByteBuf buf = Unpooled.buffer();
			byte[] bytes1 = "aaaa".getBytes(charset);
			byte[] bytes2 = "bbb".getBytes(charset);

			buf.writeBytes(bytes1);
			receiver.setLength(bytes1.length + bytes2.length);
			receiver.tryRead(buf, charset);
			buf.writeBytes(bytes2);
			receiver.tryRead(buf, charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void testEmpty() {
		try (ByteArraySink sink = new ByteArraySink()) {
			Charset charset = UTF_8;
			SinkReceiver receiver = new SinkReceiver(sink, (p, t) -> {
				System.out.println("progress: " + p + "/" + t);
			});
			receiver.promise().whenComplete((data, ex) -> {
				if (ex != null) {
					ex.printStackTrace();
				} else {
					System.out.println(new String(sink.getBytes(), charset));
					System.out.println("completed");
				}
			});
			ByteBuf buf = Unpooled.EMPTY_BUFFER;
			receiver.setLength(0);
			receiver.tryRead(buf, charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
