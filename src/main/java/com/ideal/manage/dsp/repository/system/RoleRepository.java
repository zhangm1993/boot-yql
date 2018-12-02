package com.ideal.manage.dsp.repository.system;


import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.Role;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends BaseRepository<Role,Long> {
    Role findById(Long id);

    @Query("select a from Role a where delFlag = 0")
    List<Role> findAllRoles();

    List<Role> findByType(Parameter parameter);
}
