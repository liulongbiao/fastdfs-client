# fastdfs-client

fastdfs-client is a [FastDFS](https://github.com/happyfish100/fastdfs) java client 
based on [Netty 4](http://netty.io) and [RxJava](https://github.com/ReactiveX/RxJava) .

## 前置条件

* Java 8 required.
* Fastdfs 服务器端字符集编码需使用 UTF-8

## maven

```xml
<dependency>
	<groupId>cn.strong</groupId>
	<artifactId>fastdfs-client</artifactId>
	<version>1.0.0</version>
</dependency>
```

## 使用说明

程序的主要入口为 FastdfsClient 门面类。
