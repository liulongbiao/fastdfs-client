package cn.strong.fastdfs.client.protocol.request.storage;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.strong.fastdfs.client.CommandCodes;
import cn.strong.fastdfs.client.Consts;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.utils.Utils;

public class AppendRequestTest {

	@Test
	public void test() {
		Charset charset = UTF_8;
		byte[] bytes = "\nappend fastdfs".getBytes(charset);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/0A/97/wKgURFeI0ZyEeorsAAAAADVhaBw380.inf");
		AppendRequest request = new AppendRequest(bytes, bytes.length, spath);

		ByteBufAllocator alloc = PooledByteBufAllocator.DEFAULT;
		List<Object> out = new ArrayList<>();

		request.encode(alloc, out, charset);

		assertEquals(out.size(), 3);
		ByteBuf head = (ByteBuf) out.get(0);
		long length = head.readLong();
		System.out.println(length);
		assertEquals(head.readByte(), CommandCodes.STORAGE_PROTO_CMD_APPEND_FILE);
		assertEquals(head.readByte(), Consts.ERRNO_OK);

		ByteBuf meta = (ByteBuf) out.get(1);
		long pathLen = meta.readLong();
		long size = meta.readLong();
		String path = Utils.readString(meta, (int) pathLen, charset);
		System.out.println(path);

		ByteBuf content = (ByteBuf) out.get(2);
		System.out.println(out.get(2).getClass());
		assertEquals(content.readableBytes(), size);
		assertEquals(content.toString(charset), "\nappend fastdfs");
	}

}
