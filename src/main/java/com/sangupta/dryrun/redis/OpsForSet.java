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
import java.util.Set;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;

import com.sangupta.jerry.exceptions.NotImplementedException;

/**
 * Set operations for {@link DryRunRedisTemplate}.
 * 
 * @author sangupta
 *
 * @param <K>
 * @param <V>
 */
class OpsForSet<K, V> extends AbstractRedisOperations<K, V> implements SetOperations<K, V> {
	
	public OpsForSet(DryRunRedisTemplate<K, V> template) {
		super(template);
	}

	@Override
	public Set<V> difference(K key, K otherKey) {
		return this.asValuesSet(this.bridge.sdiff(rawKey(key), rawKey(otherKey)));
	}

	@Override
	public Set<V> difference(K key, Collection<K> otherKeys) {
		return this.asValuesSet(this.bridge.sdiff(rawKey(key), this.asKeyArray(otherKeys)));
	}

	@Override
	public Long differenceAndStore(K key, K otherKey, K destKey) {
		long l = this.bridge.sdiffstore(rawKey(destKey), rawKey(key), rawKey(otherKey));
		return l;
	}

	@Override
	public Long differenceAndStore(K key, Collection<K> otherKeys, K destKey) {
		long l = this.bridge.sdiffstore(rawKey(destKey), rawKey(key), this.asKeyArray(otherKeys));
		return l;
	}

	@Override
	public Set<V> intersect(K key, K otherKey) {
		return this.asValuesSet(this.bridge.sinter(rawKey(key), rawKey(otherKey)));
	}

	@Override
	public Set<V> intersect(K key, Collection<K> otherKeys) {
		return this.asValuesSet(this.bridge.sinter(rawKey(key), this.asKeyArray(otherKeys)));
	}

	@Override
	public Long intersectAndStore(K key, K otherKey, K destKey) {
		long l = this.bridge.sinterstore(rawKey(destKey), rawKey(key), this.rawKey(otherKey));
		return l;
	}

	@Override
	public Long intersectAndStore(K key, Collection<K> otherKeys, K destKey) {
		long l = this.bridge.sinterstore(rawKey(destKey), rawKey(key), this.asKeyArray(otherKeys));
		return l;
	}

	@Override
	public Set<V> union(K key, K otherKey) {
		return this.asValuesSet(this.bridge.sunion(rawKey(key), rawKey(otherKey)));
	}

	@Override
	public Set<V> union(K key, Collection<K> otherKeys) {
		return this.asValuesSet(this.bridge.sunion(rawKey(key), this.asKeyArray(otherKeys)));
	}

	@Override
	public Long unionAndStore(K key, K otherKey, K destKey) {
		long l = this.bridge.sunionstore(rawKey(destKey), rawKey(key), rawKey(otherKey));
		return l;
	}

	@Override
	public Long unionAndStore(K key, Collection<K> otherKeys, K destKey) {
		long l = this.bridge.sunionstore(rawKey(destKey), rawKey(key), this.asKeyArray(otherKeys));
		return l;
	}

	@Override
	public Long add(K key, V... values) {
		long l = this.bridge.sadd(rawKey(key), this.asList(values));
		return l;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean isMember(K key, Object value) {
		return this.asBoolean(this.bridge.sismember(rawKey(key), rawValue((V) value)));
	}

	@Override
	public Set<V> members(K key) {
		return this.asValuesSet(this.bridge.smembers(rawKey(key)));
	}

	@Override
	public Boolean move(K key, V value, K destKey) {
		return this.asBoolean(this.bridge.smove(rawKey(destKey), rawKey(destKey), rawValue(value)));
	}

	@Override
	public V randomMember(K key) {
		return this.asValue(this.bridge.srandmember(rawKey(key)));
	}

	@Override
	public Set<V> distinctRandomMembers(K key, long count) {
		return this.asValuesSet(this.bridge.srandmember(rawKey(key), this.asInt(count)));
	}

	@Override
	public List<V> randomMembers(K key, long count) {
		return this.asValuesList(this.bridge.srandmember(rawKey(key), this.asInt(-count)));
	}

	@Override
	public Long remove(K key, Object... values) {
		long l = this.bridge.srem(rawKey(key), rawValues(values));
		return l;
	}

	@Override
	public V pop(K key) {
		return this.asValue(this.bridge.spop(rawKey(key)));
	}

	@Override
	public Long size(K key) {
		long l = this.bridge.scard(rawKey(key));
		return l;
	}

	@Override
	public RedisOperations<K, V> getOperations() {
		return this.template;
	}

	@Override
	public Cursor<V> scan(K key, ScanOptions options) {
		// TODO: fix this
		throw new NotImplementedException("Not yet implemented");
	}

}
