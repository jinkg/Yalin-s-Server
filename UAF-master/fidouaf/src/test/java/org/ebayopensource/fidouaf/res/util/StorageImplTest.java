package org.ebayopensource.fidouaf.res.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.yalin.fidouaf.res.util.StorageImpl;

public class StorageImplTest {

	@Test
	public void basic() {
		assertNotNull(StorageImpl.getInstance());
	}

}
