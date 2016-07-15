package cn.strong.fastdfs.client.handler;

import static cn.strong.fastdfs.client.CommandCodes.FDFS_PROTO_CMD_RESP;
import static java.nio.charset.StandardCharsets.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

import java.nio.charset.Charset;

import org.junit.Test;

import cn.strong.fastdfs.client.Consts;
import cn.strong.fastdfs.client.protocol.response.Receiver;
import cn.strong.fastdfs.utils.Utils;

public class FastdfsDecoderTest {

	@Test
	public void test() {
		String msg = "Hello world!";
		byte[] bytes = msg.getBytes();
		ByteBuf buf = Unpooled.buffer();
		buf.writeLong(bytes.length);
		buf.writeByte(FDFS_PROTO_CMD_RESP);
		buf.writeByte(Consts.ERRNO_OK);
		buf.writeBytes(bytes);

		FastdfsDecoder handler = new FastdfsDecoder();
		EmbeddedChannel channel = new EmbeddedChannel(handler);
		channel.attr(Receiver.RECEIVER).set(new StubReceiver());
		channel.attr(Consts.CHARSET).set(UTF_8);

		// write bytes
		channel.writeInbound(buf);
		channel.finish();
	}

	private static class StubReceiver implements Receiver {

		private long length;

		@Override
		public void setLength(long length) {
			this.length = length;
		}

		@Override
		public boolean tryRead(ByteBuf in, Charset charset) {
			String txt = Utils.readString(in, (int) length, charset);
			System.out.println("response: " + txt);
			return true;
		}

	}
}
