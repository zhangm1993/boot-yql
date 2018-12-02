package com.ideal.manage.dsp.repository.framework;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T,ID extends Serializable> extends JpaRepository<T,ID>,JpaSpecificationExecutor<T> {
    int deleteInIds(List<ID> ids);

    List<T> findAllById(List<ID> ids);

    List<T> selectAllBySql(String sql, List<Object> params, Class<T> c);

//    <T> List<T> selectAllBySql(final String sql, final List<Object> params, final Class<T> cls);

    /**
     * 根据原生sql分页查询 并映射实体
     * @param sql
     * @param pageable
     * @param c
     * @param fields
     * @return
     */
    Page<T> findAllBySql(String sql, Pageable pageable, Class<T> c, String[] fields);

    /**
     * 原生sql查询，返回分页集合
     * @param sql
     * @param pageable
     * @param c
     * @return
     */
    Page<T> findAllBySql(String sql,Pageable pageable,Class<T> c);

    /**
     * 原生sql查询，返回集合
     * @param sql
     * @param c
     * @return
     */
    List<T> findAllBySql(String sql,Class<T> c,String[] fields);

    /**
     * 执行原生SQL
     * @param sql
     * @return
     */
    int executeUpdateBySql(String sql);
}
