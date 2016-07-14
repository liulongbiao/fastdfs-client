package cn.strong.fastdfs.client.protocol.response;

import cn.strong.fastdfs.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static cn.strong.fastdfs.client.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.client.Consts.FDFS_HOST_LEN;
import static cn.strong.fastdfs.client.Consts.FDFS_STORAGE_STORE_LEN;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by liulongbiao on 16-7-14.
 */
public class StorageServerInfoReceiverTest {
    @Test
    public void test() {
        StorageServerInfoReceiver receiver = new StorageServerInfoReceiver();
        receiver.toObservable().subscribe(data -> {
            System.out.println("received: " + data);
        }, ex -> {
            ex.printStackTrace();
        }, () -> {
            System.out.println("completed");
        });
        ByteBuf buf = Unpooled.buffer();
        Utils.writeFixLength(buf, "group1", FDFS_GROUP_LEN, UTF_8);
        Utils.writeFixLength(buf, "127.0.0.1", FDFS_HOST_LEN, UTF_8);
        buf.writeLong(8080);
        buf.writeByte(2);

        receiver.setLength(FDFS_STORAGE_STORE_LEN);
        receiver.tryRead(buf);
    }
}
