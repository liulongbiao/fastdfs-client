package cn.strong.fastdfs.client.protocol.response;

import static cn.strong.fastdfs.client.Consts.FDFS_GROUP_LEN;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.charset.Charset;

import org.junit.Test;

import cn.strong.fastdfs.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by liulongbiao on 16-7-14.
 */
public class StoragePathReceiverTest {

    @Test
    public void test() {
		Charset charset = UTF_8;
        StoragePathReceiver receiver = new StoragePathReceiver();
		receiver.promise().whenComplete((data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println("received: " + data);
			}
		});
        ByteBuf buf = Unpooled.buffer();
		Utils.writeFixLength(buf, "group1", FDFS_GROUP_LEN, charset);
		byte[] bytes = "01/E3/hello.txt".getBytes(charset);
        buf.writeBytes(bytes);

        receiver.setLength(FDFS_GROUP_LEN + bytes.length);
		receiver.tryRead(buf, charset);
    }

}
