package com.ideal.manage.dsp.config.shiro;

import com.ideal.manage.dsp.bean.system.Menu;
import com.ideal.manage.dsp.bean.system.Role;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.service.system.RoleMenuService;
import com.ideal.manage.dsp.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.List;

@Configuration
public class MyShiroRealm extends AuthorizingRealm{

    @Autowired
    private UserService userService;
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthenticationInfo = new SimpleAuthorizationInfo();
        ShiroUser shiroUser = (ShiroUser) principalCollection.getPrimaryPrincipal();
        User user = userService.findById(shiroUser.getId());
        Role role = user.getRole();
        simpleAuthenticationInfo.addRole(role.getName());
        List<String> buttons = roleMenuService.getAllButton(role);
        if(buttons != null && !buttons.isEmpty()){
            simpleAuthenticationInfo.addStringPermissions(buttons);
        }
        System.out.println("权限===");
        return simpleAuthenticationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("身份验证====");
        //获取用户输入的用户名密码
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String username = usernamePasswordToken.getUsername();
        String password = String.valueOf(usernamePasswordToken.getPassword());
        User user = userService.findByLoginNameAndPassword(username,password);
        if(user == null){
            throw new IncorrectCredentialsException();
        }

        String ip = usernamePasswordToken.getHost();

        ShiroUser shiroUser = new ShiroUser();
        shiroUser.setId(user.getId());
        shiroUser.setUsername(user.getLoginName());
        shiroUser.setPassword(user.getPassword());

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(shiroUser,password,getName());

        return simpleAuthenticationInfo;
    }

    public static class ShiroUser implements Serializable{
        private Long id;
        private String username;
        private String password;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
