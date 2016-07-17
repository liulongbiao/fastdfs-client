# fastdfs-client

fastdfs-client is a [FastDFS](https://github.com/happyfish100/fastdfs) java client 
based on [Netty 4](http://netty.io) and [RxJava](https://github.com/ReactiveX/RxJava) .

## 前置条件

* Java 8 required.

## maven

```xml
<dependency>
	<groupId>cn.strong</groupId>
	<artifactId>fastdfs-client</artifactId>
	<version>2.0.0</version>
</dependency>
```

## 使用说明

程序的主要入口为 `FastdfsClient`，它使用 RxJava 实现。
同时为降低使用难度，还提供了一个 `SimpleFastdfsClient` 作为同步和路径简化的门面类。

`FastdfsClient` 后端使用 `FastdfsTemplate` 做实际的网络通信工作，
以及 `Seed<InetSocketAddress>` 来选取 tracker URL。

`FastdfsTemplate` 具有生命周期，它维护了一个通道连接池以及 IO 线程组等资源。

```java
Settings settings = new Settings();
FastdfsTemplate template = new FastdfsTemplate(settings);
try {
  List<InetSocketAddress> hosts = Arrays
    .asList(new InetSocketAddress("192.168.20.68", 22122));
  Seed<InetSocketAddress> seed = Seed.create(hosts, Seed.PICK_ROUND_ROBIN);
  FastdfsClient client = new FastdfsClient(template, seed);
  // do sth. with FastdfsClient
} finally {
  IOUtils.close(template);
}
```

得到 `FastdfsClient` 实例以后即可通过相应 API 进行上传下载等操作。

```java
// 上传
byte[] bytes = "Hello Fastdfs".getBytes(UTF_8);
CountDownLatch latch = new CountDownLatch(1);
client.upload(bytes, bytes.length, "txt", null)
  .subscribe(path -> {
    System.out.println("upload path: " + path);
    //-> upload path: group1/M00/05/22/wKgURFUP60SEYdruAAAAAEKHwLQ894.txt
  }, ex -> {
    ex.printStackTrace();
    latch.countDown();
  }, () -> {
    System.out.println("completed");
    latch.countDown();
  });
latch.await();

// 下载
StoragePath spath = StoragePath.fromFullPath("group1/M00/05/22/wKgURFUP60SEYdruAAAAAEKHwLQ124.txt");
Observable<ByteBuf> content = client.download(spath);
ByteArrayOutputStream output = new ByteArrayOutputStream();
client.download(spath).toBlocking().forEach(buf -> {
  try {
    buf.readBytes(output, buf.readableBytes());
  } catch (Exception e) {
    throw new FastdfsException("read downloaded bytes error", e);
  }
});
byte[] downloaded = output.toByteArray();
```

`SimpleFastdfsClient` 对 `FastdfsClient` 做了一层封装，平滑 RxJava 的学习曲线

* 使用同步方法，降低未学过 RxJava 的同学的学习曲线
* 存储路径使用全路径

具体 `FastdfsClient` 及 `SimpleFastdfsClient` 的使用详见 API 及对应的集成测试用例。
