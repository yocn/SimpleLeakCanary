package com.andev.learnleakcanary.leakcanary;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public final class KeyedWeakReference extends WeakReference<Object> {
	public final String key;
	public final String name;

	public KeyedWeakReference(Object referent, String key, String name,
							  ReferenceQueue<Object> referenceQueue) {
		super(referent, referenceQueue);
		this.key = key;
		this.name = name;
	}

	@Override
	public String toString() {
		return "KeyedWeakReference{" +
				"key='" + key + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
