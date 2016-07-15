package cn.strong.fastdfs.client.protocol.response;

import static cn.strong.fastdfs.client.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.client.Consts.FDFS_HOST_LEN;
import static java.nio.charset.StandardCharsets.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

import org.junit.Test;

import cn.strong.fastdfs.utils.Utils;

/**
 * Created by liulongbiao on 16-7-14.
 */
public class StorageServerInfoListReceiverTest {
    @Test
    public void test() {
		Charset charset = UTF_8;
        StorageServerInfoListReceiver receiver = new StorageServerInfoListReceiver();
		receiver.observable().subscribe(data -> {
            System.out.println("received: ");
            data.forEach(System.out::println);
        }, ex -> {
            ex.printStackTrace();
        }, () -> {
            System.out.println("completed");
        });
        ByteBuf buf = Unpooled.buffer();
		Utils.writeFixLength(buf, "group1", FDFS_GROUP_LEN, charset);
		Utils.writeFixLength(buf, "127.0.0.1", FDFS_HOST_LEN, charset);
        buf.writeLong(8080);
		Utils.writeFixLength(buf, "192.168.20.2", FDFS_HOST_LEN, charset);
        receiver.setLength(buf.readableBytes());
		receiver.tryRead(buf, charset);
    }
}
