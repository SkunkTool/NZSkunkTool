package com.nedzhang.skunktool3.apiRunner.FileStore;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import com.nedzhang.skunktool3.apiRunner.entity.ApiMaterial;
import com.nedzhang.util.TextFileUtil;

/***
 * A folder that hold materials for an API. <br/>
 * It does not separate materials by type. The user needs to use different top
 * folders to separate materials by type.<br/>
 * Example Structure:<br/>
 * Root <br/>
 * |__MaterialType (One Material Store per Material Type)<br/>
 * :::|__ApiMaterialFolder (this class one per api per material type)<br/>
 * ::::::|__Material (Material Folder that contains description and content )<br/>
 * 
 * @author nzhang
 * 
 */
public class ApiMaterialFolder {

	private static final String DESC_FILE_NAME = "description.txt";
	// private static final String HISTORY_FILE_OUTPUT = "output.xml";
	// private static final String HISTORY_FILE_OUTPUTTEMPLATE =
	// "outputTemplate.xml";
	private static final String CONTENT_FILE_NAME = "input.xml";
	private static final String HISTORY_FILE_ENCODING = "UTF-8";

	/**
	 * The folder that holds one type of materials for one api
	 */
	private final File apiMaterialFolder;

	private final String apiName;

	/***
	 * Create an instance of ApiMaterialStore.
	 * 
	 * @param materialStoreFolder
	 *            an Api's folder to store a single type of material
	 * @param apiName
	 *            name of the api
	 */
	public ApiMaterialFolder(final File materialStoreFolder,
			final String apiName) {

		apiMaterialFolder = new File(materialStoreFolder, apiName);
		this.apiName = apiName;
	}

	public String[] getMaterialList() {

		if (apiMaterialFolder.exists()) {
			return apiMaterialFolder.list(new FilenameFilter() {

				@Override
				public boolean accept(final File dir, final String name) {
					final File fileToCheck = new File(dir, name);
					return fileToCheck.isDirectory();
				}
			});
		} else {
			return null;
		}
	}

	public String getDescription(final String material) throws IOException {

		final ApiMaterial apiData = loadMaterial(material, true);

		return apiData == null ? null : apiData.getDescription();
	}

	public ApiMaterial loadMaterial(final String materialName)
			throws IOException {
		return loadMaterial(materialName, false);
	}

	private ApiMaterial loadMaterial(final String materialName,
			final boolean loadOnlyDescription) throws IOException {

		if (apiMaterialFolder.exists()) {

			final File apiMaterialFolder = getMaterialFolder(materialName);

			if ((apiMaterialFolder != null) && apiMaterialFolder.exists()) {

				final ApiMaterial material = new ApiMaterial();

				material.setApiName(apiName);

				material.setName(materialName);

				final String description = readContentFile(apiMaterialFolder,
						ApiMaterialFolder.DESC_FILE_NAME);
				material.setDescription(description);

				if (!loadOnlyDescription) {
					final String input = readContentFile(apiMaterialFolder,
							ApiMaterialFolder.CONTENT_FILE_NAME);
					material.setContent(input);

					// final String outputTemplate = readHistoryFile(
					// historyFolder,
					// ApiInputStore.HISTORY_FILE_OUTPUTTEMPLATE);
					// apiData.setOutputTemplate(outputTemplate);
					//
					// final String output = readHistoryFile(historyFolder,
					// ApiInputStore.HISTORY_FILE_OUTPUT);
					// apiData.setOutput(output);
				}
				return material;
			}
		}
		return null;
	}

	public void saveMaterial(final ApiMaterial materialToSave,
			final boolean overwriteExistingFile) throws IOException {
		if (!apiMaterialFolder.exists()) {
			apiMaterialFolder.mkdir();
		}

		if (materialToSave == null) {
			throw new IllegalArgumentException(
					"apiHistoryToSave parameter cannot be null");
		}

		final String materialName = materialToSave.getName();

		if ((materialName == null) || (materialName.length() == 0)) {
			throw new IllegalArgumentException(
					"apiHistoryToSave cannot have a null or blank name for history.");
		}

		final File materialFolder = getMaterialFolder(materialName);

		if (!overwriteExistingFile && (materialFolder != null)
				&& materialFolder.exists()) {
			throw new IOException(
					String.format(
							"History folder for %s exists. To overwrite, please set the overwriteExistingFile parameter to true.",
							materialName));
		} else {

			final String description = materialToSave.getDescription();
			if ((description != null) && (description.length() > 0)) {
				writeContentFile(materialFolder,
						ApiMaterialFolder.DESC_FILE_NAME, description);
			}

			final String content = materialToSave.getContent();
			if ((content != null) && (content.length() > 0)) {
				writeContentFile(materialFolder,
						ApiMaterialFolder.CONTENT_FILE_NAME, content);
			}

			// final String outputTemplate =
			// apiHistoryToSave.getOutputTemplate();
			// if (outputTemplate != null && outputTemplate.length() > 0) {
			// writeHistoryFile(historyFolder,
			// ApiInputStore.HISTORY_FILE_OUTPUTTEMPLATE,
			// outputTemplate);
			// }
			//
			// final String output = apiHistoryToSave.getOutput();
			// if (output != null && output.length() > 0) {
			// writeHistoryFile(historyFolder,
			// ApiInputStore.HISTORY_FILE_OUTPUT, output);
			// }
		}
	}

	/***
	 * Get the folder that contain the content and description of a material.
	 * This is the lowest folder of the material store.
	 * 
	 * @param materialName
	 * @return
	 */
	private File getMaterialFolder(final String materialName) {
		final File materialFolder = new File(apiMaterialFolder, materialName);
		return materialFolder;
	}

	private void writeContentFile(final File materialFolder,
			final String fileName, final String content) throws IOException {

		final File fileToWrite = new File(materialFolder, fileName);

		TextFileUtil.writeTextFile(fileToWrite,
				ApiMaterialFolder.HISTORY_FILE_ENCODING, false, content);
	}

	private String readContentFile(final File materialFolder,
			final String fileName) throws IOException {

		final File fileToRead = new File(materialFolder, fileName);

		if ((fileToRead != null) & fileToRead.exists()) {
			return TextFileUtil.readTextFile(fileToRead,
					ApiMaterialFolder.HISTORY_FILE_ENCODING);
		} else {
			return null;
		}
	}

	// TODO: add the ability to load last usage
}
