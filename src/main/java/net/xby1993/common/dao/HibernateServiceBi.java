package net.xby1993.common.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import net.xby1993.common.util.ReflectionUtils;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.util.Assert;


@SuppressWarnings("unchecked")
public class HibernateServiceBi<T extends BaseEntity2, PK extends Serializable> {
	private Logger log=LoggerFactory.getLogger(HibernateServiceBi.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private HibernateTemplate template;
	
	private Class<T> entityClass;

	public HibernateServiceBi() {
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		entityClass = (Class<T>) type.getActualTypeArguments()[0];
	}
	
	protected Session getSession() {
		Session s=null;
		try {
			s=sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			s=sessionFactory.openSession();
		}
		return s;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public HibernateTemplate getTemplate(){
		if(template==null){
			synchronized(this){
				if(template==null){
					template=new HibernateTemplate(sessionFactory);
				}
			}
		}
		return template;
	}
	
	public void save(final T... entitys){
		Assert.notNull(entitys, "entity不能为空");
		getTemplate().executeWithNativeSession(new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session paramSession)
					throws HibernateException {
				for(T t:entitys){
					if(t.getInsertTime()==null)
					{
						t.setInsertTime(new Date());
						t.setUpdateTime(new Date());
					}
					else
					{
						t.setUpdateTime(new Date());
					}
					paramSession.saveOrUpdate(t);
				}
				return null;
			}
		});
		
	}

	public void delete(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		getTemplate().delete(entity);
		log.debug("delete entity: {}", entity);
	}

	public void delete(final PK... ids) {
		Assert.notNull(ids, "ids不能为空");
		getTemplate().executeWithNativeSession(new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session paramSession)
					throws HibernateException {
				for (PK id : ids) {
					paramSession.delete(id);
				}
				return null;
			}
		});
		log.debug("delete entity {},id is {}", entityClass.getSimpleName(), ids);
	}

	/**
	 * 按id获取对象.
	 */
	public T load(final PK id) {
		Assert.notNull(id, "id不能为空");
		return (T) getTemplate().load(entityClass, id);
	}
	
	public T get(final PK id) {
		Assert.notNull(id, "id不能为空");
		return (T) getTemplate().get(entityClass, id);
	}
	public T loadInit(final PK id) {
		Assert.notNull(id, "id不能为空");
		T res=getTemplate().executeWithNativeSession(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session paramSession)
					throws HibernateException {
				T t=(T) paramSession.load(entityClass, id);
				autoInit(t);
				return t;
			}
			
		});
		return res;
	}
	
	public T getInit(final PK id) {
		Assert.notNull(id, "id不能为空");
		T res=getTemplate().executeWithNativeSession(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session paramSession)
					throws HibernateException {
				T t=(T) paramSession.get(entityClass, id);
				autoInit(t);
				return t;
			}
			
		});
		return res;
	}
	/**
	 *	获取全部对象.
	 */
	public List<T> getAll() {
		DetachedCriteria dc=DetachedCriteria.forClass(entityClass);
		return (List<T>) getTemplate().findByCriteria(dc);
	}
	/**
	 *	获取全部对象,支持排序.
	 */
	public List<T> getAll(String orderBy, boolean isAsc) {
		DetachedCriteria dc=DetachedCriteria.forClass(entityClass);
		if(isAsc)
		{
			dc.addOrder(Order.asc(orderBy));
		}
		else
		{
			dc.addOrder(Order.desc(orderBy));
		}
		return (List<T>) getTemplate().findByCriteria(dc);
	}

	/**
	 * 按属性查找对象列表,匹配方式为相等.
	 */
	public List<T> findBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		DetachedCriteria dc=DetachedCriteria.forClass(entityClass);
		dc.add(criterion);
		return (List<T>) getTemplate().findByCriteria(dc);
	}
	public List<T> findByInit(final String propertyName, final Object value) {
		List<T> list=getTemplate().executeWithNativeSession(new HibernateCallback<List<T>>() {

			@Override
			public List<T> doInHibernate(Session paramSession)
					throws HibernateException {
				Criterion criterion = Restrictions.eq(propertyName, value);
				Criteria c=paramSession.createCriteria(entityClass).add(criterion);
				List<T> list=c.list();
				if(list!=null){
					for(T o:list){
						autoInit(o);
					}
				}
				return list;
			}
		});
		return list;
	}
	
	/**
	 * 按id列表获取对象.
	 */
	public List<T> findByIds(List<PK> ids) {
		DetachedCriteria dc=DetachedCriteria.forClass(entityClass).add(Restrictions.in(getIdName(), ids));
		return (List<T>) getTemplate().findByCriteria(dc);
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public <X> List<X> find(final String hql, final Object... values) {
		return getTemplate().executeWithNativeSession(new HibernateCallback<List<X>>() {
			@Override
			public List<X> doInHibernate(Session paramSession)
					throws HibernateException {
				return createQuery(paramSession,hql, values).list();
			}
		});
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public <X> List<X> find(final String hql, final Map<String, ?> values) {
		return getTemplate().executeWithNativeSession(new HibernateCallback<List<X>>() {
			@Override
			public List<X> doInHibernate(Session paramSession)
					throws HibernateException {
				return createQuery(paramSession,hql, values).list();
			}
		});
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 */
	public int batchExecute(final String hql, final Object... values) {
		return getTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session paramSession)
					throws HibernateException {
				return createQuery(paramSession,hql, values).executeUpdate();
			}
		});
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * @return 更新记录数.
	 */
	public int batchExecute(final String hql, final Map<String, ?> values) {
		return getTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session paramSession)
					throws HibernateException {
				return createQuery(paramSession,hql, values).executeUpdate();
			}
		});
	}
	public void executeSql(final String sqlString, final Object... values) {
		getTemplate().executeWithNativeSession(new HibernateCallback<Void>() {

			@Override
			public Void doInHibernate(Session paramSession)
					throws HibernateException {
				Query query = paramSession.createSQLQuery(sqlString);
		        if (values != null)
		        {
		            for (int i = 0; i < values.length; i++)
		            {
		                query.setParameter(i, values[i]);
		            }
		        }
		        query.executeUpdate();
		        return null;
			}
		});
    	
    }
	public List<T> findBySql(final String sqlString, final Object... values) {
		return getTemplate().executeWithNativeSession(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session paramSession)
					throws HibernateException {
				SQLQuery query = paramSession.createSQLQuery(sqlString);
		        if (values != null)
		        {
		            for (int i = 0; i < values.length; i++)
		            {
		                query.setParameter(i, values[i]);
		            }
		        }
		        query.addEntity(entityClass);
		        return query.list();
			}
		});
    	
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 
	 * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	private Query createQuery(Session session, final String queryString, final Object... values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = session.createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	private Query createQuery(Session session, final String queryString, final Map<String, ?> values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = session.createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}

	/**
	 * 按Criteria查询对象列表.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public List<T> find(final List<Criterion> criterions,final String[] aliasNames,final List<Order> orders) {
		
		return getTemplate().executeWithNativeSession(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session paramSession)
					throws HibernateException {
				return createCriteria(paramSession,criterions,orders,aliasNames).list();
			}
		});
	}
	public List<T> findInit(final List<Criterion> criterions,final String[] aliasNames,final List<Order> orders) {
		return getTemplate().executeWithNativeSession(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session paramSession)
					throws HibernateException {
				List<T> list= createCriteria(paramSession,criterions,orders,aliasNames).list();
				if(list!=null){
					for(T t:list){
						autoInit(t);
					}
				}
				return list;
			}
		});
	}
	public List<T> find(DetachedCriteria dc){
		return (List<T>) getTemplate().findByCriteria(dc);
	}
	
	/**
	 * 分组查询/// 
	 */
	public List<Object> find(final List<Criterion> criterions,final String[] aliasNames,final List<Order> orders,final ProjectionList pList) {
	
		//pList.add(Projections.property("firm.name"));
		//pList.add(Projections.groupProperty(group));
		return getTemplate().executeWithNativeSession(new HibernateCallback<List<Object>>() {
			@Override
			public List<Object> doInHibernate(Session paramSession)
					throws HibernateException {
				return createCriteria(paramSession,criterions,orders,aliasNames).setProjection(pList).list();
			}
		});
	}
	
	/**
	 * 按Criteria查询唯一对象.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public T findUnique(final List<Criterion> criterions,final String[] aliasNames) {
		return getTemplate().executeWithNativeSession(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session paramSession)
					throws HibernateException {
				return (T) createCriteria(paramSession,criterions,null,aliasNames).uniqueResult();
			}
		});
	}
	public T findUniqueInit(final List<Criterion> criterions,final String[] aliasNames) {
		return getTemplate().executeWithNativeSession(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session paramSession)
					throws HibernateException {
				T tt=(T) createCriteria(paramSession,criterions,null,aliasNames).uniqueResult();
				autoInit(tt);
				return tt;
			}
		});
	}
	/**
	 * 根据Criterion条件创建Criteria.
	 * 
	 * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public Criteria createCriteria(Session session,final List<Criterion> criterions,final List<Order> orders,final String[] aliasNames) {
		Criteria criteria = session.createCriteria(entityClass);
		
		if(criterions!=null)
		{
			for (Criterion c : criterions) {
				criteria.add(c);
			}
		}
		if(aliasNames!=null)
		{
			for (String alias : aliasNames) 
			{
				String alias2 = alias;
				if(alias.indexOf(".")>0)
				{
					int size = alias.split("\\.").length;
					alias2 = alias.split("\\.")[size-1];
					
				}
				
				criteria.createAlias(alias, alias2,JoinType.LEFT_OUTER_JOIN);
				
			}
		}
		if(orders!=null)
		{
			for (Order order : orders) 
			{
				criteria.addOrder(order);
			}
		}
		else
		{
			criteria.addOrder(Order.desc("insertTime"));
		}
		
	
		return criteria;
	}

	/**
	 * 初始化对象.
	 * 只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性.
	 * 如需初始化关联属性,可实现新的函数,执行:
	 * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合.
	 * Hibernate.initialize(user.getDescription())，初始化User的直接属性和延迟加载的Description属性.
	 */
	public void initEntity(T... entityList) {
		for (T entity : entityList) {
			Hibernate.initialize(entity);
		}
	}

	/**
	 * Flush当前Session.
	 */
	public void flush() {
		getSession().flush();
	}

	/**
	 * 取得对象的主键名.
	 */
	public String getIdName() {
		ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
		return meta.getIdentifierPropertyName();
	}

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page 分页参数.不支持其中的orderBy参数.
	 * @param hql hql语句.
	 * @param pageNo 当前页数.
	 * @param pageSize 总页数.
	 * @param values 数量可变的查询参数,按顺序绑定.
	 * @return 分页查询结果, 附带结果列表及所有查询时的参数.
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPage(final String hql,final int pageNo,final int pageSize, final Object... values) {
		
		return getTemplate().executeWithNativeSession(new HibernateCallback<Page<T>>() {
			@Override
			public Page<T> doInHibernate(Session paramSession)
					throws HibernateException {
				Query q = createQuery(paramSession,hql, values);

				Page<T> page = new Page<T>();
				
				page.setPageNo(pageNo);
				page.setPageSize(pageSize);
				
				long totalCount = countHqlResult(hql, values);
				page.setTotalCount((int)totalCount);
				
				int start = ((pageNo - 1) * pageSize);
				q.setFirstResult(start);
				q.setMaxResults(pageSize);

				List result = q.list();
				page.setResult(result);
				return page;
			}
		});
	}

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page 分页参数.
	 * @param hql hql语句.
	 * @param values 命名参数,按名称绑定.
	 * @param pageNo 当前页数.
	 * @param pageSize 总页数.
	 * @return 分页查询结果, 附带结果列表及所有查询时的参数.
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPage(final String hql,final int pageNo,final int pageSize, final Map<String, ?> values) {
		
		return getTemplate().executeWithNativeSession(new HibernateCallback<Page<T>>() {
			@Override
			public Page<T> doInHibernate(Session paramSession)
					throws HibernateException {
				Query q = createQuery(paramSession,hql, values);
				Page<T> page = new Page<T>();
				
				page.setPageNo(pageNo);
				page.setPageSize(pageSize);
				long totalCount = countHqlResult(hql, values);
				page.setTotalCount((int)totalCount);
				
				int start = ((pageNo - 1) * pageSize);
				q.setFirstResult(start);
				q.setMaxResults(pageSize);
				
				List result = q.list();
				page.setResult(result);
				return page;
			}
		});
	}



	/**
	 * @param criterions 数量可变的Criterion
	 * @param orders 数量可变的Order
	 * @param pageNo 当前页数
	 * @param pageSize 总页数
	 * @param aliasNames 别名，用于多表关联查询
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 */
	public Page<T> findPage( final List<Criterion> criterions,final List<Order> orders, final int pageNo,final int pageSize,final String[] aliasNames) {
		return getTemplate().executeWithNativeSession(new HibernateCallback<Page<T>>() {
			@Override
			public Page<T> doInHibernate(Session paramSession)
					throws HibernateException {
				Criteria c = createCriteria(paramSession,criterions,orders,aliasNames);
				return findPage(c,pageNo,pageSize);
			}
		});
	}
	public Page<T> findPageInit( final List<Criterion> criterions,final List<Order> orders, final int pageNo,final int pageSize,final String[] aliasNames) {
		return getTemplate().executeWithNativeSession(new HibernateCallback<Page<T>>() {
			@Override
			public Page<T> doInHibernate(Session paramSession)
					throws HibernateException {
				Criteria c = createCriteria(paramSession,criterions,orders,aliasNames);
				Page<T> page= findPage(c,pageNo,pageSize);
				if(page.getResult()!=null){
					for(T t:page.getResult()){
						autoInit(t);
					}
				}
				return page;
			}
		});
	}
	public Page<T> findPage(final DetachedCriteria dc,final int pageNo,final int pageSize){
		return getTemplate().executeWithNativeSession(new HibernateCallback<Page<T>>() {
			@Override
			public Page<T> doInHibernate(Session paramSession)
					throws HibernateException {
				Criteria c=dc.getExecutableCriteria(paramSession);
				return findPage(c, pageNo, pageSize);
			}
		});
	}
	protected Page<T> findPage(Criteria c,int pageNo,int pageSize ){
		Page<T> page = new Page<T>();
		
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		int totalCount = countCriteriaResult(c);
		page.setTotalCount(totalCount);

		
		int start = ((pageNo - 1) * pageSize);
		c.setFirstResult(start);
		c.setMaxResults(pageSize);

		List result = c.list();
		page.setResult(result);
		return page;
	}
	
	/**
	 * 根据搜索条件查询前几条记录
	 * @param criterions 数量可变的Criterion
	 * @param orders 数量可变的Order
	 * @param topCount 前几条记录
	 * @param aliasNames 别名，用于多表关联查询
	 * @return 列表集合
	 */
	public List<T> findTopList(final List<Criterion> criterions,final String[] aliasNames,final List<Order> orders, final int topCount)
	{
		
		return getTemplate().executeWithNativeSession(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session paramSession)
					throws HibernateException {
				Criteria c = createCriteria(paramSession,criterions,orders,aliasNames);
				c.setFirstResult(0);
				c.setMaxResults(topCount);

				List<T> result = c.list();
				return result;
			}
		});
	}
	public List<T> findTopListInit(final List<Criterion> criterions,final String[] aliasNames,final List<Order> orders, final int topCount)
	{
		return getTemplate().executeWithNativeSession(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session paramSession)
					throws HibernateException {
				Criteria c = createCriteria(paramSession,criterions,orders,aliasNames);
				c.setFirstResult(0);
				c.setMaxResults(topCount);

				List<T> result = c.list();
				if(result!=null){
					for(T t:result){
						autoInit(t);
					}
				}
				return result;
			}
		});
	}


	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	public long countHqlResult(final String hql, final Object... values) {
		String fromHql = hql;
		//select子句与order by子句会影响count查询,进行简单的排除.
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");

		String countHql = "select count(*) " + fromHql;

		try {
			Long count = (Long) find(countHql, values).get(0);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}
	
	

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	public long countHqlResult(final String hql, final Map<String, ?> values) {
		String fromHql = hql;
		//select子句与order by子句会影响count查询,进行简单的排除.
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");

		String countHql = "select count(*) " + fromHql;

		try {
			Long count = (Long) find(countHql, values).get(0);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}
	
	/**
	 * 执行count查询获得本次Criteria查询所能获得的对象总数.
	 */
	@SuppressWarnings("unchecked")
	protected int countCriteriaResult(final Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List) ReflectionUtils.getFieldValue(impl, "orderEntries");
			ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			log.error("不可能抛出的异常:{}", e.getMessage());
		}

		// 执行Count查询
		int totalCount = (Integer) c.setProjection(Projections.count("id")).uniqueResult();

		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}
		try {
			ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			log.error("不可能抛出的异常:{}", e.getMessage());
		}

		return totalCount;
	}

  public Integer getCount(final List<Criterion> criterions,final String[] aliasNames){
	  
	  return getTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {

		@Override
		public Integer doInHibernate(Session paramSession)
				throws HibernateException {
			Criteria c = createCriteria(paramSession,criterions,null,aliasNames);
			return countCriteriaResult(c);
		}
	});
  }
  
  public Integer getCount(DetachedCriteria detachedCriteria) {
      // 设置记录数投影
      detachedCriteria.setProjection(Projections.rowCount());
      List<Long> list = (List<Long>) this.getTemplate().findByCriteria(detachedCriteria);
      // 将投影置为空
      detachedCriteria.setProjection(null);
      if (list.size() > 0) {
          return list.get(0).intValue();
      }
      return -1;
  }

	public boolean isPropertyExists(final String propertyName, final Object newValue) {
		List<T> list = findBy(propertyName, newValue);
		return (list != null && list.size()>0);
	}
	
	public void autoInit(T entity){
		if(entity==null){
			return;
		}
		Hibernate.initialize(entity);
		
		Class<?> clazz=entity.getClass();
		List<Field> fslist=new ArrayList<Field>();
		for(Class superClass=clazz;superClass!=Object.class;superClass=superClass.getSuperclass()){
			Field[] fs=superClass.getDeclaredFields();
			for(Field f:fs){
				fslist.add(f);
			}
		}
		for(Field f:fslist){
			if(f.isSynthetic()){
				continue;
			}
			Class<?> clz = f.getType();
			String name = f.getName();
			if (clz.isPrimitive() || (!BaseEntity2.class.isAssignableFrom(clz))) {
				continue;
			}
			try {
				Method m = clazz.getMethod("get" + StringUtils.capitalize(name));
				Transient t1=m.getAnnotation(Transient.class);
				if(t1==null){
					Object obj = m.invoke(entity);
					Hibernate.initialize(obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
