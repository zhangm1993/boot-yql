package com.ideal.manage.dsp.service.system;

import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.system.ParameterRepository;
import com.ideal.manage.dsp.util.HttpRequests;
import org.apache.shiro.SecurityUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ParameterService {
    @Resource
    private ParameterRepository parameterRepository;

    public Page<Parameter> findAllParent(int pageNum,HttpServletRequest request,Long parentId){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        SpecificationOperator delFlag = new SpecificationOperator("delFlag","0","EQ");
        SpecificationOperator parent = null;
        if(parentId == null){
            parent = new SpecificationOperator("parent",null,"ISNULL");
        }else{
            parent = new SpecificationOperator("parent.id",parentId,"EQ");
        }
        operators.add(delFlag);
        operators.add(parent);
        MySpecification<Parameter> mySpecifications = new MySpecification<>(operators);
        Pageable pageable = new PageRequest(pageNum,10,sort);
        Page<Parameter> page = parameterRepository.findAll(mySpecifications,pageable);
        return page;
    }

    /**
     * 验证CODE是否唯一
     * @param code
     * @return
     */
    public Parameter findByCode(String code){
        List<Parameter> parameters = parameterRepository.findByCode(code);
        if(parameters.size() > 0){
            return parameters.get(0);
        }else {
            return null;
        }
    }

    public void parameterSave(Long id,String code,String remark,String name,Long parentId){
        MyShiroRealm.ShiroUser shiroUser = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        User user = new User();
        user.setId(shiroUser.getId());
        Parameter parameter = new Parameter();
        if(id != null){
            parameter = findById(id);
            parameter.setUpdateTime(new Date());
        }else{
            if(parentId != null){
                Parameter p = parameterRepository.findById(parentId);
                parameter.setParent(p);
            }
            parameter.setCreateUser(user);
            parameter.setDelFlag(0L);
            parameter.setStatus(0L);
            parameter.setCreateTime(new Date());
            parameter.setCode(code);
        }
        parameter.setRemark(remark);
        parameter.setName(name);

        parameterRepository.save(parameter);
    }

    public Parameter findById(Long id){
        Parameter parameter = parameterRepository.findById(id);
        return parameter;
    }


    public void delParameter(Long[] ids){
        List<Parameter> parameters = parameterRepository.findAll(Arrays.asList(ids));
        for(Parameter parameter : parameters){
            parameter.setDelFlag(1L);
        }
        parameterRepository.save(parameters);
    }

    public List<Parameter> findByParent(Parameter parameter){
        List<Parameter> parameters = parameterRepository.findByParent(parameter);
        return parameters;
    }

    /**
     * 根据父CODE 查找所有的子参数
     * @param code
     * @return
     */
    public List<Parameter> findChildByParentCode(String code){
        Parameter parameter = findByCode(code);
        List<Parameter> parameters = findByParent(parameter);
        return parameters;
    }

    /**
     * 根据指定的父code 子name 获取对应的对象
     * @param code
     * @param name
     * @return
     */
    public Parameter findByCodeAndRemark(String code,String name){
        Parameter p = null;
        List<Parameter> parameters = findChildByParentCode(code);
        for(Parameter parameter : parameters){
            if(!parameter.getName().equals(name)){
                continue;
            }
            p = parameter;
            break;
        }
        return p;
    }


    /**
     * 获取所有子类
     * @param code
     * @return
     */
    public List<ParameterExtend> findByAllChildListByCode(String code) {

        List<ParameterExtend> paramList = new ArrayList<ParameterExtend>();

        List<Parameter> firstParam = findChildByParentCode(code);    //第一级参数

        for (Parameter parameterParent : firstParam) {

            List<Parameter> secondParam = findChildByParentCode(parameterParent.getCode());   //第二级参数
            List<ParameterExtend> paramListChild = new ArrayList<ParameterExtend>();
            for (Parameter parameterChild : secondParam) {

                ParameterExtend parameterExtendChild = new ParameterExtend();
                parameterExtendChild.setId(parameterChild.getId());
                parameterExtendChild.setName(parameterChild.getName());
                parameterExtendChild.setRemark(parameterChild.getRemark());
                paramListChild.add(parameterExtendChild);

            }

            ParameterExtend parameterExtend = new ParameterExtend();
            parameterExtend.setId(parameterParent.getId());
            parameterExtend.setName(parameterParent.getName());
            parameterExtend.setRemark(parameterParent.getRemark());
            parameterExtend.setChildList(paramListChild);

            paramList.add(parameterExtend);
        }

        return paramList;
    }

    /**
     * 保存实体类
     * @param parameter
     */
    public void saveParameter(Parameter parameter) {

        parameterRepository.save(parameter);
    }

}
