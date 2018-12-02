package com.ideal.manage.dsp.repository.industry;

import com.ideal.manage.dsp.bean.industry.Partner;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartnerRepository extends BaseRepository<Partner, Long> {

    /**
     * 获取 在线的数据
     * @return
     */
    @Query("select a from Partner a where delFlag = 0 and status = 1")
    List<Partner> getOnline();
}
