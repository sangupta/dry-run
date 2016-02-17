package com.sangupta.dryrun.redis;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;

class OpsForSet<K, V> extends AbstractRedisOperations<K, V> implements SetOperations<K, V> {
	
	public OpsForSet(DryRunRedisTemplate<K, V> template) {
		super(template);
	}

	@Override
	public Set<V> difference(K key, K otherKey) {
		return null;
	}

	@Override
	public Set<V> difference(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long differenceAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long differenceAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> intersect(K key, K otherKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> intersect(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long intersectAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long intersectAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> union(K key, K otherKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> union(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long unionAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long unionAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long add(K key, V... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isMember(K key, Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> members(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean move(K key, V value, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V randomMember(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> distinctRandomMembers(K key, long count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<V> randomMembers(K key, long count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long remove(K key, Object... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V pop(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long size(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RedisOperations<K, V> getOperations() {
		return this.template;
	}

	@Override
	public Cursor<V> scan(K key, ScanOptions options) {
		// TODO Auto-generated method stub
		return null;
	}

}
