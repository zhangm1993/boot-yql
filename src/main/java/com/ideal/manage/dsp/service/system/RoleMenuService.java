package com.ideal.manage.dsp.service.system;

import com.ideal.manage.dsp.bean.system.Menu;
import com.ideal.manage.dsp.bean.system.Role;
import com.ideal.manage.dsp.bean.system.RoleMenu;
import com.ideal.manage.dsp.repository.system.RoleMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig( cacheNames = "roleMenuCache")
public class RoleMenuService {
    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Cacheable(value = "menuList")
    public List<Menu> getAllMenu(Role role){
        List<Menu> result = new ArrayList<>();
        List<Menu> menuList = roleMenuRepository.getAllMenu(role,1L);
        for(Menu parent : menuList){
            if(parent.getParentMenu() != null){
                continue;
            }
            List<Menu> menus = new ArrayList<>();
            for(Menu child : menuList){
                if(child.getParentMenu() == null){
                    continue;
                }
                if(child.getParentMenu().getId() == parent.getId()){
                    menus.add(child);
                }
            }
            parent.setSubMenu(menus);
            result.add(parent);
        }

        return result;
    }

    public List<Menu> findAllButton(Role role){
        List<Menu> menuList = roleMenuRepository.getAllMenu(role,2L);
        return menuList;
    }

    /**
     * 根据角色 发现其所有的BUTTON
     * @param role
     * @return
     */
    public List<String> getAllButton(Role role){
        List<String> buttons = roleMenuRepository.getAllButtonCodeByRole(role);
        return buttons;
    }

    public RoleMenu findByRoleAndMenu(Long roleId, Long menuId){
        RoleMenu roleMenu = roleMenuRepository.findByRoleAndMenu(roleId, menuId);
        return roleMenu;
    }

    @Transactional
    public void roleAuthorizationSave(Long roleId,String treeIds){
        roleMenuRepository.deleteByRoleId(roleId);
        List<RoleMenu> roleMenus = new ArrayList<>();
        String []nodes = treeIds.split(",");
        for(String node : nodes){
            RoleMenu roleMenu = new RoleMenu(new Role(roleId),new Menu(Long.parseLong(node)));
            roleMenus.add(roleMenu);
        }
        roleMenuRepository.save(roleMenus);
    }

}
