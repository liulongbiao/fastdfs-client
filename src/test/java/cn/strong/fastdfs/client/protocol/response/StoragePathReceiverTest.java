package cn.strong.fastdfs.client.protocol.response;

import cn.strong.fastdfs.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static cn.strong.fastdfs.client.Consts.FDFS_GROUP_LEN;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by liulongbiao on 16-7-14.
 */
public class StoragePathReceiverTest {

    @Test
    public void test() {
        StoragePathReceiver receiver = new StoragePathReceiver();
        receiver.toObservable().subscribe(data -> {
            System.out.println("received: " + data);
        }, ex -> {
            ex.printStackTrace();
        }, () -> {
            System.out.println("completed");
        });
        ByteBuf buf = Unpooled.buffer();
        Utils.writeFixLength(buf, "group1", FDFS_GROUP_LEN, UTF_8);
        byte[] bytes = "01/E3/hello.txt".getBytes();
        buf.writeBytes(bytes);

        receiver.setLength(FDFS_GROUP_LEN + bytes.length);
        receiver.tryRead(buf);
    }

}
