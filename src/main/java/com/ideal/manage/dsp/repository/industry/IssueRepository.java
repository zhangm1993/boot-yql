package com.ideal.manage.dsp.repository.industry;

import com.ideal.manage.dsp.bean.industry.Issue;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IssueRepository extends BaseRepository<Issue,Long> {

    @Query("update Issue set status=?1 where id=?2")
    @Modifying
    int updateStatus(Long status,Long id);

    @Query("update Issue set delFlag=?1 where id in(?2)")
    @Modifying
    int updateDelFlag(Long delFlag,List<Long> id);
}
