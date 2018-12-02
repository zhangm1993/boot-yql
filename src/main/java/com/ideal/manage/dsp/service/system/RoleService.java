package com.ideal.manage.dsp.service.system;

import com.ideal.manage.dsp.bean.DTO.ZtreeDto;
import com.ideal.manage.dsp.bean.system.*;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.system.RoleRepository;
import com.ideal.manage.dsp.util.HttpRequests;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig( cacheNames = "roleCache")
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Resource
    private ParameterService parameterService;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleMenuService roleMenuService;


    /**
     * 分页 根据创建时间排序
     * @param pageNum
     * @return
     */
    @Cacheable(value = "rolePage")
    public Page<Role> findAll(int pageNum, HttpServletRequest request){
        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        operators.add(operator);
        MySpecification<Role> mySpecifications = new MySpecification<>(operators);
        Pageable pageable = new PageRequest(pageNum,15,sort);
        Page<Role> page = roleRepository.findAll(mySpecifications,pageable);
        return page;
    }



    /**
     * 保存
     * @param role
     */
    @CacheEvict(value = "rolePage",allEntries=true)
    public Role saveOne(Role role){
        MyShiroRealm.ShiroUser shiroUser = (MyShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        User user = new User();
        user.setId(shiroUser.getId());
        role.setCreateUser(user);
        role.setDelFlag(0L);
        role.setCreateDate(new Date());
        role.setStatus(1L);
        Role role1 = roleRepository.save(role);
        return role1;
    }

    /**
     * 根据ID 获取role
     * @param id
     * @return
     */
    public Role findById(Long id){
        Role role = roleRepository.findById(id);
        return role;
    }

    @CacheEvict(value = "rolePage",allEntries=true)
    public void delRole(Long[] ids){
        List<Role> roles = roleRepository.findAll(Arrays.asList(ids));
        for(Role role : roles){
            role.setDelFlag(1L);
        }
        roleRepository.save(roles);
    }

    public List<Role> findAllRoles(){
        List<Role> roles = roleRepository.findAllRoles();
        return roles;
    }

    /**
     * 获取指定身份的角色
     * @param name
     */
    public List<Role> findByType(String name){
        Parameter parameter = parameterService.findByCodeAndRemark("10000",name);
        List<Role> roles = roleRepository.findByType(parameter);
        return roles;
    }

    public List<ZtreeDto> roleAuthorization(Long roleId){
        if(roleId == null){
            return null;
        }
        List<ZtreeDto> ztreeDtos = new ArrayList<>();
        List<Menu> menus = menuService.findAllMenu();
        for(Menu menu : menus){
            RoleMenu roleMenu = roleMenuService.findByRoleAndMenu(roleId,menu.getId());
            if(roleMenu == null){
                ztreeDtos.add(new ZtreeDto(menu.getId(),menu.getParentMenu() == null ? 0L : menu.getParentMenu().getId() ,menu.getTitle(),false,false));
            }else{
                ztreeDtos.add(new ZtreeDto(menu.getId(),menu.getParentMenu() == null ? 0L : menu.getParentMenu().getId() ,menu.getTitle(),false,true));
            }
        }
        return ztreeDtos;
    }
}
