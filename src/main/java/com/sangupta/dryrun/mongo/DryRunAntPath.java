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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sangupta.jerry.util.AssertUtils;

/**
 * Class copied from {@link org.springframework.data.mongodb.gridfs.AntPath}
 * 
 * @author sangupta
 *
 */
public class DryRunAntPath {

	private static final String PREFIX_DELIMITER = ":";
	private static final Pattern WILDCARD_PATTERN = Pattern.compile("\\?|\\*\\*|\\*");

	private final String path;

	/**
	 * Creates a new {@link AntPath} from the given path.
	 * 
	 * @param path must not be {@literal null}.
	 */
	public DryRunAntPath(String path) {
		if(AssertUtils.isEmpty(path)) {
			throw new IllegalArgumentException("path cannot be empty/null");
		}
		
		this.path = path;
	}

	/**
	 * Returns whether the path is a pattern.
	 * 
	 * @return
	 */
	public boolean isPattern() {
		String path = stripPrefix(this.path);
		return (path.indexOf('*') != -1 || path.indexOf('?') != -1);
	}

	private static String stripPrefix(String path) {
		int index = path.indexOf(PREFIX_DELIMITER);
		return (index > -1 ? path.substring(index + 1) : path);
	}

	/**
	 * Returns the regular expression equivalent of this Ant path.
	 * 
	 * @return
	 */
	public String toRegex() {

		StringBuilder patternBuilder = new StringBuilder();
		Matcher m = WILDCARD_PATTERN.matcher(path);
		int end = 0;

		while (m.find()) {

			patternBuilder.append(quote(path, end, m.start()));
			String match = m.group();

			if ("?".equals(match)) {
				patternBuilder.append('.');
			} else if ("**".equals(match)) {
				patternBuilder.append(".*");
			} else if ("*".equals(match)) {
				patternBuilder.append("[^/]*");
			}

			end = m.end();
		}

		patternBuilder.append(quote(path, end, path.length()));
		return patternBuilder.toString();
	}

	private static String quote(String s, int start, int end) {
		if (start == end) {
			return "";
		}
		return Pattern.quote(s.substring(start, end));
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return path;
	}

}
