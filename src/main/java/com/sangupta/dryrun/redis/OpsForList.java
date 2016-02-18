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
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;

import redis.clients.jedis.BinaryClient.LIST_POSITION;

/**
 * List operations for {@link DryRunRedisTemplate}.
 * 
 * @author sangupta
 *
 * @param <K>
 * @param <V>
 */
class OpsForList<K, V> extends AbstractRedisOperations<K, V> implements ListOperations<K, V> {
	
	public OpsForList(DryRunRedisTemplate<K, V> template) {
		super(template);
	}

	@Override
	public List<V> range(K key, long start, long end) {
		return this.asValuesList(this.mockJedis.lrange(rawKey(key), start, end));
	}

	@Override
	public void trim(K key, long start, long end) {
		this.mockJedis.ltrim(rawKey(key), start, end);
	}

	@Override
	public Long size(K key) {
		return this.mockJedis.llen(rawKey(key));
	}

	@Override
	public Long leftPush(K key, V value) {
		return this.mockJedis.lpush(rawKey(key), rawValue(value));
	}

	@Override
	public Long leftPushAll(K key, V... values) {
		return this.mockJedis.lpush(rawKey(key), this.asArray(values));
	}

	@Override
	public Long leftPushAll(K key, Collection<V> values) {
		return this.mockJedis.lpush(rawKey(key), this.asArray(values));
	}

	@Override
	public Long leftPushIfPresent(K key, V value) {
		return this.mockJedis.lpushx(rawKey(key), rawValue(value));
	}

	@Override
	public Long leftPush(K key, V pivot, V value) {
		return this.mockJedis.linsert(rawKey(key), LIST_POSITION.BEFORE, rawValue(pivot), rawValue(value));
	}

	@Override
	public Long rightPush(K key, V value) {
		return this.mockJedis.rpush(rawKey(key), rawValue(value));
	}

	@Override
	public Long rightPushAll(K key, V... values) {
		return this.mockJedis.rpush(rawKey(key), asArray(values));
	}

	@Override
	public Long rightPushAll(K key, Collection<V> values) {
		return this.mockJedis.rpush(rawKey(key), asArray(values));
	}

	@Override
	public Long rightPushIfPresent(K key, V value) {
		return this.mockJedis.rpushx(rawKey(key), rawValue(value));
	}

	@Override
	public Long rightPush(K key, V pivot, V value) {
		return this.mockJedis.linsert(rawKey(key), LIST_POSITION.AFTER, rawValue(pivot), rawValue(value));
	}

	@Override
	public void set(K key, long index, V value) {
		this.mockJedis.lset(rawKey(key), index, rawValue(value));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long remove(K key, long count, Object value) {
		if(value instanceof byte[]) {
			return this.mockJedis.lrem(rawKey(key), count, (byte[]) value);
		}
		
		return this.mockJedis.lrem(rawKey(key), count, rawValue((V) value));
	}

	@Override
	public V index(K key, long index) {
		return this.asValue(this.mockJedis.lindex(rawKey(key), index));
	}

	@Override
	public V leftPop(K key) {
		return this.asValue(this.mockJedis.lpop(rawKey(key)));
	}

	@Override
	public V leftPop(K key, long timeout, TimeUnit unit) {
		List<V> values = this.asValuesList(this.mockJedis.blpop(this.asIntSeconds(timeout, unit), rawKey(key)));
		if(values == null || values.isEmpty()) {
			return null;
		}
		
		return values.get(0);
	}

	@Override
	public V rightPop(K key) {
		return this.asValue(this.mockJedis.rpop(rawKey(key)));
	}

	@Override
	public V rightPop(K key, long timeout, TimeUnit unit) {
		List<V> values = this.asValuesList(this.mockJedis.brpop(this.asIntSeconds(timeout, unit), rawKey(key)));
		if(values == null || values.isEmpty()) {
			return null;
		}
		
		return values.get(0);
	}

	@Override
	public V rightPopAndLeftPush(K sourceKey, K destinationKey) {
		return this.asValue(this.mockJedis.rpoplpush(rawKey(sourceKey), rawKey(destinationKey)));
	}

	@Override
	public V rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout, TimeUnit unit) {
		return this.asValue(this.mockJedis.brpoplpush(rawKey(sourceKey), rawKey(destinationKey), this.asIntSeconds(timeout, unit)));
	}

	@Override
	public RedisOperations<K, V> getOperations() {
		return this.template;
	}

}
