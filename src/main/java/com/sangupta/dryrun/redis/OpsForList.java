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

import com.sangupta.dryredis.support.DryRedisInsertOrder;

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
		return this.asValuesList(this.bridge.lrange(rawKey(key), start, end));
	}

	@Override
	public void trim(K key, long start, long end) {
		this.bridge.ltrim(rawKey(key), start, end);
	}

	@Override
	public Long size(K key) {
		long l = this.bridge.llen(rawKey(key));
		return l;
	}

	@Override
	public Long leftPush(K key, V value) {
		long l = this.bridge.lpush(rawKey(key), rawValue(value));
		return l;
	}

	@Override
	public Long leftPushAll(K key, V... values) {
		long l = this.bridge.lpush(rawKey(key), this.asList(values));
		return l;
	}

	@Override
	public Long leftPushAll(K key, Collection<V> values) {
		long l = this.bridge.lpush(rawKey(key), this.asList(values));
		return l;
	}

	@Override
	public Long leftPushIfPresent(K key, V value) {
		long l = this.bridge.lpushx(rawKey(key), rawValue(value));
		return l;
	}

	@Override
	public Long leftPush(K key, V pivot, V value) {
		long l = this.bridge.linsert(rawKey(key), DryRedisInsertOrder.BEFORE, rawValue(pivot), rawValue(value));
		return l;
	}

	@Override
	public Long rightPush(K key, V value) {
		long l = this.bridge.rpush(rawKey(key), rawValue(value));
		return l;
	}

	@Override
	public Long rightPushAll(K key, V... values) {
		long l = this.bridge.rpush(rawKey(key), asList(values));
		return l;
	}

	@Override
	public Long rightPushAll(K key, Collection<V> values) {
		long l = this.bridge.rpush(rawKey(key), asList(values));
		return l;
	}

	@Override
	public Long rightPushIfPresent(K key, V value) {
		long l = this.bridge.rpushx(rawKey(key), rawValue(value));
		return l;
	}

	@Override
	public Long rightPush(K key, V pivot, V value) {
		long l = this.bridge.linsert(rawKey(key), DryRedisInsertOrder.AFTER, rawValue(pivot), rawValue(value));
		return l;
	}

	@Override
	public void set(K key, long index, V value) {
		this.bridge.lset(rawKey(key), index, rawValue(value));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long remove(K key, long count, Object value) {
	    long l;
		if(value instanceof byte[]) {
			l = this.bridge.lrem(rawKey(key), count, (byte[]) value);
		}
		
		l = this.bridge.lrem(rawKey(key), count, rawValue((V) value));
		return l;
	}

	@Override
	public V index(K key, long index) {
		return this.asValue(this.bridge.lindex(rawKey(key), index));
	}

	@Override
	public V leftPop(K key) {
		return this.asValue(this.bridge.lpop(rawKey(key)));
	}

	@Override
	public V leftPop(K key, long timeout, TimeUnit unit) {
		return this.asValue(this.bridge.blpop(rawKey(key), this.asIntSeconds(timeout, unit)));
	}

	@Override
	public V rightPop(K key) {
		return this.asValue(this.bridge.rpop(rawKey(key)));
	}

	@Override
	public V rightPop(K key, long timeout, TimeUnit unit) {
		return this.asValue(this.bridge.brpop(rawKey(key), this.asIntSeconds(timeout, unit)));
	}

	@Override
	public V rightPopAndLeftPush(K sourceKey, K destinationKey) {
		return this.asValue(this.bridge.rpoplpush(rawKey(sourceKey), rawKey(destinationKey)));
	}

	@Override
	public V rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout, TimeUnit unit) {
		return this.asValue(this.bridge.brpoplpush(rawKey(sourceKey), rawKey(destinationKey), this.asIntSeconds(timeout, unit)));
	}

	@Override
	public RedisOperations<K, V> getOperations() {
		return this.template;
	}

}
