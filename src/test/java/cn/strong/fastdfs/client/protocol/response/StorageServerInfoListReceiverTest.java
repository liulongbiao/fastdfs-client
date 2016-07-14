package cn.strong.fastdfs.client.protocol.response;

import cn.strong.fastdfs.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static cn.strong.fastdfs.client.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.client.Consts.FDFS_HOST_LEN;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by liulongbiao on 16-7-14.
 */
public class StorageServerInfoListReceiverTest {
    @Test
    public void test() {
        StorageServerInfoListReceiver receiver = new StorageServerInfoListReceiver();
        receiver.toObservable().subscribe(data -> {
            System.out.println("received: ");
            data.forEach(System.out::println);
        }, ex -> {
            ex.printStackTrace();
        }, () -> {
            System.out.println("completed");
        });
        ByteBuf buf = Unpooled.buffer();
        Utils.writeFixLength(buf, "group1", FDFS_GROUP_LEN, UTF_8);
        Utils.writeFixLength(buf, "127.0.0.1", FDFS_HOST_LEN, UTF_8);
        buf.writeLong(8080);
        Utils.writeFixLength(buf, "192.168.20.2", FDFS_HOST_LEN, UTF_8);
        receiver.setLength(buf.readableBytes());
        receiver.tryRead(buf);
    }
}
