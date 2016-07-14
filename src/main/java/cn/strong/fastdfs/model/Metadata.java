/**
 * 
 */
package cn.strong.fastdfs.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件元数据
 * 
 * @author liulongbiao
 *
 */
public class Metadata {

	final Map<String, String> map;

	public Metadata() {
		this.map = new LinkedHashMap<>();
	}

	public Metadata append(String key, String value) {
		map.put(key, value);
		return this;
	}

	public Map<String, String> toMap() {
		return Collections.unmodifiableMap(map);
	}

}
