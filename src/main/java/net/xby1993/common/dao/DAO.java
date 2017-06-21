package net.xby1993.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

/**
 * Hibernate通用的持久层接口
 * 
 * @author xby taojw
 *
 */
public interface DAO<T> {
	/**
	 * 保存
	 * 
	 * @param entity
	 * @return oid
	 * 
	 *         public Serializable save(T entity);
	 */
	/**
	 * 更新
	 * 
	 * @param entity
	 */
	public void update(T entity);

	/**
	 * 保存或更新
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(T entity);

	/**
	 * 删除
	 * 
	 * @param entity
	 */
	public void delete(T entity);
	/**
	 * 根据主键删除指定实体
	 * 
	 * @param id
	 */
	public void deleteByKey(Serializable id);

	/**
	 * 删除集合中的全部实体
	 * 
	 * @param entities
	 */
	public void deleteAll(Collection<T> entities);

	/**
	 * 通过对象标识符获取对象
	 * 
	 * @param oid
	 * @return 标识符对应的对象，没找大则返回null
	 */
	public T findById(Serializable id);
	public T load(Serializable id);
	/**
	 * 返回所有对象的列表
	 * 
	 * @return
	 */
	public List<T> findAll();
	/**
	 * 通过属性名来查询
	 * @param propertyName 属性名
	 * @param value
	 * @return
	 */
	public List<T> findByProperty(String propertyName,Object value);
	public T findUniqueByProperty(String propertyName,Object value);
	public List<T> findByExample(T entity);
	 /**
	  * 获取全部实体。
	  * @return
	  */
    public List<T> loadAll();

	/**
	 * 查找满足条件的总记录数
	 * 
	 * @param detachedCriteria
	 * @return
	 */
	Integer getRowCount(DetachedCriteria detachedCriteria);

	/**
	 * 向分页对象中设置记录
	 * 
	 * @param detachedCriteria
	 *            离线查询对象
	 * @param pageNo 页标，从1开始
	 * @param pageSize
	 *            每页记录数
	 * @return
	 */
	List<T> findByPage(DetachedCriteria detachedCriteria,List<Order> orders, int pageNo, int pageSize);
	/**
	 * 分页获取结果
	 * @param criteria
	 * @param orders
	 * @param pageNo 页标，从1开始
	 * @param pageSize 每页记录数
	 * @return
	 */
	public Page2<T> findPageForResults(DetachedCriteria criteria,List<Order> orders,
            int pageNo, int pageSize);
	/**
	 * 创建与会话无关的检索标准对象
	 * 
	 * @return
	 */
	public DetachedCriteria createDetachedCriteria();

	/**
	 * 创建与会话绑定的检索标准对象
	 * 
	 * @return
	 */
	public Criteria createCriteria();

	/**
	 * 通过条件查询
	 * 
	 * @param detachedCriteria
	 * @return
	 */
	List<T> findByCriteria(DetachedCriteria detachedCriteria);


	/**
	 * 使用HSQL语句直接增加、更新、删除实体
	 * 
	 * @param queryString
	 * @return
	 */
	public int bulkUpdateByHQL(String queryString,Object... values);


	/**
	 * 使用位置参数的HSQL语句检索数据
	 * 
	 * @param queryString
	 * @param values
	 * @return
	 */
	public List<T> findByHQL(String queryString, Object... values );

	/**
	 * 使用命名参数的HSQL语句检索数据
	 * 
	 * @param queryString
	 * @param paramNames
	 * @param values
	 * @return
	 */
	public List<T> findByHQL(String queryString, String[] paramNames, Object[] values);
	public T findUniqueByHQL(String queryString, Object... values);
	public T findUniqueByHQL(String queryString, String[] paramNames, Object[] values);
	/**
	 * 使用NamedQuery检索数据
	 * 
	 * @param queryName
	 *            NamedQuery名字
	 * @return
	 */
	public List<T> findByNamedQuery(String queryName, Object... values);

	/**
	 * 使用NamedQuery检索数据
	 * 
	 * @param queryName
	 *            NamedQuery名字
	 * @return
	 */
	public List<T> findByNamedQuery(String queryName, String[] paramNames, Object[] values);

	/**
	 *  使用HSQL语句检索数据，返回 Iterator
	 *    find和iterato的区别主要是iterate采用了N+1次查询，对于大批量查询，比如查询10000条记录，那么iterate就要执行10000+1次查询，find和iterate应根据具体的实际
情况来使用，对于频繁的写操作对象，应使用find查询，而对于一些只读的数据对象，应使用iterate操作，因为iterate操作使用了hibernate的缓存机制
	 * @param queryString
	 * @return
	 */
    public Iterator<T> iterate(String queryString,Object... values);
    /**
     * 关闭检索返回的 Iterator
     * @param it
     */
    public void closeIterator(Iterator it);
 /**
  *  使用指定的检索标准检索数据，返回指定统计值(max,min,avg,sum)
  * @param criteria
  * @param propertyName
  * @param StatName
  * @return
  */
    public Object getStatValue(DetachedCriteria criteria, String propertyName,
            String StatName);
    
    public List<T> findBySql(String sqlString, Object... values);
    public T findUniqueBySql(String sqlString, Object... values);
	public void executeSql(String sqlString, Object... values);
	
	

	/**
	 *  强制初始化指定的实体
	 * @param proxy
	 */
    public void initialize(T proxy);

    /**
     *  强制立即更新缓冲数据到数据库（否则仅在事务提交时才更新）
     */
    public void flush();
    
    /**
     * 获取Session
     * @return
     */
    public Session getSession();
}
