package com.sangupta.dryrun.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fiftyonred.mock_jedis.MockJedis;

abstract class AbstractRedisOperations<K, V> {

	final DryRunRedisTemplate<K, V> template;
	
	final MockJedis mockJedis;

	AbstractRedisOperations(DryRunRedisTemplate<K, V> template) {
		this.template = template;
		this.mockJedis = template.mockJedis;
	}

	byte[] getKey(K key) {
		return this.template.keySerializer.serialize(key);
	}
	
	byte[] getValue(V value) {
		return this.template.valueSerializer.serialize(value);
	}

	byte[] getString(String str) {
		return this.template.stringSerializer.serialize(str);
	}
	
	V toValue(byte[] bytes) {
		return this.template.valueSerializer.deserialize(bytes);
	}

	List<V> asList(List<byte[]> bytes) {
		List<V> result = new ArrayList<V>();
		for(byte[] bs : bytes) {
			result.add(this.template.valueSerializer.deserialize(bs));
		}
		
		return result;
	}
	
	byte[][] asArray(V... values) {
		byte[][] array = new byte[values.length][];
		int index = 0;
		for(V value : values) {
			array[index++] = getValue(value);
		}
		
		return array;
	}
	
	byte[][] asArray(Collection<V> values) {
		byte[][] array = new byte[values.size()][];
		int index = 0;
		for(V value : values) {
			array[index++] = getValue(value);
		}
		
		return array;
	}

	int asIntSeconds(long timeout, TimeUnit unit) {
		Long value = unit.toSeconds(timeout);
		return value.intValue();
	}
	
}
