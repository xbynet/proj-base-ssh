package net.xby1993.common.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

public class ResultData<T> implements Serializable {
	public static final String SUCCESS="true";
	public static final String FAILURE ="false";
	private static final long serialVersionUID = 571887313798592339L;

	// grid列表对象
	@JSONField(name = "curPageData")
	// 前台接收时的名称
	private List<T> rows;

	// 总数
	@JSONField(name = "allDataCount")
	// 前台接收时的名称
	private Long total;


	private String status;

	// 实体对象
	private T entity;

	private Map<String, Object> map;

	private ResultData(Builder<T> builder) {
		this.rows = builder.rows;
		this.total = builder.total;
		this.status = builder.status;
		this.entity = builder.entity;
		this.map = builder.map;
	}

	public static class Builder<T> {
		private List<T> rows;

		private Long total;

		private String status = SUCCESS;

		private T entity;
		private Map<String, Object> map = new HashMap<>();

		public Builder() {
			map.put("code", false);
		}

		// 构造器入口
		public ResultData<T> build() {
			return new ResultData<T>(this);
		}

		public Builder<T> setRows(List<T> rows) {
			this.rows = rows;
			return this;
		}

		public Builder<T> setTotal(Long total) {
			this.total = total;
			return this;
		}

		public Builder<T> setStatus(boolean status) {
			this.status = String.valueOf(status);
			return this;
		}

		public Builder<T> setEntity(T entity) {
			this.entity = entity;
			return this;
		}

		public Builder<T> setMap(Map<String, Object> map) {
			this.map.putAll(map);
			return this;
		}

		public Builder<T> put(String key, Object value) {
			this.map.put(key, value);
			return this;
		}

	}

	public List<T> getRows() {
		return rows;
	}

	public Long getTotal() {
		return total;
	}

	public String getStatus() {
		return status;
	}

	public T getEntity() {
		return entity;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

}
