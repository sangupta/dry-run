dryrun
======

[![Build Status](https://travis-ci.org/sangupta/dryrun.svg?branch=master)](https://travis-ci.org/sangupta/dryrun)
[![Coverage Status](https://coveralls.io/repos/github/sangupta/dryrun/badge.svg?branch=master)](https://coveralls.io/github/sangupta/dryrun?branch=master)
[![Maven Version](https://maven-badges.herokuapp.com/maven-central/com.sangupta/dryrun/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.sangupta/dryrun)

**dryrun** is a collection of already-mocked classes that serve pretty useful during testing. For example
unit testing code with `RedisTemplate` usually involves spinning a Redis instance. Our `DryRunRedisTemplate`
uses the `MockJedis` framework to connect to an in-memory Redis, that connects via API calls and not via
the usual connection.

Features
--------

* Mocked implementation to `RedisTemplate` using `MockJedis`

Currently only the operations for values are available. Operations on sets/lists/... are still pending.

Usage
-----

To test code that uses `RedisTemplate` as a service, just inject the mocked template as:

```java
// create a MockJedis instance
MockJedis jedis = new MockJedis("mock-jedis");

// create an instance of RedisTemplate
RedisTemplate<String, byte[]> template = new DryRunRedisTemplate<String, byte[]>(jedis);

// must specify the key and value serializers
template.setKeySerializer(new StringRedisSerializer());
template.setValueSerializer(new RedisSerializer<byte[]>() {

	@Override
	public byte[] serialize(byte[] t) throws SerializationException {
		return t;
	}

	@Override
	public byte[] deserialize(byte[] bytes) throws SerializationException {
		return bytes;
	}
});

// inject in your service
MyTestableService service = new DefaultMyTestableServiceImpl();
service.setRedisTemplate(redisTemplate);
```

Downloads
---------

The library can be downloaded from Maven Central using:

```xml
<dependency>
    <groupId>com.sangupta</groupId>
    <artifactId>dryrun</artifactId>
    <version>0.1.0</version>
</dependency>
```

Release Notes
-------------

**Development Snapshot**

* Partial RedisTemplate methods
* RedisTemplate.opsForValue() implemented completely

Versioning
----------

For transparency and insight into our release cycle, and for striving to maintain backward compatibility, 
`dryrun` will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.

License
-------
	
```
dryrun - Mocked classes for unit testing
Copyright (c) 2016, Sandeep Gupta

http://sangupta.com/projects/dryrun

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

