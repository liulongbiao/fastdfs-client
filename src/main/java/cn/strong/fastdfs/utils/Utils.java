/**
 * 
 */
package cn.strong.fastdfs.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import cn.strong.fastdfs.client.Consts;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.ReferenceCountUtil;

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
		ByteBuf buf = null;
		try {
			buf = in.readBytes(length);
			return buf.toString(charset).trim();
		} finally {
			ReferenceCountUtil.safeRelease(buf);
		}
	}

	/**
	 * 判断字符串为空
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isEmpty(String content) {
		return content == null || content.isEmpty();
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileExt(String filename) {
		if (filename == null) {
			return "";
		}
		int idx = filename.lastIndexOf('.');
		return idx == -1 ? "" : filename.substring(idx + 1).toLowerCase();
	}

	/**
	 * 获取 channel 中的 Charset 属性
	 * 
	 * @param ch
	 * @return
	 */
	public static Charset ensureGetCharset(Channel ch) {
		Charset charset = ch.attr(Consts.CHARSET).get();
		if (charset == null) {
			charset = UTF_8;
			ch.attr(Consts.CHARSET).set(charset);
		}
		return charset;
	}

	/**
	 * 判断集合是否为空
	 * 
	 * @param coll
	 * @return
	 */
	public static <T> boolean isEmpty(Collection<T> coll) {
		return coll == null || coll.isEmpty();
	}

	/**
	 * 获取列表头元素
	 * 
	 * @param list
	 * @return
	 */
	public static <T> T head(List<T> list) {
		return isEmpty(list) ? null : list.get(0);
	}
}
