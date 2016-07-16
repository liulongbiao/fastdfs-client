/**
 * 
 */
package cn.strong.fastdfs.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * IO 工具类
 * 
 * @author liulongbiao
 *
 */
public class IOUtils {
	/**
	 * 关闭
	 * 
	 * @param closeable
	 */
	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}
}
