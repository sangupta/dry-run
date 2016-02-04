package com.sangupta.dryrun.redis;

import java.util.Collection;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fiftyonred.mock_jedis.MockJedis;

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
	
	final MockJedis mockJedis;
	
	RedisSerializer<K> keySerializer;
	
	RedisSerializer<V> valueSerializer;
	
	RedisSerializer<String> stringSerializer = new StringRedisSerializer();
	
	private final OpsForValue<K, V> opsForValue;
	
	public DryRunRedisTemplate(MockJedis jedis) {
		this.mockJedis = jedis;
		opsForValue = new OpsForValue<K, V>(this);
	}
	
	@Override
	public void delete(K key) {
		byte[] keyBytes = this.keySerializer.serialize(key);
		this.mockJedis.del(keyBytes);
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
