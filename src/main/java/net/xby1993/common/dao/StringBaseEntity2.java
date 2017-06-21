package net.xby1993.common.dao;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * 统一定义String类型id的entity基类.
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * 子类可重载getId()函数重定义id的列名映射和生成策略.
 * 
 * uuid
 */
//JPA 基类的标识
@MappedSuperclass
public abstract class StringBaseEntity2 extends BaseEntity2{

	protected String id;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		if(id.equals(""))
		{
			id = null;
		}
		this.id = id;
	}
}
