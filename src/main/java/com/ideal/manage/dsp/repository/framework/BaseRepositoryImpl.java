package com.ideal.manage.dsp.repository.framework;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T,ID>
        implements BaseRepository<T,ID> {

    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;

    }

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }


    /**
     * 根据ID 批量删除数据
     * @param ids
     * @return
     */
    @Override
    public int deleteInIds(List<ID> ids) {
        if(ids == null || ids.isEmpty()){
            return 0;
        }
        String sql = QueryUtils.getQueryString(QueryUtils.DELETE_ALL_QUERY_STRING,entityInformation.getEntityName());
        Query query = applyAndBind(sql,ids,entityManager);
        return query.executeUpdate();
    }

    /**
     * 根据IDS 获取集合
     * @param ids
     * @return
     */
    @Override
    public List<T> findAllById(List<ID> ids) {
        if(ids == null || ids.isEmpty()){
            return new ArrayList<>();
        }
        String sql = QueryUtils.getQueryString("select x from %s x",entityInformation.getEntityName());
        Query query = applyAndBind(sql,ids,entityManager);
        List<T> list = query.getResultList();
        return list;
    }

    //TODO
    @Override
    public List<T> selectAllBySql(String sql, List<Object> params, Class<T> c) {
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> mapList = query.getResultList();
        return null;
    }

    /**
     *  根据分页条件 查询
     * @param sql   执行sql
     * @param pageable  分页条件
     * @param c     要转换的对象
     * @param fields  转换的对象的属性
     * @return
     */
    @Override
    public Page<T> findAllBySql(String sql, Pageable pageable, Class<T> c, String[] fields) {
        try{
            int total = getCount(sql);
            Query query = entityManager.createNativeQuery(sql);
            query.setMaxResults(pageable.getPageSize());
            query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
            List<Object[]> listObject = query.getResultList();
            List<T> list = new ArrayList<T>();
            for(Object[] objects : listObject){
                T obj = c.newInstance();
                for(int i = 0; i < fields.length; i++){
                    objectToBean(obj,fields[i],objects[i]);
                }
                list.add(obj);
            }
            return new PageImpl<T>(list,pageable,total);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据sql 获取总数count
     * @param sql
     * @return
     */
    public int getCount(String sql){
        sql = String.format("select count(1) from (%s) t",sql);
        Query query = entityManager.createNativeQuery(sql);
        int count = Integer.parseInt(query.getSingleResult().toString());
        return count;
    }

    /**
     * 分页查询
     * @param sql
     * @param pageable
     * @param c
     * @return
     */
    @Override
    public Page<T> findAllBySql(String sql, Pageable pageable, Class<T> c) {
        int total = getCount(sql);
        Query query = entityManager.createNativeQuery(sql,c);
        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        List<T> content = query.getResultList();
        return new PageImpl<T>(content,pageable,total);
    }

    /**
     * 根据sql查出的值 赋值给指定字段的对象
     * @param sql
     * @param c
     * @param fields
     * @return
     */
    @Override
    public List<T> findAllBySql(String sql, Class<T> c, String[] fields) {
        try{
            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> objectsList = query.getResultList();
            List<T> list = new ArrayList<>();
            for(Object[] objects : objectsList){
                T obj = c.newInstance();
                for(int i = 0;i< fields.length;i++){
                    objectToBean(obj,fields[i],objects[i]);
                }
                list.add(obj);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 执行原生的update sql
     * @param sql
     * @return
     */
    @Override
    public int executeUpdateBySql(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        return query.executeUpdate();
    }

    /**
     * 将ids作为条件 与 sql语句拼接
     * @param sql
     * @param ids
     * @param entityManager
     * @return
     */
    public Query applyAndBind(String sql,List<ID> ids,EntityManager entityManager){
        Assert.notNull(sql,"BaseRepository:SQL不能为NULL");
        Assert.notNull(ids,"BaseRepository:SQL不能为NULL");
        Assert.notNull(entityManager,"BaseRepository:类型不能为NULL");

        String alia = QueryUtils.detectAlias(sql);
        StringBuilder stringBuilder = new StringBuilder(sql);
        stringBuilder.append(" WHERE ");
        stringBuilder.append(String.format(" %s.id in (?1) ",alia));
        Query query = entityManager.createQuery(stringBuilder.toString());
        query.setParameter(1,ids);
        return query;
    }

    /**
     * 通过反射 将Object转成bean
     * @param c
     * @param fieldName
     * @param object
     * @throws Exception
     */
    public void objectToBean(Object c, String fieldName, Object object) throws Exception{
        if(c != null){
            Class cls = c.getClass();
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            //判断数据库传过来的值是否是BigDecimal类型 是 转成long或者double
            if(object != null && ("BigDecimal".equals(object.getClass().getSimpleName()) || "BigInteger".equals(object.getClass().getSimpleName()) ) ){
                if(field.getType().getSimpleName().equals("Long")){
                    object = Long.parseLong(object.toString());
                }else if(field.getType().getSimpleName().equals("Double")){
                    object = Double.parseDouble(object.toString());
                }else{
                    object = object.toString();
                }
            }

            //将Character类型 转为String类型
            if(object != null && "Character".equals(object.getClass().getSimpleName()) ){
                    object = object.toString();
            }

            field.set(c,field.getType().cast(object));
            field.setAccessible(false);
        }
    }
}
