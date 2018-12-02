package com.ideal.manage.dsp.service.system;

import com.ideal.manage.dsp.bean.system.Customer;
import com.ideal.manage.dsp.bean.system.Role;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.system.UserRepository;
import com.ideal.manage.dsp.util.HttpRequests;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@CacheConfig( cacheNames = "userCache")
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CustomerService customerService;

    public Page<User> findAll(int pageNum, HttpServletRequest request){
        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        operators.add(operator);
        MySpecification<User> mySpecifications = new MySpecification<>(operators);
        Pageable pageable = new PageRequest(pageNum,15,sort);
        Page<User> page = userRepository.findAll(mySpecifications,pageable);
        return page;
    }


    public User findByLoginNameAndPassword(String loginName,String password){
        User user = userRepository.findByLoginNameAndPassword(loginName,password);
        return user;
    }

    public void findAllMenu(){
        User user = userRepository.findByLoginNameAndPassword("xbb","xbb");
        Set<Role> set = user.getRoles();
        Long id = user.getId();
        System.out.println(set.size());
    }

    /**
     * 根据ID获取user
     * @param id
     * @return
     */
//    @Cacheable(value = "user_id")
    public User findById (Long id){
        User user = userRepository.findOne(id);
        return user;
    }

    public void saveUser(Long id,String loginName,String password,
                         String name,Long sex,String telephone,
                         String mobile,String email,Long role,
                         Long customer,String jobTitle,String remark){
        MyShiroRealm.ShiroUser shiroUser = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        User user = new User();
        if(id != null){
            user = findById(id);
            user.setUpdateUser(new User(shiroUser.getId()));
            user.setDateLastUpdated(new Date());
        }else {
            user.setCreateDate(new Date());
            user.setCreateUser(new User(shiroUser.getId()));
            user.setDelFlag(0L);
        }
        user.setLoginName(loginName);
        user.setPassword(password);
        user.setName(name);
        user.setSex(sex);
        user.setTelephone(telephone);
        user.setMobile(mobile);
        user.setEmail(email);

        if(role != null){
            Role r = roleService.findById(role);
            user.setRole(r);
        }
        if(customer != null){
            Customer c = customerService.findById(customer);
            user.setCustomer(c);
        }
        user.setJobTitle(jobTitle);
        user.setRemark(remark);
        userRepository.save(user);
    }

    public void delUser(Long [] ids){
        List<User> users = userRepository.findAll(Arrays.asList(ids));
        for(User user : users){
            user.setDelFlag(1L);
        }
        userRepository.save(users);
    }

    /**
     * 获取指定身份名称的用户
     * @param name
     */
    public List<User> findByParameterName(String name){
        List<Role> roles = roleService.findByType(name);
        List<User> users = userRepository.findByRoleIn(roles);
        return users;
    }

    public void testFind(){
//        List<Long> ids = new ArrayList<>();
//        ids.add(1L);
//        SpecificationOperator specificationOperator = new SpecificationOperator("id",ids,"IN");
//        List<SpecificationOperator> specificationOperators = new ArrayList<>();
//        specificationOperators.add(specificationOperator);
//        MySpecification mySpecification = new MySpecification(specificationOperators);
//        List<User> userList = userRepository.findAll(mySpecification);
//        System.out.println(userList.size());

    }
}
