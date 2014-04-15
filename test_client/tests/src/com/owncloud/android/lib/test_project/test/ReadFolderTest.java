/* ownCloud Android Library is available under MIT license
 *   Copyright (C) 2014 ownCloud Inc.
 *   
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *   
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *   
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 *   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS 
 *   BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN 
 *   ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 *   CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 *
 */

package com.owncloud.android.lib.test_project.test;

import java.io.File;

import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.resources.files.FileUtils;
import com.owncloud.android.lib.test_project.TestActivity;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Class to test Read Folder Operation
 * @author masensio
 * @author David A. Velasco
 */

public class ReadFolderTest extends	ActivityInstrumentationTestCase2<TestActivity> {
	
	private static final String LOG_TAG = ReadFolderTest.class.getCanonicalName();

	private static final String FOLDER_PATH = "/folderToRead";
	private static final String FILE1_NAME = "file1.txt";
	private static final String FILE2_NAME = "file2.txt";
	private static final String FILE3_NAME = "file3.txt";

	
	private TestActivity mActivity;
	
	public ReadFolderTest() {
		super(TestActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		
		File textFile = mActivity.extractAsset(TestActivity.ASSETS__TEXT_FILE_NAME);
		RemoteOperationResult result = mActivity.createFolder(FOLDER_PATH, true);
		if (result.isSuccess()) {
			String [] filePaths = {
					FOLDER_PATH + FileUtils.PATH_SEPARATOR + FILE1_NAME,
					FOLDER_PATH + FileUtils.PATH_SEPARATOR + FILE2_NAME,
					FOLDER_PATH + FileUtils.PATH_SEPARATOR + FILE3_NAME
			};

			for (int i=0; i<filePaths.length && result.isSuccess(); i++) {
				result = mActivity.uploadFile(
						textFile.getAbsolutePath(), 
						filePaths[i], 
						"txt/plain");
			}
		}
				
		if (!result.isSuccess()) {
			Utils.logAndThrow(LOG_TAG, result);
		}
	    
	}

	/**
	 * Test Read Folder
	 */
	public void testReadFolder() {

		RemoteOperationResult result = mActivity.readFile(FOLDER_PATH);
		assertTrue(result.isSuccess());
		assertTrue(result.getData() != null && result.getData().size() > 1);
		assertTrue(result.getData().size() == 4);
		// TODO assert more properties about the result
	}

	
	@Override
	protected void tearDown() throws Exception {
		RemoteOperationResult removeResult = mActivity.removeFile(FOLDER_PATH);
		if (!removeResult.isSuccess()) {
			Utils.logAndThrow(LOG_TAG, removeResult);
		}
		
		super.tearDown();
	}
	
}
