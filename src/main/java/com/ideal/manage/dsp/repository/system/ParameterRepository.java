package com.ideal.manage.dsp.repository.system;

import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParameterRepository extends BaseRepository<Parameter,Long>{

    @Query("select a from Parameter a where a.delFlag=0 and a.code=?1")
    List<Parameter> findByCode(String code);

    Parameter findById(Long id);
    @Query("select a from Parameter a where a.delFlag=0 and a.parent=?1")
    List<Parameter> findByParent(Parameter parameter);

}
