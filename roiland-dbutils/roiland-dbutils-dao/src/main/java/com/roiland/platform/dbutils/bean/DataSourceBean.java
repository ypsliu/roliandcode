package com.roiland.platform.dbutils.bean;

import javax.sql.DataSource;

public class DataSourceBean {

	private String name;
	private String schema;
	private DataSource datasource;
	
	public DataSourceBean(String name, String schema, DataSource datasource) {
		this.name = name;
		this.schema = schema;
		this.datasource = datasource;
	}
	
	public String getSchema() {
		return schema;
	}

	public DataSource getDataSource() {
		return datasource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataSourceBean other = (DataSourceBean) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
