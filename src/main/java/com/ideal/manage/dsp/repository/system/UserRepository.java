package com.ideal.manage.dsp.repository.system;

import com.ideal.manage.dsp.bean.system.Role;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;


public interface UserRepository extends BaseRepository<User,Long> {
    User findByLoginNameAndPassword(String loginName,String password);

    List<User> findByLoginName(String loginName);

    List<User> findByRoleIn(List<Role> roles);
}
