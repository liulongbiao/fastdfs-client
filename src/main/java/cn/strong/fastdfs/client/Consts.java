/**
 * 
 */
package cn.strong.fastdfs.client;

/**
 * 常量定义
 * 
 * @author liulongbiao
 *
 */
public interface Consts {

	int HEAD_LEN = 10; // 头部长度
	int FDFS_LONG_LEN = 8; // long 所占字节数
	int FDFS_PROTO_PKG_LEN_SIZE = FDFS_LONG_LEN; // 包长字节数
	int FDFS_FILE_EXT_LEN = 6; // 文件扩展名字节数
	int FDFS_GROUP_LEN = 16; // 文件分组字节数
	int FDFS_HOST_LEN = 15; // 主机地址字节数
	int FDFS_PORT_LEN = FDFS_LONG_LEN; // 端口 字节数
	int FDFS_STORE_PATH_INDEX_LEN = 1; // storePathIndex 字节数
	int FDFS_STORAGE_LEN = FDFS_GROUP_LEN + FDFS_HOST_LEN + FDFS_PORT_LEN; // 存储服务器信息字节数
	int FDFS_STORAGE_STORE_LEN = FDFS_STORAGE_LEN + FDFS_STORE_PATH_INDEX_LEN; // 存储服务器信息及存储路径索引字节数

	/**
	 * Overwrite MetaData
	 */
	byte STORAGE_SET_METADATA_FLAG_OVERWRITE = 'O';
	/**
	 * Merge MetaData
	 */
	byte STORAGE_SET_METADATA_FLAG_MERGE = 'M';

	String FDFS_RECORD_SEPERATOR = "\u0001";
	String FDFS_FIELD_SEPERATOR = "\u0002";
	byte ERRNO_OK = 0;
}
