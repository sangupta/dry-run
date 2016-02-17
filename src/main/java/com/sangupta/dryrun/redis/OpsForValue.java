/**
 *
 * DryRun - Mocked classes for unit testing
 * Copyright (c) 2016, Sandeep Gupta
 * 
 * http://sangupta.com/projects/dryrun
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.dryrun.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;

import redis.clients.jedis.Transaction;

class OpsForValue<K, V> extends AbstractRedisOperations<K, V> implements ValueOperations<K, V> {
	
	public OpsForValue(DryRunRedisTemplate<K, V> template) {
		super(template);
	}

	@Override
	public void set(K key, V value) {
		this.mockJedis.set(getKey(key), getValue(value));
	}

	@Override
	public void set(K key, V value, long timeout, TimeUnit unit) {
		if(TimeUnit.MILLISECONDS.equals(unit)) {
			int millis = ((Long) timeout).intValue();
			this.mockJedis.psetex(getKey(key), millis, getValue(value));
			return;
		}
		
		Long seconds = unit.toSeconds(timeout);
		this.mockJedis.setex(getKey(key), seconds.intValue(), getValue(value));
	}

	@Override
	public Boolean setIfAbsent(K key, V value) {
		Long result = this.mockJedis.setnx(getKey(key), getValue(value));
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
			
			transaction.set(getKey(key), getValue(value));
		}
		
		transaction.exec();
	}

	@Override
	public Boolean multiSetIfAbsent(Map<? extends K, ? extends V> map) {
		Transaction transaction = this.mockJedis.multi();
		for(Entry<? extends K, ? extends V> entry : map.entrySet()) {
			K key = entry.getKey();
			V value = entry.getValue();
			
			transaction.setnx(getKey(key), getValue(value));
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
			result = this.mockJedis.get(getKey((K) key));
		}
		
		return this.template.valueSerializer.deserialize(result);
	}

	@Override
	public V getAndSet(K key, V value) {
		byte[] result = this.mockJedis.getSet(getKey(key), getValue(value));
		
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
		return this.mockJedis.incrBy(getKey(key), delta);
	}

	@Override
	public Double increment(K key, double delta) {
		return this.mockJedis.incrByFloat(getKey(key), delta);
	}

	@Override
	public Integer append(K key, String value) {
		Long result = this.mockJedis.append(getKey(key), getString(value));
		if(result != null) {
			return result.intValue();
		}
		
		return null;
	}

	@Override
	public String get(K key, long start, long end) {
		byte[] result = this.mockJedis.getrange(getKey(key), start, end);
		return this.template.stringSerializer.deserialize(result);
	}

	@Override
	public void set(K key, V value, long offset) {
		this.mockJedis.setrange(getKey(key), offset, getValue(value));
	}

	@Override
	public Long size(K key) {
		return this.mockJedis.strlen(getKey(key));
	}

	@Override
	public RedisOperations<K, V> getOperations() {
		return this.template;
	}

	@Override
	public Boolean setBit(K key, long offset, boolean value) {
		return this.mockJedis.setbit(getKey(key), offset, value);
	}

	@Override
	public Boolean getBit(K key, long offset) {
		return this.mockJedis.getbit(getKey(key), offset);
	}
	
}
