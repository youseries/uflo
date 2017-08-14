package com.bstek.uflo.console.provider;

import java.util.Date;

/**
 * @author Jacky.gao
 * @since 2017年7月12日
 */
public class ProcessFile {
	private String name;
	private Date updateDate;
	public ProcessFile(String name,Date updateDate) {
		this.name=name;
		this.updateDate=updateDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
