package com.sangupta.dryrun.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;

import com.fiftyonred.mock_jedis.MockJedis;

import redis.clients.jedis.Transaction;

class OpsForValue<K, V> implements ValueOperations<K, V> {
	
	final DryRunRedisTemplate<K, V> template;
	
	final MockJedis mockJedis;
	
	public OpsForValue(DryRunRedisTemplate<K, V> template) {
		this.template = template;
		this.mockJedis = this.template.mockJedis;
	}

	@Override
	public void set(K key, V value) {
		byte[] keyBytes = this.template.keySerializer.serialize(key);
		byte[] valueBytes = this.template.valueSerializer.serialize(value);
		this.mockJedis.set(keyBytes, valueBytes);
	}

	@Override
	public void set(K key, V value, long timeout, TimeUnit unit) {
		byte[] keyBytes = this.template.keySerializer.serialize(key);
		byte[] valueBytes = this.template.valueSerializer.serialize(value);
		
		if(TimeUnit.MILLISECONDS.equals(unit)) {
			int millis = ((Long) timeout).intValue();
			this.mockJedis.psetex(keyBytes, millis, valueBytes);
			return;
		}
		
		Long seconds = unit.toSeconds(timeout);
		this.mockJedis.setex(keyBytes, seconds.intValue(), valueBytes);
	}

	@Override
	public Boolean setIfAbsent(K key, V value) {
		byte[] keyBytes = this.template.keySerializer.serialize(key);
		byte[] valueBytes = this.template.valueSerializer.serialize(value);
		Long result = this.mockJedis.setnx(keyBytes, valueBytes);
		if(result != null && result > 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public void multiSet(Map<? extends K, ? extends V> map) {
		Transaction transaction = this.mockJedis.multi();
		for(Entry<? extends K, ? extends V> entry : map.entrySet()) {
			K key = entry.getKey();
			V value = entry.getValue();
			
			byte[] keyBytes = this.template.keySerializer.serialize(key);
			byte[] valueBytes = this.template.valueSerializer.serialize(value);
			
			transaction.set(keyBytes, valueBytes);
		}
		
		transaction.exec();
	}

	@Override
	public Boolean multiSetIfAbsent(Map<? extends K, ? extends V> map) {
		Transaction transaction = this.mockJedis.multi();
		for(Entry<? extends K, ? extends V> entry : map.entrySet()) {
			K key = entry.getKey();
			V value = entry.getValue();
			
			byte[] keyBytes = this.template.keySerializer.serialize(key);
			byte[] valueBytes = this.template.valueSerializer.serialize(value);
			
			transaction.setnx(keyBytes, valueBytes);
		}
		
		transaction.exec();
		
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		byte[] result;
		if(key instanceof byte[]) {
			result = this.mockJedis.get((byte[]) key);
		} else {
			byte[] keyBytes = this.template.keySerializer.serialize((K) key);
			result = this.mockJedis.get(keyBytes);
		}
		
		return this.template.valueSerializer.deserialize(result);
	}

	@Override
	public V getAndSet(K key, V value) {
		byte[] keyBytes = this.template.keySerializer.serialize(key);
		byte[] valueBytes = this.template.valueSerializer.serialize(value);
		byte[] result = this.mockJedis.getSet(keyBytes, valueBytes);
		
		return this.template.valueSerializer.deserialize(result);
	}

	@Override
	public List<V> multiGet(Collection<K> keys) {
		List<V> result = new ArrayList<V>();
		for(K key : keys) {
			result.add(this.get(key));
		}
		
		return result;
	}

	@Override
	public Long increment(K key, long delta) {
		byte[] keyBytes = this.template.keySerializer.serialize(key);
		return this.mockJedis.incrBy(keyBytes, delta);
	}

	@Override
	public Double increment(K key, double delta) {
		byte[] keyBytes = this.template.keySerializer.serialize(key);
		return this.mockJedis.incrByFloat(keyBytes, delta);
	}

	@Override
	public Integer append(K key, String value) {
		byte[] keyBytes = this.template.keySerializer.serialize(key);
		byte[] valueBytes = this.template.stringSerializer.serialize(value);
		Long result = this.mockJedis.append(keyBytes, valueBytes);
		if(result != null) {
			return result.intValue();
		}
		
		return null;
	}

	@Override
	public String get(K key, long start, long end) {
		byte[] keyBytes = this.template.keySerializer.serialize(key);
		byte[] result = this.mockJedis.getrange(keyBytes, start, end);
		return this.template.stringSerializer.deserialize(result);
	}

	@Override
	public void set(K key, V value, long offset) {
		byte[] keyBytes = this.template.keySerializer.serialize(key);
		byte[] valueBytes = this.template.valueSerializer.serialize(value);
		this.mockJedis.setrange(keyBytes, offset, valueBytes);
	}

	@Override
	public Long size(K key) {
		byte[] keyBytes = this.template.keySerializer.serialize(key);
		return this.mockJedis.strlen(keyBytes);
	}

	@Override
	public RedisOperations<K, V> getOperations() {
		return this.template;
	}

	@Override
	public Boolean setBit(K key, long offset, boolean value) {
		byte[] keyBytes = this.template.keySerializer.serialize(key);
		return this.mockJedis.setbit(keyBytes, offset, value);
	}

	@Override
	public Boolean getBit(K key, long offset) {
		byte[] keyBytes = this.template.keySerializer.serialize(key);
		return this.mockJedis.getbit(keyBytes, offset);
	}
	
}
