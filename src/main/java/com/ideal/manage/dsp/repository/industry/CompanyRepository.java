package com.ideal.manage.dsp.repository.industry;

import com.ideal.manage.dsp.bean.industry.Company;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends BaseRepository<Company, Long> {

    @Query("select a from Company a where delFlag = 0 and status = 1 and a.category.id = ?1")
    List<Company> findCompanyByCatagory(Long id);

    @Query("select a from Company a where delFlag = 0 and status = 1 and a.category.code = ?1")
    Company findParAndFelAndStatus(String code);
}
