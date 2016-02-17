package com.sangupta.dryrun.mongo;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoTypeMapper;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.util.TypeInformation;

import com.mongodb.DBObject;
import com.mongodb.DBRef;

public class DryRunMongoConverter implements MongoConverter {

	@Override
	public ConversionService getConversionService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> getMappingContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <R> R read(Class<R> arg0, DBObject arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(Object arg0, DBObject arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object convertToMongoType(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object convertToMongoType(Object obj, TypeInformation<?> typeInformation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBRef toDBRef(Object object, MongoPersistentProperty referingProperty) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MongoTypeMapper getTypeMapper() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
