package com.ideal.manage.dsp.repository.industry;

import com.ideal.manage.dsp.bean.industry.ServerManager;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServerManagerRepository extends BaseRepository<ServerManager,Long> {


    @Query("select a from ServerManager a where delFlag = 0 and status = 1")
    List<ServerManager> findOnline();
}
