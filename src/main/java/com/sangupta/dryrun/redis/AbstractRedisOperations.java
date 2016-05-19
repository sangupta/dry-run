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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.sangupta.dryredis.DryRedis;

/**
 * Common/Utility operations for {@link DryRunRedisTemplate}.
 * 
 * @author sangupta
 *
 * @param <K>
 * @param <V>
 */
abstract class AbstractRedisOperations<K, V> {

	final DryRunRedisTemplate<K, V> template;
	
	final DryRedis dryRedis;
	
	final DryRedisBridge bridge;

	AbstractRedisOperations(DryRunRedisTemplate<K, V> template) {
		this.template = template;
		this.dryRedis = template.dryRedis;
		this.bridge = new DryRedisBridge(dryRedis);
	}

	byte[] rawKey(K key) {
		return this.template.keySerializer.serialize(key);
	}
	
	byte[] rawValue(V value) {
		return this.template.valueSerializer.serialize(value);
	}
	
	boolean asBoolean(int value) {
	    if(value == 1) {
	        return true;
	    }
	    
	    return false;
	}

	@SuppressWarnings("unchecked")
	List<byte[]> rawValues(Object... values) {
		final List<byte[]> list = new ArrayList<byte[]>();
		for (Object value : values) {
			list.add(rawValue((V) value));
		}
		
		return list;
	}

	byte[] getString(String str) {
		return this.template.stringSerializer.serialize(str);
	}
	
	V asValue(byte[] bytes) {
		return this.template.valueSerializer.deserialize(bytes);
	}
	
	V asValue(String value) {
	    if(value == null) {
	        return null;
	    }
	    
	    byte[] bytes = value.getBytes();
        return this.template.valueSerializer.deserialize(bytes);
    }

	List<V> asValuesList(List<String> bytes) {
		List<V> result = new ArrayList<V>();
		for(String str : bytes) {
		    if(str == null) {
		        continue;
		    }
		    
		    byte[] bs = str.getBytes();
			result.add(this.template.valueSerializer.deserialize(bs));
		}
		
		return result;
	}
	
	byte[][] asArray(V... values) {
		byte[][] array = new byte[values.length][];
		int index = 0;
		for(V value : values) {
			array[index++] = rawValue(value);
		}
		
		return array;
	}
	
	byte[][] asKeyArray(Collection<K> values) {
        byte[][] array = new byte[values.size()][];
        int index = 0;
        for(K value : values) {
            array[index++] = rawKey(value);
        }
        
        return array;
    }
	
	byte[][] asArray(Collection<V> values) {
		byte[][] array = new byte[values.size()][];
		int index = 0;
		for(V value : values) {
			array[index++] = rawValue(value);
		}
		
		return array;
	}
	
	List<byte[]> asList(Collection<V> values) {
        List<byte[]> list = new ArrayList<byte[]>();
        for(V value : values) {
            list.add(rawValue(value));
        }
        
        return list;
    }
	
	List<byte[]> asList(V... values) {
        List<byte[]> list = new ArrayList<byte[]>();
        for(V value : values) {
            list.add(rawValue(value));
        }
        
        return list;
    }

	int asIntSeconds(long timeout, TimeUnit unit) {
		Long value = unit.toSeconds(timeout);
		return value.intValue();
	}
	
	Set<V> asValuesSet(Collection<String> sdiff) {
        if(sdiff == null) {
            return null;
        }
        
        Set<V> set = new HashSet<V>();
        if(sdiff.isEmpty()) {
            return set;
        }
        
        for(String value : sdiff) {
            if(value == null) {
                continue;
            }
            
            byte[] bytes = value.getBytes();
            set.add(this.asValue(bytes));
        }
        
        return set;
    }

	byte[][] rawKeys(K key, Collection<K> keys) {
		final byte[][] rawKeys = new byte[keys.size() + (key != null ? 1 : 0)][];

		int i = 0;

		if(key != null) {
			rawKeys[i++] = rawKey(key);
		}

		for(K k : keys) {
			rawKeys[i++] = rawKey(k);
		}

		return rawKeys;
	}
	
	byte[][] rawKeys(K key, K otherKey) {
		final byte[][] rawKeys = new byte[2][];

		int i = 0;

		if(key != null) {
			rawKeys[i++] = rawKey(key);
		}

		if(otherKey != null) {
			rawKeys[i++] = rawKey(otherKey);
		}

		return rawKeys;
	}
	
	int asInt(long value) {
		return new Long(value).intValue();
	}
	
}
