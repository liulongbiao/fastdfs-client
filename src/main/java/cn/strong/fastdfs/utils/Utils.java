/**
 * 
 */
package cn.strong.fastdfs.utils;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * 工具类
 * 
 * @author liulongbiao
 *
 */
public class Utils {

	private Utils() {
		super();
	}

	/**
	 * 给 ByteBuf 写入定长字符串
	 * <p>
	 * 若字符串长度大于定长，则截取定长字节；若小于定长，则补零
	 * 
	 * @param buf
	 * @param content
	 * @param length
	 */
	public static void writeFixLength(ByteBuf buf, String content, int length, Charset charset) {
		byte[] bytes = content.getBytes(charset);
		int blen = bytes.length;
		int wlen = blen > length ? length : blen;
		buf.writeBytes(bytes, 0, wlen);
		if (wlen < length) {
			buf.writeZero(length - wlen);
		}
	}

	/**
	 * 读取固定长度的字符串(修剪掉补零的字节)
	 * 
	 * @param in
	 * @param length
	 * @return
	 */
	public static String readString(ByteBuf in, int length, Charset charset) {
		return in.readBytes(length).toString(charset).trim();
	}
}
