/**
 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package cn.iscas;


/**
 * @author wuheng@iscas.ac.cn
 *
 */
public class Utils {

	public static long toNum (String value) {
		long num = 0;
		if (value.endsWith("Ki")) {
			num = Long.parseLong(value.substring(0, value.length() - 2)) * 1024;
		} else if (value.endsWith("Mi")) {
			num = Long.parseLong(value.substring(0, value.length() - 2)) * 1024 * 1024;
		} else if (value.endsWith("Gi")) {
			num = Long.parseLong(value.substring(0, value.length() - 2)) * 1024 * 1024 * 1024;
		} else if (value.endsWith("Ti")) {
			num = Long.parseLong(value.substring(0, value.length() - 2)) * 1024 * 1024 * 1024 * 1024;
		} else if (value.endsWith("Pi")) {
			num = Long.parseLong(value.substring(0, value.length() - 2)) * 1024 * 1024 * 1024 * 1024;
		} else {
			num = Long.parseLong(value);
		}

		return num;
	}
}
