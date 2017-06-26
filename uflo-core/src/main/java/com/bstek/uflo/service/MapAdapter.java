/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.uflo.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapAdapter<K, V> extends XmlAdapter<MapAdapter.Adapter<K, V>, Map<K, V>> {

	@Override
	public Map<K, V> unmarshal(Adapter<K, V> v) throws Exception {
		if (v == null) {
			return null;
		}
		Map<K, V> map = new HashMap<K, V>();
		for (MyEntry<K, V> mapEntryType : v.getEntries()) {
			map.put(rUnmarshal(mapEntryType.getKey()), rUnmarshal(mapEntryType.getValue()));
		}
		return map;
	}

	@Override
	public Adapter<K, V> marshal(Map<K, V> v) throws Exception {
		if (v == null) {
			return null;
		}
		return new Adapter<K, V>(v);
	}

	@SuppressWarnings("unchecked")
	private static <T> T rUnmarshal(T obj) throws Exception {
		if (obj instanceof Adapter) {
			return (T) new MapAdapter<Object, Object>().unmarshal((Adapter<Object, Object>) obj);
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	static <T> T rMarshal(T obj) throws Exception {
		if (obj instanceof Map) {
			return (T) new MapAdapter<Object, Object>().marshal((Map<Object, Object>) obj);
		}
		return obj;
	}

	@XmlType
	@XmlRootElement
	public final static class Adapter<K, V> {

		@XmlElement
		protected List<MyEntry<K, V>> fEntries = new LinkedList<MyEntry<K, V>>();

		// needed for JAXB
		@SuppressWarnings("unused")
		private Adapter() {
		}

		public Adapter(Map<K, V> original) throws Exception {
			for (Map.Entry<K, V> entry : original.entrySet()) {
				this.fEntries.add(new MyEntry<K, V>(entry));
			}
		}

		public List<MyEntry<K, V>> getEntries() {
			return this.fEntries;
		}

	}

	@XmlType
	@XmlRootElement
	public final static class MyEntry<K, V> {

		@XmlElement
		protected K fKey;

		@XmlElement
		protected V fValue;

		// needed for JAXB
		@SuppressWarnings("unused")
		private MyEntry() {
		}

		public MyEntry(Map.Entry<K, V> original) throws Exception {
			this.fKey = rMarshal(original.getKey());
			this.fValue = rMarshal(original.getValue());
		}

		public K getKey() {
			return this.fKey;
		}

		public V getValue() {
			return this.fValue;
		}

	}

}
