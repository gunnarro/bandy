package com.gunnarro.android.bandy.custom;

import android.util.Log;

public class CustomLog {

	private static final boolean IS_TRACE_ON = false;
	private static final boolean IS_SYSTEM_OUT = true;

	public static void i(Class<?> clazz, String msg) {
		if (IS_TRACE_ON) {
			Log.i(createTag(clazz), msg);
		}

		if (IS_SYSTEM_OUT) {
			System.out.println(createTag(clazz) + " " + msg);
		}
	}

	public static void d(Class<?> clazz, Object msg) {
		if (IS_TRACE_ON) {
			if (msg != null) {
				Log.d(createTag(clazz), msg.toString());
			} else {
				Log.d(createTag(clazz), "Log object is null!");
			}
		}
		if (IS_SYSTEM_OUT) {
			System.out.println(createTag(clazz) + " " + msg);
		}
	}

	public static void e(Class<?> clazz, String msg) {
		Log.e(createTag(clazz), msg);
	}

	private static String createTag(Class<?> clazz) {
		StringBuffer tag = new StringBuffer();
		for (StackTraceElement stackTrace : Thread.currentThread().getStackTrace()) {
			if (stackTrace.getClassName().equals(clazz.getName())) {
				tag.append(clazz.getSimpleName()).append(".").append(stackTrace.getMethodName()).append(":").append(stackTrace.getLineNumber());
				break;
			}
		}
		return tag.toString();
	}
}
