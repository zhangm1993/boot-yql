package com.ideal.manage.dsp.repository.industry;

import com.ideal.manage.dsp.bean.industry.Blogroll;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogrollRepository extends BaseRepository<Blogroll, Long> {

    /**
     * 获取线上数据
     * @param id
     * @return
     */
    @Query("select a from Blogroll a where delFlag = 0 and status = 1")
    List<Blogroll> findBlogrollOnline(Long id);
}
