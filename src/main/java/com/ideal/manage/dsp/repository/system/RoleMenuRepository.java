package com.ideal.manage.dsp.repository.system;


import com.ideal.manage.dsp.bean.system.Menu;
import com.ideal.manage.dsp.bean.system.Role;
import com.ideal.manage.dsp.bean.system.RoleMenu;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleMenuRepository extends BaseRepository<RoleMenu,Long> {

    @Query("select rm.menu from RoleMenu rm where rm.menu.delFlag = 0 and rm.role = ?1 and rm.menu.type = ?2  order by rm.menu")
    List<Menu> getAllMenu(Role role,Long menuType);

    @Query("select rm.menu.code from RoleMenu rm where rm.menu.delFlag = 0 and rm.role = ?1 and rm.menu.type = 2 ")
    List<String> getAllButtonCodeByRole(Role role);

    @Query("select a from RoleMenu a where a.role.id = ?1 and a.menu.id = ?2")
    RoleMenu findByRoleAndMenu(Long roleId, Long menuId);

    @Modifying
    @Query("delete from RoleMenu a where a.role.id = ?1")
    void deleteByRoleId(Long roleId);
}
