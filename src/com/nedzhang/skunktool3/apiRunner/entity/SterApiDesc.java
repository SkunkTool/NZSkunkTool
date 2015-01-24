package com.nedzhang.skunktool3.apiRunner.entity;

import java.io.Serializable;

public class SterApiDesc implements Serializable {
	// name="acceptOrderLineReservation"
	// title="interface in com.yantra.api.omp.order"
	// docPage="com/yantra/api/omp/order/acceptOrderLineReservation.html"

	/**
	 * 
	 */
	private static final long serialVersionUID = -5856657307654580975L;

	private String name;
	private String title;
	private String docPage;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getDocPage() {
		return docPage;
	}

	public void setDocPage(final String docPage) {
		this.docPage = docPage;
	}
}
