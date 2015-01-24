/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nedzhang.skunktool3.apiRunner.FileStore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.nedzhang.skunktool3.SkunkSetting;
import com.nedzhang.skunktool3.apiRunner.entity.ApiMaterial;
import com.nedzhang.skunktool3.apiRunner.entity.ApiMaterialType;

public class MaterialStore {

	// private static String fileStorePath;

	// private static File inputFileStoreFolder;
	// private static File outputTemplateFileStoreFolder;
	// private static File transformationFileStoreFolder;
	// private static File chainRunFileStoreFolder;

	// private static File inputFolder;
	// private static File outputTemplateFolder;
	// private static File outputFolder;
	// private static File outputTransformationFolder;
	// private static File outputExecutionFolder;

	private static Map<ApiMaterialType, File> materialFolderMap;

	{

		final File fileStoreFolder = new File(
				SkunkSetting.getInstance().FILE_STORE_PATH);

		if (!fileStoreFolder.exists()) {
			fileStoreFolder.mkdir();
		}

		materialFolderMap = new HashMap<ApiMaterialType, File>();

		materialFolderMap.put(ApiMaterialType.ApiInput,
				getStoreFolder(fileStoreFolder, "10_input"));

		materialFolderMap.put(ApiMaterialType.TestHarness,
				getStoreFolder(fileStoreFolder, "11_test_harness"));

		materialFolderMap.put(ApiMaterialType.ApiOutputTemplate,
				getStoreFolder(fileStoreFolder, "20_outputTemplate"));

		materialFolderMap.put(ApiMaterialType.ApiOutput,
				getStoreFolder(fileStoreFolder, "30_output"));

		materialFolderMap.put(ApiMaterialType.ApiOutputTransformation,
				getStoreFolder(fileStoreFolder, "40_output_transform"));

		materialFolderMap.put(ApiMaterialType.ApiOutputExecution,
				getStoreFolder(fileStoreFolder, "50_output_transform_result"));

		// materialFolderMap.put(ApiMaterialType.ApiOutputExecution,
		// getStoreFolder(fileStoreFolder, "80_execution"));

	}

	// @Override
	// public void saveApiInput(final String apiName, final String inputName,
	// final String inputDescription, final String apiInput)
	// throws IOException {
	//
	// final ApiMaterialFolder apiInputStore = new ApiMaterialFolder(
	// inputFolder, apiName);
	//
	// final ApiMaterial inputToSave = new ApiMaterial(apiName,
	// ApiMaterialType.ApiInput, inputName, inputDescription, apiInput);
	//
	// apiInputStore.saveHistory(inputToSave, true);
	//
	// }

	public void saveMaterial(final String apiName,
			final ApiMaterialType materialType, final String materialName,
			final String materialDescription, final String materialContent)
			throws IOException {

		final ApiMaterialFolder apiInputStore = getApiMaterialFolder(apiName,
				materialType);

		final ApiMaterial inputToSave = new ApiMaterial(apiName,
				ApiMaterialType.ApiInput, materialName, materialDescription,
				materialContent);

		apiInputStore.saveMaterial(inputToSave, true);

	}

	public String[] getMaterialList(final String apiName,
			final ApiMaterialType materialType) {

		final ApiMaterialFolder apiMaterialFolder = getApiMaterialFolder(
				apiName, materialType);

		return apiMaterialFolder.getMaterialList();

	}

	public ApiMaterial getMaterial(final String apiName,
			final ApiMaterialType materialType, final String materialName)
			throws IOException {
		final ApiMaterialFolder apiMaterialFolder = getApiMaterialFolder(
				apiName, materialType);

		return apiMaterialFolder.loadMaterial(materialName);
	}

	private ApiMaterialFolder getApiMaterialFolder(final String apiName,
			final ApiMaterialType materialType) {
		final File materialTypeFolder = materialFolderMap.get(materialType);

		final ApiMaterialFolder apiMaterialFolder = new ApiMaterialFolder(
				materialTypeFolder, apiName);
		return apiMaterialFolder;
	}

	private File getStoreFolder(final File fileStoreFolder,
			final String sectionName) {

		final File sectionFolder = new File(fileStoreFolder, sectionName);

		// if (sectionFolder == null) {
		// throw new FilerException("Got Null when try to get the folder for " +
		// sectionName);
		// }

		if (!sectionFolder.exists()) {
			sectionFolder.mkdir();
		}

		return sectionFolder;
	}
}
