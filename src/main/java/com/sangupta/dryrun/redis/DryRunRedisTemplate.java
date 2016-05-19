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

import java.util.Collection;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.sangupta.dryredis.DryRedis;

/**
 * An implementation of the {@link RedisTemplate} that uses {@link MockJedis} to
 * mock actual <b>Jedis</b> connections - thus helping with tests that can run
 * purely in-memory.
 * 
 * @author sangupta
 *
 * @param <K>
 *            the type of keys to be stored in redis
 * 
 * @param <V>
 *            the type of values to be stored in redis
 */
public class DryRunRedisTemplate<K, V> extends RedisTemplate<K, V> {
    
    final DryRedis dryRedis;
    
    final DryRedisBridge bridge;
	
	RedisSerializer<K> keySerializer;
	
	RedisSerializer<V> valueSerializer;
	
	RedisSerializer<String> stringSerializer = new StringRedisSerializer();
	
	private final OpsForValue<K, V> opsForValue;
	
	private final OpsForSet<K, V> opsForSet;
	
	private final OpsForList<K, V> opsForList;
	
	private final OpsForHyperLogLog<K, V> opsForHyperLogLog;
	
	public DryRunRedisTemplate(DryRedis dryRedis) {
	    this.dryRedis = dryRedis;
	    this.bridge = new DryRedisBridge(dryRedis);
	    
		this.opsForValue = new OpsForValue<K, V>(this);
		this.opsForSet = new OpsForSet<K, V>(this);
		this.opsForList = new OpsForList<K, V>(this);
		this.opsForHyperLogLog = new OpsForHyperLogLog<K, V>(this);
	}
	
	@Override
	public Boolean hasKey(K key) {
		byte[] keyBytes = this.keySerializer.serialize(key);
		return this.bridge.exists(keyBytes);
	}
	
	@Override
	public void delete(K key) {
		byte[] keyBytes = this.keySerializer.serialize(key);
		this.bridge.del(keyBytes);
	}

	@Override
	public void delete(Collection<K> keys) {
		for(K key : keys) {
			this.delete(key);
		}
	}
	
	// Usual getters that are meant for RedisTemplate options
	
	@Override
	public ValueOperations<K, V> opsForValue() {
		return this.opsForValue;
	}
	
	@Override
	public SetOperations<K, V> opsForSet() {
		return this.opsForSet;
	}
	
	@Override
	public ListOperations<K, V> opsForList() {
		return this.opsForList;
	}
	
	@Override
	public HyperLogLogOperations<K, V> opsForHyperLogLog() {
		return this.opsForHyperLogLog;
	}
	
	@Override
	public <HK, HV> HashOperations<K, HK, HV> opsForHash() {
		// TODO Auto-generated method stub
		return super.opsForHash();
	}
	
	// Usual setters
	
	@Override
	@SuppressWarnings("unchecked")
	public void setKeySerializer(RedisSerializer<?> serializer) {
		keySerializer = (RedisSerializer<K>) serializer;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void setValueSerializer(RedisSerializer<?> serializer) {
		valueSerializer = (RedisSerializer<V>) serializer;
	}
	
	@Override
	public void setStringSerializer(RedisSerializer<String> stringSerializer) {
		this.stringSerializer = stringSerializer;
	}
	
}
