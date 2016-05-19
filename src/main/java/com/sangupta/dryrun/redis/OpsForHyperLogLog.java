package com.sangupta.dryrun.redis;

import org.springframework.data.redis.core.HyperLogLogOperations;

public class OpsForHyperLogLog<K, V> extends AbstractRedisOperations<K, V> implements HyperLogLogOperations<K, V> {

	public OpsForHyperLogLog(DryRunRedisTemplate<K, V> template) {
		super(template);
	}

	@Override
	public Long add(K key, V... values) {
		return null;
	}

	@Override
	public Long size(K... keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long union(K destination, K... sourceKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(K key) {
		// TODO Auto-generated method stub
		
	}

}
