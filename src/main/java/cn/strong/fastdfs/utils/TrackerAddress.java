/**
 * 
 */
package cn.strong.fastdfs.utils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tracker 地址帮助类
 * 
 * @author liulongbiao
 *
 */
public class TrackerAddress {

	static final String DFT_HOST = "127.0.0.1";
	static final int DFT_PORT = 22122;
	static final List<InetSocketAddress> DFT_ADDRS = Collections.unmodifiableList(Arrays
			.asList(new InetSocketAddress(DFT_HOST, DFT_PORT)));

	/**
	 * 创建 tracker 种子，默认顺序选取策略
	 * 
	 * @param uris
	 * @return Tracker 地址种子
	 */
	public static Seed<InetSocketAddress> createSeed(String uris) {
		return createSeed(uris);
	}

	/**
	 * 创建 tracker 种子
	 * 
	 * @param uris
	 *            以逗号分隔的地址
	 * @param strategy
	 *            选取策略
	 * @return Tracker 地址种子
	 */
	public static Seed<InetSocketAddress> createSeed(String uris, int strategy) {
		List<InetSocketAddress> addrs = parseTrackers(uris);
		return Seed.create(addrs, strategy);
	}

	/**
	 * 解析 Tracker 地址
	 * 
	 * @param uris
	 *            以逗号分隔的地址
	 * @return 网络地址列表
	 */
	public static List<InetSocketAddress> parseTrackers(String uris) {
		if (uris == null) {
			return DFT_ADDRS;
		}
		uris = uris.trim();
		if (uris.isEmpty()) {
			return DFT_ADDRS;
		}
		List<InetSocketAddress> result = new ArrayList<>();
		for (String uri : uris.split(",")) {
			result.add(parseTrackerUri(uri));
		}
		return result;
	}

	private static InetSocketAddress parseTrackerUri(String uri) {
		String host = DFT_HOST;
		int port = DFT_PORT;
		if (uri.startsWith("[")) {
			int idx = uri.indexOf("]");
			if (idx == -1) {
				throw new IllegalArgumentException(
						"an IPV6 address must be encosed with '[' and ']' according to RFC 2732.");
			}
			int portIdx = uri.indexOf("]:");
			if (portIdx != -1) {
				port = Integer.parseInt(uri.substring(portIdx + 2));
			}
			host = uri.substring(1, idx);
		} else {
			int idx = uri.indexOf(":");
			if (idx > 0) {
				try {
					port = Integer.parseInt(uri.substring(idx + 1));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException(
							"host and port should be specified in host:port format");
				}
				host = uri.substring(0, idx).trim();
			} else {
				host = uri;
			}
		}
		return new InetSocketAddress(host, port);
	}
}
