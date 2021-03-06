package com.sangupta.dryrun;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.sangupta.dryredis.DryRedis;
import com.sangupta.dryrun.redis.DryRunRedisTemplate;

public class TestDryRunRedisTemplate {
	
	@Test
	public void testRedisTemplate() {
		DryRedis dryRedis = DryRedis.getDatabase("test");
		RedisTemplate<String, String> template = new DryRunRedisTemplate<String, String>(dryRedis);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		
		// test now
		final String key = "test";
		final String value = "value";
		
		Assert.assertFalse(template.hasKey(key));
		template.opsForValue().set(key, value);
		Assert.assertTrue(template.hasKey(key));
		Assert.assertEquals(value, template.opsForValue().get(key));
		template.delete(key);
		Assert.assertFalse(template.hasKey(key));
	}

}
