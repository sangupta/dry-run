package com.sangupta.dryrun.mongo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.sangupta.jerry.constants.HttpHeaderName;
import com.sangupta.jerry.util.HashUtils;
import com.sangupta.jerry.util.ReflectionUtils;

public class DryRunGridFSDBFile extends GridFSDBFile {
	
	public final String id;

	public String fileName;
	
	public byte[] bytes;
	
	public Map<String, Object> metadata;
	
	public DryRunGridFSDBFile(String filename, byte[] bytes) {
		this.id = UUID.randomUUID().toString();
		this.fileName = filename;
		this.bytes = bytes;
	}

	/**
	 * Use reflection to get all metadata and keep it in a hashmap
	 * 
	 * @param metadata
	 */
	public void setMetaData(Object metadata) {
		if(metadata == null) {
			return;
		}
		
		this.metadata = ReflectionUtils.convertToMap(metadata, false);
	}
	
	public void setMetaData(DBObject metadata) {
		if(metadata == null) {
			return;
		}
		
		Set<String> keySet = metadata.keySet();
		if(keySet.isEmpty()) {
			this.metadata = null;
		}
		
		this.metadata = new HashMap<String, Object>();
		for(String key : keySet) {
			this.metadata.put(key, metadata.get(key));
		}
	}

	public void setContentType(String contentType) {
		if(this.metadata == null) {
			this.metadata = new HashMap<String, Object>();
		}
		
		this.metadata.put(HttpHeaderName.CONTENT_TYPE, contentType);
	}

	@Override
	public boolean containsField(String fieldName) {
		if(this.metadata == null) {
			return false;
		}
		
		return this.metadata.containsKey(fieldName);
	}
	
	@Override
	public int hashCode() {
		return this.fileName.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(this == obj) {
			return true;
		}
		
		if(!(obj instanceof DryRunGridFSDBFile)) {
			return false;
		}

		DryRunGridFSDBFile file = (DryRunGridFSDBFile) obj;
		return this.fileName.equals(file.fileName);
	}
	
	@Override
	public Object get(String key) {
		if(this.metadata == null) {
			return null;
		}
		
		return this.metadata.get(key);
	}
	
	@Override
	public String getContentType() {
		if(this.metadata == null) {
			return null;
		}
		
		return (String) this.metadata.get(HttpHeaderName.CONTENT_TYPE);
	}
	
	@Override
	public String getFilename() {
		return this.fileName;
	}
	
	@Override
	public Object getId() {
		return this.id;
	}
	
	@Override
	public long getLength() {
		return this.bytes.length;
	}
	
	@Override
	public String getMD5() {
		return HashUtils.getMD5Hex(this.bytes);
	}
	
}
