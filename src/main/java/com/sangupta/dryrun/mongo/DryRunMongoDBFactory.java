package com.sangupta.dryrun.mongo;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.MongoDbFactory;

import com.mongodb.DB;

public class DryRunMongoDBFactory implements MongoDbFactory {

	@Override
	public DB getDb() throws DataAccessException {
		return null;
	}

	@Override
	public DB getDb(String dbName) throws DataAccessException {
		return null;
	}

	@Override
	public PersistenceExceptionTranslator getExceptionTranslator() {
		return null;
	}

}
