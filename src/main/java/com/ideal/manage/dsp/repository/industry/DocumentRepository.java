package com.ideal.manage.dsp.repository.industry;

import com.ideal.manage.dsp.bean.industry.Document;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends BaseRepository<Document,Long> {

   @Query("update Document set delFlag=?2,status=?3 where id in(?1)")
   @Modifying
   int updateDoc(List<Long> ids,Long delFlag,Long status);

    @Query("update Document set status=?2 where id in(?1)")
    @Modifying
   int updateStatusDoc(List<Long> id,Long status);
}
