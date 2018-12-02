package com.ideal.manage.dsp.repository.system;

import com.ideal.manage.dsp.bean.system.Menu;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends BaseRepository<Menu,Long> {
    List<Menu> findAllByDelFlag(Long delFlag);

    @Query("select a from Menu a where a.delFlag = 0 and a.type = 1 and a.parentMenu is null ")
    List<Menu> findAllParentMenu();

    @Query("select a from Menu a where a.delFlag = 0 and a.type = 1 and a.parentMenu is not null")
    List<Menu> findAllChildMenu();
}
