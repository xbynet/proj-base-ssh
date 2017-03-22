package net.xby1993.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

public class BaseDao<T> extends HibernateDaoSupport implements DAO<T>{
	// 存储泛型的实际参数
    private Class<T> clazz;

    @SuppressWarnings("unchecked")
	public BaseDao() {
        // 谁实现该类，这就是谁的类字节码
        Class c = this.getClass();
        // 返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type
        Type type = c.getGenericSuperclass();
        // 将类型强转为参数化类型
        ParameterizedType pType = (ParameterizedType) type;
        // 获取该类的父类的所有实际类型参数，也就是泛型的实际参数
        // 这里也就是获取BaseDaoImpl的实际类型参数
        Type[] actualTypeArguments = pType.getActualTypeArguments();
        // 将实际类型参数赋值给成员变量
        clazz = (Class<T>) (actualTypeArguments[0]);
    }

    @Resource(name = "sessionFactory")
    public void setMySessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    /*@Override
    public Serializable save(T entity) {
        return this.getHibernateTemplate().save(entity);
    }*/

    @Override
    public void update(T entity) {
        this.getHibernateTemplate().update(entity);
    }
    @Override
    public int bulkUpdateByHQL(String queryString,Object... values) {
        return getHibernateTemplate().bulkUpdate(queryString,values);
    }
    @Override
    public void saveOrUpdate(T entity) {
        this.getHibernateTemplate().saveOrUpdate(entity);
    }

    @Override
    public void delete(T entity) {
        this.getHibernateTemplate().delete(entity);
    }
    @Override
    public void deleteByKey(Serializable id) {
        this.delete(this.load(id));
    }
    @Override
    public void deleteAll(Collection<T> entities) {
        getHibernateTemplate().deleteAll(entities);
    }
    @Override
    public T findById(Serializable oid) {
        return (T) this.getHibernateTemplate().get(this.clazz, oid);
    }
    @SuppressWarnings("unchecked")
	@Override
    public T load(Serializable id) {
        T load = (T) this.getSession().load(clazz, id);
        return load;
    }
    @SuppressWarnings("unchecked")
	@Override
    public List<T> findByHQL(String queryString,Object... values){
    	return (List<T>) getHibernateTemplate().find(queryString, values);
    }
    @Override
    public void executeSql(String sqlString, Object... values) {
    	Query query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        query.executeUpdate();
    }
    @SuppressWarnings("unchecked")
	@Override
	public List<T> findBySql(String sqlString, Object... values) {
    	SQLQuery query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        query.addEntity(clazz);
        return query.list();
	}
    @SuppressWarnings("unchecked")
	@Override
	public  T findUniqueBySql(String sqlString, Object... values) {
    	SQLQuery query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        query.addEntity(clazz);
        return (T) query.uniqueResult();
	}
    @SuppressWarnings("unchecked")
	@Override
	public T findUniqueByHQL(String queryString, Object... values){
    	Query query = this.getSession().createQuery(queryString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
            	query.setParameter(i, values[i]);
            }
        }
        return (T) query.uniqueResult();
	}
    @SuppressWarnings("unchecked")
	@Override
	public List<T> findByHQL(String queryString, String[] paramNames, Object[] values){
    	return (List<T>) getHibernateTemplate().findByNamedParam(queryString, paramNames, values);
	}
    @SuppressWarnings("unchecked")
	@Override
	public T findUniqueByHQL(String queryString, String[] paramNames, Object[] values){
    	Query query = this.getSession().createQuery(queryString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
            	query.setParameter(paramNames[i], values[i]);
            }
        }
        return (T) query.uniqueResult();
	}
    @SuppressWarnings("unchecked")
	@Override
    public List<T> findByNamedQuery(String queryName, Object... values) {
        return (List<T>) getHibernateTemplate().findByNamedQuery(queryName, values);
    }
    @SuppressWarnings("unchecked")
	@Override
	public List<T> findByNamedQuery(String queryName, String[] paramNames, Object[] values){
    	return (List<T>) getHibernateTemplate().findByNamedQueryAndNamedParam(queryName,paramNames, values);
	}
    @SuppressWarnings("unchecked")
	@Override
    public Iterator<T> iterate(String queryString,Object... values){
    	return (Iterator<T>) getHibernateTemplate().iterate(queryString, values);
    }
    @SuppressWarnings("unchecked")
	@Override
    public void closeIterator(Iterator it) {
        getHibernateTemplate().closeIterator(it);
    }
    @SuppressWarnings("unchecked")
	@Override
    public List<T> findAll() {
        return (List<T>) this.getHibernateTemplate().find("from " + this.clazz.getSimpleName());
    }
    @Override
    public List<T> loadAll() {
        return (List<T>) getHibernateTemplate().loadAll(clazz);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public Integer getRowCount(DetachedCriteria detachedCriteria) {
        // 设置记录数投影
        detachedCriteria.setProjection(Projections.rowCount());
        List<Long> list = (List<Long>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
        // 将投影置为空
        detachedCriteria.setProjection(null);
        if (list.size() > 0) {
            return list.get(0).intValue();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<T> findByPage(DetachedCriteria detachedCriteria,List<Order> orders, int pageNo, int pageSize) {
    	if(orders!=null){
    		for(Order order:orders){
    			detachedCriteria.addOrder(order);
    		}
    	}
        // 指定hibernate在连接查询时，只封装成一个对象
        detachedCriteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
        pageNo=pageNo<1?1:pageNo;
        int startIndex=(pageNo-1)*pageSize;
        return (List<T>) this.getHibernateTemplate().findByCriteria(detachedCriteria, startIndex, pageSize);
    }
    @SuppressWarnings("unchecked")
    @Override
	public PageResults<T> findPageForResults(DetachedCriteria criteria,List<Order> orders,
            int pageNo, int pageSize){
    	if(orders!=null){
    		for(Order order:orders){
    			criteria.addOrder(order);
    		}
    	}
    	pageNo=pageNo<1?1:pageNo;
        int startIndex=(pageNo-1)*pageSize;
        criteria.setProjection (Projections.rowCount());// 设置查询的结果是总数
        int totalRows = ((Number) getHibernateTemplate().findByCriteria (criteria).get(0 )).intValue();
        criteria.setProjection ( null );//设置为 null这样查询的 结果就不是总数了
      //criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setResultTransformer (Criteria.ROOT_ENTITY ); //使用了关联类查询要设置这个，不然返回的是 object【】类型
        List<T> list=(List<T>) getHibernateTemplate().findByCriteria(criteria, startIndex, pageNo);
        PageResults<T> results=new PageResults<>();
        results.setCurrentPage(pageNo);
        results.setPageSize(pageSize);
        results.setPageCount(totalRows/pageSize+1);
        results.setTotalCount(totalRows);
        results.setResults(list);
        return results;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<T> findByCriteria(DetachedCriteria detachedCriteria) {
        return (List<T>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
    }
    @Override
    public DetachedCriteria createDetachedCriteria() {
        return DetachedCriteria.forClass(clazz);
    }
    @Override
    public Criteria createCriteria() {
        return this.getSession().createCriteria(clazz);
    }
    @SuppressWarnings("unchecked")
	@Override
    public List<T> findByProperty(String propertyName,Object value) {  
        String queryString = "from "+clazz.getName()+ " as model where model." + propertyName + "=?";     
        return (List<T>) getHibernateTemplate().find(queryString, value);  
    } 
    @SuppressWarnings("unchecked")
	@Override
    public T findUniqueByProperty(String propertyName,Object value){
    	String queryString = "select model from "+clazz.getName()+ " as model where model." + propertyName + "=?";     
        return (T) getSession().createQuery(queryString).uniqueResult(); 
    }
    @Override
    public List<T> findByExample(T entity) {  
        return getHibernateTemplate().findByExample(entity);  
    }  
    @Override
    public Object getStatValue(DetachedCriteria criteria, String propertyName,
            String StatName) {
        if (StatName.toLowerCase().equals("max"))
            criteria.setProjection(Projections.max(propertyName));
        else if (StatName.toLowerCase().equals("min"))
            criteria.setProjection(Projections.min(propertyName));
        else if (StatName.toLowerCase().equals("avg"))
            criteria.setProjection(Projections.avg(propertyName));
        else if (StatName.toLowerCase().equals("sum"))
            criteria.setProjection(Projections.sum(propertyName));
        else
            return null;
        List list = getHibernateTemplate().findByCriteria(criteria);
        criteria.setProjection(null);
        return list.get(0);
    }
    public void lock(T entity, LockMode lock) {
        getHibernateTemplate().lock(entity, lock);
    }
    @Override
    public void initialize(T proxy) {
        getHibernateTemplate().initialize(proxy);
    }
    @Override
    public void flush() {
        getHibernateTemplate().flush();
    }
    
    @Override
    public Session getSession(){
    	return this.getSessionFactory().getCurrentSession();
    }

	
}
