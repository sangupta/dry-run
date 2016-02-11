package com.sangupta.dryrun.mongo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

public class DryRunGridFSTemplate implements GridFsOperations {
	
	private final Map<String, DryRunGridFSDBFile> files = new HashMap<String, DryRunGridFSDBFile>();  

	@Override
	public ClassLoader getClassLoader() {
		return this.getClassLoader();
	}

	@Override
	public GridFSFile store(InputStream content, String fileName) {
		DryRunGridFSDBFile file = new DryRunGridFSDBFile(fileName, readFully(content));
		this.files.put(fileName, file);
		return file;
	}

	@Override
	public GridFSFile store(InputStream content, Object metadata) {
		String fileName = UUID.randomUUID().toString();
		
		DryRunGridFSDBFile file = new DryRunGridFSDBFile(fileName, readFully(content));
		file.setMetaData(metadata);
		
		this.files.put(fileName, file);
		return file;
	}

	@Override
	public GridFSFile store(InputStream content, DBObject metadata) {
		String fileName = UUID.randomUUID().toString();
		
		DryRunGridFSDBFile file = new DryRunGridFSDBFile(fileName, readFully(content));
		file.setMetaData(metadata);
		
		this.files.put(fileName, file);
		return file;
	}

	@Override
	public GridFSFile store(InputStream content, String fileName, String contentType) {
		DryRunGridFSDBFile file = new DryRunGridFSDBFile(fileName, readFully(content));
		file.setContentType(contentType);
		
		return file;
	}

	@Override
	public GridFSFile store(InputStream content, String fileName, Object metadata) {
		DryRunGridFSDBFile file = new DryRunGridFSDBFile(fileName, readFully(content));
		file.setMetaData(metadata);
		
		return file;
	}

	@Override
	public GridFSFile store(InputStream content, String fileName, String contentType, Object metadata) {
		DryRunGridFSDBFile file = new DryRunGridFSDBFile(fileName, readFully(content));
		file.setMetaData(metadata);
		file.setContentType(contentType);
		
		return file;
	}

	@Override
	public GridFSFile store(InputStream content, String fileName, DBObject metadata) {
		DryRunGridFSDBFile file = new DryRunGridFSDBFile(fileName, readFully(content));
		file.setMetaData(metadata);
		
		return file;
	}

	@Override
	public GridFSFile store(InputStream content, String fileName, String contentType, DBObject metadata) {
		DryRunGridFSDBFile file = new DryRunGridFSDBFile(fileName, readFully(content));
		file.setMetaData(metadata);
		file.setContentType(contentType);
		
		return file;
	}

	@Override
	public List<GridFSDBFile> find(Query query) {
		if(query == null) {
			if(this.files.isEmpty()) {
				return new ArrayList<GridFSDBFile>();
			}
			
			List<GridFSDBFile> list = new ArrayList<GridFSDBFile>();
			
			Collection<DryRunGridFSDBFile> files = this.files.values();
			for(DryRunGridFSDBFile file : files) {
				list.add(file);
			}
			
			return list;
		}
		
		return null;
	}

	@Override
	public GridFSDBFile findOne(Query query) {
		if(query == null) {
			if(this.files.isEmpty()) {
				return null;
			}
			
			DryRunGridFSDBFile file = this.files.values().iterator().next();
			return file;
		}
		
		return null;
	}

	@Override
	public void delete(Query query) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GridFsResource getResource(String fileName) {
		DryRunGridFSDBFile file = this.files.get(fileName);
		if(file == null) {
			return null;
		}
		
		return new GridFsResource(file);
	}

	@Override
	public GridFsResource[] getResources(String filenamePattern) {
		// TODO Auto-generated method stub
		return null;
	}

	private static byte[] readFully(InputStream content) {
		if(content == null) {
			throw new IllegalArgumentException("Inputstream cannot be null");
		}
		
		try {
			return IOUtils.toByteArray(content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
