package com.sangupta.dryrun.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sangupta.dryredis.DryRedis;
import com.sangupta.dryredis.support.DryRedisGeoUnit;
import com.sangupta.dryredis.support.DryRedisInsertOrder;
import com.sangupta.dryredis.support.DryRedisRangeArgument;
import com.sangupta.dryredis.support.DryRedisSetAggregationType;
import com.sangupta.jerry.exceptions.NotImplementedException;

/**
 * Provides a facade to all command operations of a particular group
 * of commands as defined by REDIS.
 * 
 * @author sangupta
 *
 */
public class DryRedisBridge {
    
    final DryRedis dryRedis;
    
    public DryRedisBridge(DryRedis dryRedis) {
        this.dryRedis = dryRedis;
    }
    
    // Convenience functions
    
    private Map<String, String> fromMap(Map<byte[], byte[]> fieldValues) {
        Map<String, String> map = new HashMap<String, String>();
        if(fieldValues == null || fieldValues.isEmpty()) {
            return map;
        }
        
        for(Entry<byte[], byte[]> entry : fieldValues.entrySet()) {
            map.put(toKey(entry.getKey()), toKey(entry.getValue()));
        }
        
        return map;
    }

    private String toKey(byte[] bytes) {
        return new String(bytes);
    }
    
    private String[] toKeys(byte[][] keys) {
        if(keys == null || keys.length == 0) {
            return new String[] { };
        }
        
        String[] result = new String[keys.length];
        for(int index = 0; index < keys.length; index++) {
            result[index] = new String(keys[index]);
        }
        
        return result;
    }
    
    private List<String> toKeys(Collection<byte[]> keys) {
        List<String> list = new ArrayList<String>();
        
        if(keys == null || keys.isEmpty()) {
            return list;
        }
        
        for(byte[] bytes : keys) {
            list.add(new String(bytes));
        }
        
        return list;
    }
    
    private Set<String> toKeySet(Collection<byte[]> keys) {
        Set<String> list = new HashSet<String>();
        
        if(keys == null || keys.isEmpty()) {
            return list;
        }
        
        for(byte[] bytes : keys) {
            list.add(new String(bytes));
        }
        
        return list;
    }

    // GEO commands follow

    public int geoadd(byte[] bytes, double longitude, double latitude, String member) {
        return this.dryRedis.geoadd(toKey(bytes), longitude, latitude, member);
    }
    
    public String geohash(byte[] bytes, String member) {
        return this.dryRedis.geohash(toKey(bytes), member);
    }
    
    public double[] geopos(byte[] bytes, String member) {
        return this.dryRedis.geopos(toKey(bytes), member);
    }
    
    public Double geodist(byte[] key, byte[] member1, byte[] member2, DryRedisGeoUnit unit) {
        return this.dryRedis.geodist(toKey(key), toKey(member1), toKey(member2), unit);
    }
    
    public List<String> georadius(byte[] bytes, double longitude, double latitude, double radius, DryRedisGeoUnit unit) {
        return this.dryRedis.georadius(toKey(bytes), longitude, latitude, radius, unit);
    }
    
    public List<String> georadius(byte[] bytes, double longitude, double latitude, double radius, DryRedisGeoUnit unit, boolean withCoordinates, boolean withDistance, boolean withHash, int count) {
        return this.dryRedis.georadius(toKey(bytes), longitude, latitude, radius, unit, withCoordinates, withDistance, withHash, count); 
    }
    
    public List<String> georadiusbymember(byte[] bytes, byte[] member, double radius, DryRedisGeoUnit unit) {
        return this.dryRedis.georadiusbymember(toKey(bytes), toKey(member), radius, unit);
    }
    
    public List<String> georadiusbymember(byte[] bytes, byte[] member, double radius, DryRedisGeoUnit unit, boolean withCoordinates, boolean withDistance, boolean withHash, int count) {
        return this.dryRedis.georadiusbymember(toKey(bytes), toKey(member), radius, unit, withCoordinates, withDistance, withHash, count);
    }

    // HASH commands follow
    
    public int hdel(byte[] bytes, String field) {
        return this.dryRedis.hdel(toKey(bytes), field);
    }
    
    public int hdel(byte[] bytes, List<String> fields) {
        return this.dryRedis.hdel(toKey(bytes), fields);
    }
    
    public int hexists(byte[] bytes, String field) {
        return this.dryRedis.hexists(toKey(bytes), field);
    }
    
    public Object hget(byte[] bytes, String field) {
        return this.dryRedis.hget(toKey(bytes), field);
    }
    
    public List<String> hgetall(byte[] bytes) {
        return this.dryRedis.hgetall(toKey(bytes));
    }
    
    public List<String> hkeys(byte[] bytes) {
        return this.dryRedis.hkeys(toKey(bytes));
    }
    
    public int hlen(byte[] bytes) {
        return this.dryRedis.hlen(toKey(bytes));
    }
    
    public int hset(byte[] bytes, byte[] field, byte[] value) {
        return this.dryRedis.hset(toKey(bytes), toKey(field), toKey(value));
    }
    
    public int hsetnx(byte[] bytes, byte[] field, byte[] value) {
        return this.dryRedis.hsetnx(toKey(bytes), toKey(field), toKey(value));
    }
    
    public int hstrlen(byte[] bytes, byte[] field) {
        return this.dryRedis.hstrlen(toKey(bytes), toKey(field));
    }
    
    public List<String> hvals(byte[] bytes) {
        return this.dryRedis.hvals(toKey(bytes));
    }
    
    public long hincrby(byte[] bytes, byte[] field, long increment) {
        return this.dryRedis.hincrby(toKey(bytes), toKey(field), increment);
    }
    
    public double hincrbyfloat(byte[] bytes, byte[] field, double increment) {
        return this.dryRedis.hincrbyfloat(toKey(bytes), toKey(field), increment);
    }
    
    public List<String> hmget(byte[] bytes, List<byte[]> fields) {
        return this.dryRedis.hmget(toKey(bytes), toKeys(fields));
    }
    
    public String hmset(byte[] bytes, Map<byte[], byte[]> fieldValues) {
        return this.dryRedis.hmset(toKey(bytes), fromMap(fieldValues));
    }
    
    // HYPERLOGLOG commands
    
    public int pfadd(byte[] bytes) {
        return this.dryRedis.pfadd(toKey(bytes));
    }
    
    public int pfadd(byte[] bytes, byte[] element) {
        return this.dryRedis.pfadd(toKey(bytes), toKey(element));
    }
    
    public long pfcount(byte[] bytes) {
        return this.dryRedis.pfcount(toKey(bytes));
    }
    
    public String pfmerge(byte[] destinationBytes, List<byte[]> keysBytes) {
        return this.dryRedis.pfmerge(toKey(destinationBytes), toKeys(keysBytes));
    }

    // LIST commands
    
    public String blpop(byte[] bytes, int maxSecondsToBlock) {
        return this.dryRedis.blpop(toKey(bytes), maxSecondsToBlock);
    }
    
    public String brpop(byte[] bytes, int maxSecondsToBlock) {
        return this.dryRedis.brpop(toKey(bytes), maxSecondsToBlock);
    }
    
    public String brpoplpush(byte[] sourceBytes, byte[] destinationBytes, int maxSecondsToBlock) {
        return this.dryRedis.brpoplpush(toKey(sourceBytes), toKey(destinationBytes), maxSecondsToBlock);
    }
    
    public String lindex(byte[] bytes, long index) {
        return this.dryRedis.lindex(toKey(bytes), Long.valueOf(index).intValue());
    }
    
    public int linsert(byte[] bytes, DryRedisInsertOrder order, byte[] pivot, byte[] value) {
        return this.dryRedis.linsert(toKey(bytes), order, toKey(pivot), toKey(value));
    }
    
    public int llen(byte[] bytes) {
        return this.dryRedis.llen(toKey(bytes));
    }
    
    public String lpop(byte[] bytes) {
        return this.dryRedis.lpop(toKey(bytes));
    }
    
    public int lpush(byte[] bytes, byte[] value) {
        return this.dryRedis.lpush(toKey(bytes), toKey(value));
    }
    
    public int lpush(byte[] bytes, List<byte[]> values) {
        return this.dryRedis.lpush(toKey(bytes), toKeys(values));
    }
    
    public int lpushx(byte[] bytes, List<byte[]> values) {
        return this.dryRedis.lpushx(toKey(bytes), toKeys(values));
    }
    
    public List<String> lrange(byte[] bytes, long start, long stop) {
        return this.dryRedis.lrange(toKey(bytes), Long.valueOf(start).intValue(), Long.valueOf(stop).intValue());
    }
    
    public int lrem(byte[] bytes, long count, byte[] value) {
        return this.dryRedis.lrem(toKey(bytes), Long.valueOf(count).intValue(), toKey(value));
    }
    
    public String lset(byte[] bytes, long index, byte[] value) {
        return this.dryRedis.lset(toKey(bytes), Long.valueOf(index).intValue(), toKey(value));
    }
    
    public String ltrim(byte[] bytes, long start, long stop) {
        return this.dryRedis.ltrim(toKey(bytes), Long.valueOf(start).intValue(), Long.valueOf(stop).intValue());
    }
    
    public String rpop(byte[] bytes) {
        return this.dryRedis.rpop(toKey(bytes));
    }
    
    public String rpoplpush(byte[] sourceBytes, byte[] destinationBytes) {
        return this.dryRedis.rpoplpush(toKey(sourceBytes), toKey(destinationBytes));
    }
    
    public int rpush(byte[] bytes, byte[] value) {
        return this.dryRedis.rpush(toKey(bytes), toKey(value));
    }
    
    public int rpushx(byte[] bytes, byte[] value) {
        return this.dryRedis.rpushx(toKey(bytes), toKey(value));
    }
    
    public int rpush(byte[] bytes, List<byte[]> values) {
        return this.dryRedis.rpush(toKey(bytes), toKeys(values));
    }
    
    public int lpushx(byte[] bytes, byte[] value) {
        return this.dryRedis.lpushx(toKey(bytes), toKey(value));
    }
    
    // SET commands
    
    public int sadd(byte[] bytes, byte[] value) {
        return this.dryRedis.sadd(toKey(bytes), toKey(value));
    }
    
    public int sadd(byte[] bytes, List<byte[]> values) {
        return this.dryRedis.sadd(toKey(bytes), toKeys(values));
    }
    
    public int scard(byte[] bytes) {
        return this.dryRedis.scard(toKey(bytes));
    }
    
    public Set<String> sdiff(byte[] bytes, byte[]... otherKeys) {
        return this.dryRedis.sdiff(toKey(bytes), toKeys(otherKeys));
    }
    
    public int sdiffstore(byte[] destinationBytes, byte[] bytes, byte[]... otherKeys) {
        return this.dryRedis.sdiffstore(toKey(destinationBytes), toKey(bytes), toKeys(otherKeys));
    }
    
    public Set<String> sinter(byte[] bytes, byte[]... otherKeys) {
        return this.dryRedis.sinter(toKey(bytes), toKeys(otherKeys));
    }
    
    public int sinterstore(byte[] destinationBytes, byte[] keyBytes, byte[]... otherKeys) {
        return this.dryRedis.sinterstore(toKey(destinationBytes), toKey(keyBytes), toKeys(otherKeys));
    }
    
    public int sismember(byte[] bytes, byte[] value) {
        return this.dryRedis.sismember(toKey(bytes), toKey(value));
    }
    
    public Set<String> smembers(byte[] bytes) {
        return this.dryRedis.smembers(toKey(bytes));
    }
    
    public int smove(byte[] sourceBytes, byte[] destinationBytes, byte[] value) {
        return this.dryRedis.smove(toKey(sourceBytes), toKey(destinationBytes), toKey(value));
    }
    
    public String spop(byte[] bytes) {
        return this.dryRedis.spop(toKey(bytes));
    }
    
    public List<String> spop(byte[] bytes, int count) {
        return this.dryRedis.spop(toKey(bytes), count);
    }
    
    public String srandmember(byte[] bytes) {
        return this.dryRedis.srandmember(toKey(bytes));
    }

    public List<String> srandmember(byte[] bytes, int count) {
        return this.dryRedis.srandmember(toKey(bytes), count);
    }
    
    public int srem(byte[] bytes, byte[] value) {
        return this.dryRedis.srem(toKey(bytes), toKey(value));
    }
    
    public int srem(byte[] bytes, List<byte[]> values) {
        return this.dryRedis.srem(toKey(bytes), toKeys(values));
    }
    
    public Set<String> sunion(byte[] bytes, byte[]... otherKeys) {
        return this.dryRedis.sunion(toKey(bytes), toKeys(otherKeys));
    }
    
    public int sunionstore(byte[] destinationBytes, byte[] keyBytes, byte[]... otherKeys) {
        return this.dryRedis.sunionstore(toKey(destinationBytes), toKey(keyBytes), toKeys(otherKeys));
    }
    
    public List<String> sscan(byte[] bytes, int cursor) {
        return this.dryRedis.sscan(toKey(bytes), cursor);
    }
    
    // SORTED-SET commands
    
    public int zadd(byte[] bytes, double score, byte[] member) {
        return this.dryRedis.zadd(toKey(bytes), score, toKey(member));
    }

    public long zcard(byte[] bytes) {
        return this.dryRedis.zcard(toKey(bytes));
    }

    public long zcount(byte[] bytes, double min, double max) {
        return this.dryRedis.zcount(toKey(bytes), min, max);
    }

    public double zincrby(byte[] bytes, double increment, byte[] member) {
        return this.dryRedis.zincrby(toKey(bytes), increment, toKey(member));
    }

    public Integer zrank(byte[] bytes, byte[] member) {
        return this.dryRedis.zrank(toKey(bytes), toKey(member));
    }

    public Integer zrevrank(byte[] bytes, byte[] member) {
        return this.dryRedis.zrevrank(toKey(bytes), toKey(member));
    }

    public int zrem(byte[] bytes, byte[] member) {
        return this.dryRedis.zrem(toKey(bytes), toKey(member));
    }

    public int zrem(byte[] bytes, Set<byte[]> members) {
        return this.dryRedis.zrem(toKey(bytes), toKeySet(members));
    }

    public Double zscore(byte[] bytes, byte[] member) {
        return this.dryRedis.zscore(toKey(bytes), toKey(member));
    }

    public List<String> zrange(byte[] bytes, int start, int stop, boolean withScores) {
        return this.dryRedis.zrange(toKey(bytes), start, stop, withScores);
    }

    public List<String> zrangebylex(byte[] bytes, String min, String max) {
        return this.dryRedis.zrangebylex(toKey(bytes), min, max);
    }

    public List<String> zrangebylex(byte[] bytes, DryRedisRangeArgument min, DryRedisRangeArgument max) {
        return this.dryRedis.zrangebylex(toKey(bytes), min, max);
    }

    public List<String> zrevrangebylex(byte[] bytes, String min, String max) {
        return this.dryRedis.zrevrangebylex(toKey(bytes), max, min);
    }

    public List<String> zrevrangebylex(byte[] bytes, DryRedisRangeArgument max, DryRedisRangeArgument min) {
        return this.dryRedis.zrevrangebylex(toKey(bytes), min, max);
    }

    public int zlexcount(byte[] bytes, String min, String max) {
        return this.dryRedis.zlexcount(toKey(bytes), min, max);
    }
    
    public int zlexcount(byte[] bytes, DryRedisRangeArgument min, DryRedisRangeArgument max) {
        return this.dryRedis.zlexcount(toKey(bytes), min, max);
    }

    public int zremrangebylex(byte[] bytes, byte[] min, byte[] max) {
        return this.dryRedis.zremrangebylex(toKey(bytes), toKey(min), toKey(max));
    }

    public int zremrangebylex(byte[] bytes, DryRedisRangeArgument min, DryRedisRangeArgument max) {
        return this.dryRedis.zremrangebylex(toKey(bytes), min, max);
    }

    public int zremrangebyrank(byte[] bytes, int start, int stop) {
        return this.dryRedis.zremrangebyrank(toKey(bytes), start, stop);
    }

    public int zremrangebyscore(byte[] bytes, DryRedisRangeArgument min, DryRedisRangeArgument max) {
        return this.dryRedis.zremrangebyscore(toKey(bytes), min, max);
    }

    public List<String> zrevrange(byte[] bytes, int start, int stop, boolean withScores) {
        return this.dryRedis.zrevrange(toKey(bytes), start, stop, withScores);
    }

    public int zinterstore(byte[] destinationBytes, List<byte[]> keysList) {
        if(keysList == null) {
            return 0;
        }
        
        return this.dryRedis.zinterstore(toKey(destinationBytes), toKeys(keysList));
    }

    public int zinterstore(byte[] destinationBytes, List<byte[]> keysList, double[] weights, DryRedisSetAggregationType aggregation) {
        if(keysList == null) {
            return 0;
        }

        return this.dryRedis.zinterstore(toKey(destinationBytes), toKeys(keysList), weights, aggregation);
    }

    public int zunionstore(byte[] destinationBytes, List<byte[]> keysList) {
        if(keysList == null) {
            return 0;
        }
        
        return this.dryRedis.zunionstore(toKey(destinationBytes), toKeys(keysList));
    }

    public int zunionstore(byte[] destinationBytes, List<byte[]> keysList, double[] weights, DryRedisSetAggregationType aggregation) {
        if(keysList == null) {
            return 0;
        }
        
        return this.dryRedis.zunionstore(toKey(destinationBytes), toKeys(keysList), weights, aggregation);
    }
    
    // STRING commands

    public int append(byte[] bytes, byte[] value) {
        return this.dryRedis.append(toKey(bytes), toKey(value));
    }
    
    public long incr(byte[] bytes) {
        return this.dryRedis.incr(toKey(bytes));
    }
    
    public long incrby(byte[] bytes, long delta) {
        return this.dryRedis.incrby(toKey(bytes), delta);
    }
    
    public double incrbyfloat(byte[] bytes, double delta) {
        return this.dryRedis.incrbyfloat(toKey(bytes), delta);
    }
    
    public List<String> mget(byte[][] keysArray) {
        if(keysArray == null) {
            return null;
        }
        
        return this.dryRedis.mget(toKeys(keysArray));
    }
    
    public List<String> mget(Collection<byte[]> keysList) {
        if(keysList == null) {
            return null;
        }
        
        return this.dryRedis.mget(toKeys(keysList));
    }
    
    public String set(byte[] bytes, byte[] value) {
        return this.dryRedis.set(toKey(bytes), toKey(value));
    }
    
    public String setnx(byte[] bytes, byte[] value) {
        return this.dryRedis.setnx(toKey(bytes), toKey(value));
    }
    
    public String setxx(byte[] bytes, byte[] value) {
        return this.dryRedis.setxx(toKey(bytes), toKey(value));
    }
    
    public String getrange(byte[] bytes, long start, long end) {
        return this.dryRedis.getrange(toKey(bytes), Long.valueOf(start).intValue(), Long.valueOf(end).intValue());
    }
    
    public int setrange(byte[] bytes, long offset, byte[] value) {
        return this.dryRedis.setrange(toKey(bytes), Long.valueOf(offset).intValue(), toKey(value));
    }
    
    public long bitcount(byte[] bytes, long start, long end) {
        return this.dryRedis.bitcount(toKey(bytes), Long.valueOf(start).intValue(), Long.valueOf(end).intValue());
    }
    
    public long decr(byte[] bytes) {
        return this.dryRedis.decr(toKey(bytes));
    }
    
    public long decrby(byte[] bytes, long delta) {
        return this.dryRedis.decrby(toKey(bytes), delta);
    }
    
    public String get(byte[] bytes) {
        return this.dryRedis.get(toKey(bytes));
    }
    
    public int strlen(byte[] bytes) {
        return this.dryRedis.strlen(toKey(bytes));
    }
    
    public String getset(byte[] bytes, byte[] value) {
       return this.dryRedis.getset(toKey(bytes), toKey(value));
    }
    
    public long bitcount(byte[] bytes) {
        return this.dryRedis.bitcount(toKey(bytes));
    }

    public boolean exists(byte[] key) {
        int exists = this.dryRedis.exists(toKey(key));
        if(exists == 0) {
            return false;
        }
        
        return true;
    }

    public void del(byte[] key) {
        this.dryRedis.del(toKey(key));
    }

    public boolean setbit(byte[] rawKey, long offset, boolean value) {
        throw new NotImplementedException();
    }

    public boolean getbit(byte[] rawKey, long offset) {
        throw new NotImplementedException();
    }

    public void psetex(byte[] rawKey, int millis, byte[] rawValue) {
        throw new NotImplementedException();
    }

    public void setex(byte[] rawKey, int intValue, byte[] rawValue) {
        throw new NotImplementedException();        
    }

}
