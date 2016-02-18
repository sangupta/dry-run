package com.sangupta.dryrun.redis;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;

import redis.clients.jedis.BinaryClient.LIST_POSITION;

class OpsForList<K, V> extends AbstractRedisOperations<K, V> implements ListOperations<K, V> {
	
	public OpsForList(DryRunRedisTemplate<K, V> template) {
		super(template);
	}

	@Override
	public List<V> range(K key, long start, long end) {
		return this.asList(this.mockJedis.lrange(getKey(key), start, end));
	}

	@Override
	public void trim(K key, long start, long end) {
		this.mockJedis.ltrim(getKey(key), start, end);
	}

	@Override
	public Long size(K key) {
		return this.mockJedis.llen(getKey(key));
	}

	@Override
	public Long leftPush(K key, V value) {
		return this.mockJedis.lpush(getKey(key), getValue(value));
	}

	@Override
	public Long leftPushAll(K key, V... values) {
		return this.mockJedis.lpush(getKey(key), this.asArray(values));
	}

	@Override
	public Long leftPushAll(K key, Collection<V> values) {
		return this.mockJedis.lpush(getKey(key), this.asArray(values));
	}

	@Override
	public Long leftPushIfPresent(K key, V value) {
		return this.mockJedis.lpushx(getKey(key), getValue(value));
	}

	@Override
	public Long leftPush(K key, V pivot, V value) {
		return this.mockJedis.linsert(getKey(key), LIST_POSITION.BEFORE, getValue(pivot), getValue(value));
	}

	@Override
	public Long rightPush(K key, V value) {
		return this.mockJedis.rpush(getKey(key), getValue(value));
	}

	@Override
	public Long rightPushAll(K key, V... values) {
		return this.mockJedis.rpush(getKey(key), asArray(values));
	}

	@Override
	public Long rightPushAll(K key, Collection<V> values) {
		return this.mockJedis.rpush(getKey(key), asArray(values));
	}

	@Override
	public Long rightPushIfPresent(K key, V value) {
		return this.mockJedis.rpushx(getKey(key), getValue(value));
	}

	@Override
	public Long rightPush(K key, V pivot, V value) {
		return this.mockJedis.linsert(getKey(key), LIST_POSITION.AFTER, getValue(pivot), getValue(value));
	}

	@Override
	public void set(K key, long index, V value) {
		this.mockJedis.lset(getKey(key), index, getValue(value));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long remove(K key, long count, Object value) {
		if(value instanceof byte[]) {
			return this.mockJedis.lrem(getKey(key), count, (byte[]) value);
		}
		
		return this.mockJedis.lrem(getKey(key), count, getValue((V) value));
	}

	@Override
	public V index(K key, long index) {
		return this.toValue(this.mockJedis.lindex(getKey(key), index));
	}

	@Override
	public V leftPop(K key) {
		return this.toValue(this.mockJedis.lpop(getKey(key)));
	}

	@Override
	public V leftPop(K key, long timeout, TimeUnit unit) {
		List<V> values = this.asList(this.mockJedis.blpop(this.asIntSeconds(timeout, unit), getKey(key)));
		if(values == null || values.isEmpty()) {
			return null;
		}
		
		return values.get(0);
	}

	@Override
	public V rightPop(K key) {
		return this.toValue(this.mockJedis.rpop(getKey(key)));
	}

	@Override
	public V rightPop(K key, long timeout, TimeUnit unit) {
		List<V> values = this.asList(this.mockJedis.brpop(this.asIntSeconds(timeout, unit), getKey(key)));
		if(values == null || values.isEmpty()) {
			return null;
		}
		
		return values.get(0);
	}

	@Override
	public V rightPopAndLeftPush(K sourceKey, K destinationKey) {
		return this.toValue(this.mockJedis.rpoplpush(getKey(sourceKey), getKey(destinationKey)));
	}

	@Override
	public V rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout, TimeUnit unit) {
		return this.toValue(this.mockJedis.brpoplpush(getKey(sourceKey), getKey(destinationKey), this.asIntSeconds(timeout, unit)));
	}

	@Override
	public RedisOperations<K, V> getOperations() {
		return this.template;
	}

}
