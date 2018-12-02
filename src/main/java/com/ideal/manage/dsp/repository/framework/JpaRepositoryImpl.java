//package com.ideal.manage.dsp.repository.framework;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.support.JpaEntityInformation;
//import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
//import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
//
//import javax.persistence.EntityManager;
//import java.io.Serializable;
//import java.util.List;
//
//public class JpaRepositoryImpl<T,ID extends Serializable> extends SimpleJpaRepository<T,ID> implements JpaRepository<T, ID> {
//
////    private  JpaEntityInformation<T, ?> entityInformation;
//    private final EntityManager em;
//    private final Class<T> domainClass;
//
//
////    public JpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
////        super(entityInformation, entityManager);
////        this.entityInformation = entityInformation;
////        this.em = entityManager;
////    }
//
//    public JpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
//        super(domainClass, em);
//        this.em = em;;
//
//    }
//
//    @Override
//    public int deleteInIds(List<ID> ids) {
//        return 0;
//    }
//
//    @Override
//    public List<T> findAllById(List<ID> ids) {
//        return null;
//    }
//
//    @Override
//    public List<T> selectAllBySql(String sql, List<Object> params, Class<T> c) {
//        return null;
//    }
//
//    @Override
//    public Page<T> findAllBySql(String sql, Pageable pageable, Class<T> c, String[] fields) {
//        return null;
//    }
//
//    @Override
//    public Page<T> findAllBySql(String sql, Pageable pageable, Class<T> c) {
//        return null;
//    }
//
//    @Override
//    public List<T> findAllBySql(String sql, Class<T> c, String[] fields) {
//        return null;
//    }
//
//    @Override
//    public int executeUpdateBySql(String sql) {
//        return 0;
//    }
//}
