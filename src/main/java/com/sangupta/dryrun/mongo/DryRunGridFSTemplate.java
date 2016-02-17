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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.sangupta.jerry.util.AssertUtils;

/**
 * An in-memory implementation of {@link GridFsOperations} or {@link GridFsTemplate}
 * that can be used during unit testing. Supports almost all operations.
 * 
 * Following operations are currently not supported:
 * 
 * 1. Boolean-Operators in find/delete in Query
 * 2. Sorting in find
 * 3. Regex is not supported
 * 4. Mathematical-Operators in find/delete in Query
 *   
 * @author sangupta
 *
 */
public class DryRunGridFSTemplate extends GridFsTemplate {
	
	private final Map<String, DryRunGridFSDBFile> files = new HashMap<String, DryRunGridFSDBFile>();  
	
	public DryRunGridFSTemplate(String bucket) {
		super(new DryRunMongoDBFactory(), new DryRunMongoConverter(), bucket);
	}

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
		
		this.files.put(fileName, file);
		
		return file;
	}

	@Override
	public List<GridFSDBFile> find(Query query) {
		DBObject queryObject = null;
		if(query != null) {
			queryObject = query.getQueryObject();
		}
		
		return this.findObjects(queryObject, -1);
	}

	@Override
	public GridFSDBFile findOne(Query query) {
		DBObject queryObject = null;
		if(query != null) {
			queryObject = query.getQueryObject();
		}
		
		List<GridFSDBFile> files = this.findObjects(queryObject, 1);
		if(AssertUtils.isEmpty(files)) {
			return null;
		}
		
		return files.get(0);
	}

	@Override
	public void delete(Query query) {
		DBObject queryObject = null;
		if(query != null) {
			queryObject = query.getQueryObject();
		}
		
		List<GridFSDBFile> files = this.findObjects(queryObject, 1);
		if(AssertUtils.isEmpty(files)) {
			return;
		}
		
		for(GridFSDBFile file : files) {
			this.files.remove(file.getFilename());
		}
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
		if(AssertUtils.isEmpty(filenamePattern)) {
			return new GridFsResource[0];
		}
		
		DryRunAntPath path = new DryRunAntPath(filenamePattern);

		if (path.isPattern()) {
			Query query = Query.query(Criteria.where("filename").regex(path.toRegex()));
			List<GridFSDBFile> files = this.findObjects(query.getQueryObject(), -1);
			List<GridFsResource> resources = new ArrayList<GridFsResource>(files.size());

			for (GridFSDBFile file : files) {
				resources.add(new GridFsResource(file));
			}

			return resources.toArray(new GridFsResource[resources.size()]);
		}

		return new GridFsResource[] { getResource(filenamePattern) };
	}
	
	@SuppressWarnings("unchecked")
	private List<GridFSDBFile> findObjects(DBObject matchingObject, int max) {
		List<GridFSDBFile> found = new ArrayList<GridFSDBFile>();
		
		Map<String, Object> query = null;
		if(matchingObject != null) {
			query = matchingObject.toMap();
		}
		
		boolean runMatch = AssertUtils.isNotEmpty(query);
		
		Collection<DryRunGridFSDBFile> currentFiles = this.files.values();
		for(DryRunGridFSDBFile candidate : currentFiles) {
			if(runMatch) {
				// match candidate against matching map
				if(match(query, candidate.getMetadata())) {
					found.add(candidate);
				}
			} else {
				found.add(candidate);
			}
			
			if(found.size() == max) {
				// break the for-loop as we found number of items we needed
				break;
			}
		}
		
		return found;
	}

	/**
	 * Match the query map with candidate map. All fields in query map should
	 * match with candidate map.
	 * 
	 * @param query
	 *            the map of query params. Can never be <code>null</code>
	 * 
	 * @param candidate
	 *            the map of candidate object properties
	 * 
	 * @return <code>true</code> if all properties from query are present in
	 *         candidate map and contain the same value, <code>false</code>
	 *         otherwise
	 */
	private boolean match(Map<String, Object> query, Map<String, Object> candidate) {
		Set<String> keySet = query.keySet();
		
		for(String key : keySet) {
			if(key.startsWith("$")) {
				throw new IllegalArgumentException("Query with operators is currently not supported");
			}
			
			Object value = query.get(key);
			Object candidateValue = candidate.get(key);
			
			if(value instanceof Pattern) {
				Pattern pattern = (Pattern) value;
				Matcher matcher = pattern.matcher(candidateValue.toString());
				if(matcher.find()) {
					return true;
				}
				
				return false;
			}
			
			if(value instanceof DBObject) {
				throw new IllegalArgumentException("Query with operators is currently not supported");
			}

			// normal object comparison for now
			if(!value.equals(candidateValue)) {
				return false;
			}
		}
		
		// all checks passed - we are good to go
		return true;
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
