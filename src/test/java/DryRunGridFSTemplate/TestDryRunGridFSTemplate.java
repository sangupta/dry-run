package DryRunGridFSTemplate;

import org.junit.Test;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.sangupta.dryrun.mongo.DryRunGridFSTemplate;

public class TestDryRunGridFSTemplate {

	@Test
	public void testGridFSTemplate() {
		GridFsTemplate template = new DryRunGridFSTemplate("myBucket");
		
		// test with the template
	}
}
