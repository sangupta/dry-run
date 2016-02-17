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

package com.sangupta.dryrun.mongo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.util.JSON;
import com.sangupta.jerry.constants.HttpHeaderName;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.HashUtils;
import com.sangupta.jerry.util.ReflectionUtils;

/**
 * In-memory implementation of {@link GridFSDBFile} that can be used with
 * {@link DryRunGridFSTemplate}.
 * 
 * @author sangupta
 *
 */
public class DryRunGridFSDBFile extends GridFSDBFile {
	
	private final String id;

	private String fileName;
	
	private byte[] bytes;
	
	private final Map<String, Object> metadata = new HashMap<String, Object>();
	
	private Date uploadDate;
	
	private transient GridFS gridFS;
	
	public DryRunGridFSDBFile(String filename, byte[] bytes) {
		this.id = UUID.randomUUID().toString();
		this.fileName = filename;
		this.bytes = bytes;
		
		this.uploadDate = new Date();
		this.put("_id", this.id);
		this.put("filename", this.fileName);
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
		
		Map<String, Object> map = ReflectionUtils.convertToMap(metadata, false);
		if(AssertUtils.isEmpty(map)) {
			return;
		}
		
		this.metadata.putAll(map);
	}
	
	public void setMetaData(DBObject metadata) {
		if(metadata == null) {
			return;
		}
		
		Set<String> keySet = metadata.keySet();
		if(keySet.isEmpty()) {
			return;
		}
		
		for(String key : keySet) {
			this.metadata.put(key, metadata.get(key));
		}
	}

	public void setContentType(String contentType) {
		this.metadata.put(HttpHeaderName.CONTENT_TYPE, contentType);
	}
	
	public Map<String, Object> getMetadata() {
		return metadata;
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

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(this.bytes);
	}
	
	@Override
	public boolean isPartialObject() {
		return false;
	}
	
	@Override
	public int numChunks() {
		return 1;
	}
	
	@Override
	public Date getUploadDate() {
		return this.uploadDate;
	}
	
	@Override
	public boolean containsKey(String s) {
		return this.containsField(s);
	}
	
	@Override
	protected GridFS getGridFS() {
		return this.gridFS;
	}
	
	@Override
	public void markAsPartialObject() {
		// do nothing
	}
	
	@Override
	public DBObject getMetaData() {
		DBObject obj = new BasicDBObject();
		
		if(this.metadata != null) {
			Set<String> keySet = this.metadata.keySet();
			for(String key : keySet) {
				Object value = this.metadata.get(key);
				obj.put(key, value);
			}
		}
		
		// TODO: add filename and bytes to the right attribute
		// and other necessary attributes
		
		return obj;
	}

	@Override
	public long writeTo(File file) throws IOException {
		if(file == null) {
			throw new IllegalArgumentException("File cannot be null");
		}
		
		FileUtils.writeByteArrayToFile(file, this.bytes);
		return this.bytes.length;
	}
	
	@Override
	public long writeTo(String fileName) throws IOException {
		return this.writeTo(new File(fileName));
	}
	
	@Override
	public long writeTo(OutputStream out) throws IOException {
		IOUtils.write(this.bytes, out);
		return this.bytes.length;
	}
	
	@Override
	public Object removeField(String key) {
		return this.metadata.remove(key);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void putAll(Map map) {
		throw new UnsupportedOperationException("Not supported by GridFSFile");
	}
	
	@Override
	public Object put(String key, Object value) {
		return this.metadata.put(key, value);
	}
	
	@Override
	public Set<String> keySet() {
		return this.metadata.keySet();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAliases() {
		if(this.metadata == null) {
			return null;
		}
		
		Object value = this.metadata.get("aliases");
		if(value == null) {
			return null;
		}
		
		return (List<String>) value;
	}
	
	@Override
	public void save() {
		throw new UnsupportedOperationException("Still need to implement this");
	}
	
	@Override
	public void putAll(BSONObject o) {
		throw new UnsupportedOperationException("Not supported by GridFSFile");
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map toMap() {
		throw new UnsupportedOperationException("Not supported by GridFSFile");
	}
	
	@Override
	public long getChunkSize() {
		return Long.MAX_VALUE;
	}
	
	@Override
	protected void setGridFS(GridFS fs) {
		this.gridFS = fs;
	}
	
	@Override
	public void validate() {
		// as everything is in-memory - validation should always succeed
	}
	
	@Override
	public String toString() {
		return JSON.serialize(this);
	}
	
}
