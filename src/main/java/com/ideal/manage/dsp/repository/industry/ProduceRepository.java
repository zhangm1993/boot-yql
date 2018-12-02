package com.ideal.manage.dsp.repository.industry;

import com.ideal.manage.dsp.bean.industry.Produce;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProduceRepository extends BaseRepository<Produce,Long> {

    @Query("select a from Produce a where a.delFlag = 0 and a.code = ?1")
    Produce findByCode(String code);
}
