package com.roiland.platform.dbutils.bean;

/**
 * 数据库连接池初始化
 */
public class DBConnBean {

    private String name = null;
    private String schema = null;
    private String url = null;
    private String username = null;
    private String password = null;
	
	public DBConnBean(String name, String url, String username, String password) {
        this.name = name;
		this.username = username;
		this.password = password;

		final Integer iHostPos = url.indexOf('/', url.indexOf("://") + 3);
		if (iHostPos < 0 || (url.indexOf('?') > 0 && iHostPos + 1 == url.indexOf('?')) || iHostPos + 1 == url.length()) {
    		this.url = url;
        	this.schema = name;
		} else {
			this.url = url.substring(0, iHostPos + 1) + (url.indexOf('?') > 0? url.substring(url.indexOf('?')): "");
			this.schema = url.substring(iHostPos + 1, url.indexOf('?') > 0? url.indexOf('?'): url.length());
		}
	}

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchema() {
        return schema;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DBConnBean that = (DBConnBean) o;

		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (schema != null ? !schema.equals(that.schema) : that.schema != null) return false;
		if (url != null ? !url.equals(that.url) : that.url != null) return false;
		if (username != null ? !username.equals(that.username) : that.username != null) return false;
		return !(password != null ? !password.equals(that.password) : that.password != null);

	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (schema != null ? schema.hashCode() : 0);
		result = 31 * result + (url != null ? url.hashCode() : 0);
		result = 31 * result + (username != null ? username.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "DBConnBean [name=" + name + ", schema=" + schema + ", url=" + url + ", username=" + username + "]";
	}
}
