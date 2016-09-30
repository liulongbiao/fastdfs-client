# fastdfs-client

fastdfs-client is a [FastDFS](https://github.com/happyfish100/fastdfs) java client 
based on [Netty 4](http://netty.io).

## 前置条件

* Java 8 required.

## maven

```xml
<dependency>
	<groupId>cn.strong</groupId>
	<artifactId>fastdfs-client</artifactId>
	<version>3.0.0</version>
</dependency>
```

## 使用说明

程序的主要入口为 `SimpleFastdfsClient/FastdfsClient`。

`FastdfsClient` 后端使用 `FastdfsTemplate` 做实际的网络通信工作，
以及 `Seed<InetSocketAddress>` 来选取 tracker URL。
`FastdfsTemplate` 具有生命周期，它维护了一个通道连接池以及 IO 线程组等资源。

`SimpleFastdfsClient` 对 `FastdfsClient` 做了一层封装，采用同步方式，且存储路径也使用字符串全路径。

```java
Settings settings = new Settings();
FastdfsTemplate template = new FastdfsTemplate(settings);
try {
  SimpleFastdfsClient client = new SimpleFastdfsClient(template, TrackerAddress.createSeed("192.168.20.68:22122"));
  // do sth. with SimpleFastdfsClient
} finally {
  IOUtils.close(template);
}
```

得到 `SimpleFastdfsClient` 实例以后即可通过相应 API 进行上传下载等操作。

```java
// 上传
byte[] bytes = "Hello Fastdfs".getBytes(UTF_8);
String path = client.upload(bytes, "txt");
System.out.println("path: " + path);

// 下载
String path = "group1/M00/15/93/wKgURFfGi4-AeRRwAAAADSghvvI390.txt";
byte[] bytes = client.download(path);
String data = new String(bytes, UTF_8);
System.out.println("downloaded: " + data);
```

具体 `FastdfsClient` 及 `SimpleFastdfsClient` 的使用详见 API 及对应的集成测试用例。
