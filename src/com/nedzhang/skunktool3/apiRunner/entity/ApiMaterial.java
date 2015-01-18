package com.nedzhang.skunktool3.apiRunner.entity;

import java.io.Serializable;

public class ApiMaterial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2549927051267443211L;

	private String apiName;
	private ApiMaterialType type;
	private String name;
	private String description;

	private String content;

	// private String outputTemplate;
	// private String output;

	public ApiMaterial() {

	}

	public ApiMaterial(final String apiName, final ApiMaterialType type,
			final String name, final String description, final String content) {

		this();

		this.name = name;
		this.description = description;
		this.apiName = apiName;
		this.content = content;

	}

	public String getApiName() {
		return this.apiName;
	}

	public void setApiName(final String apiName) {
		this.apiName = apiName;
	}

	public ApiMaterialType getType() {
		return this.type;
	}

	public void setType(final ApiMaterialType type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(final String content) {
		this.content = content;
	}
}
