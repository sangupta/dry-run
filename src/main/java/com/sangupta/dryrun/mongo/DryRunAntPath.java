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
