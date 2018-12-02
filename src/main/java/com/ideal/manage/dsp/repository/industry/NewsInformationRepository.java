package com.ideal.manage.dsp.repository.industry;

import com.ideal.manage.dsp.bean.industry.NewsInformation;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by liuhang on 2018/3/9.
 */

@Repository
public interface NewsInformationRepository extends BaseRepository<NewsInformation,Long> {

    @Query("select t from NewsInformation t where t.id=?1 and t.delFlag=0")
    NewsInformation findNewsInformationById(Long id);

}
