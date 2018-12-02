package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.Issue;
import com.ideal.manage.dsp.bean.industry.Produce;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.IssueRepository;
import com.ideal.manage.dsp.util.HttpRequests;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class IssueService {

    @Resource
    private IssueRepository issueRepository;


    @Transactional
    public int issUpdate(Long status,Long id){
       return issueRepository.updateStatus(status,id);
    }


    /**
     * 删除的方法
     * @param ids
     * @return
     */
    @Transactional
    public int issueDel(Long[] ids){

        List<Long> ls=Arrays.asList(ids);
       return issueRepository.updateDelFlag(1l,ls);
    }

    /**
     * 保存常见问题
     * @param issue
     * @return
     */
    public Issue save(Issue issue){

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Long userId=user.getId();
        Long id=issue.getId();


        if(id==null){
            issue.setDelFlag(0l);
            issue.setStatus(1l);
            issue.setCreateUser(new User(userId));
            issue.setCreateTime(new Date());
            issue.setUpdateUser(new User(userId));
            issue.setUpdateTime(new Date());
           return issueRepository.save(issue);
        }

        Issue ie=findId(id);
        ie.setTitle(issue.getTitle());
        ie.setContent(issue.getContent());
        ie.setUpdateTime(new Date());
        ie.setUpdateUser(new User(userId));
        if(issue.getSort()!=null){
            ie.setSort(issue.getSort());
        }
        if(issue.getDelFlag()!=null){
            ie.setDelFlag(issue.getDelFlag());
        }
        if(issue.getStatus()!=null){
            ie.setStatus(issue.getStatus());
        }
      return issueRepository.save(ie);
    }


    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    public Page<Issue> findByAllHtml(Integer pageNum,Integer pageSize, HttpServletRequest request){
        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"sort","updateTime");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        SpecificationOperator operator1 = new SpecificationOperator("status","1","EQ");

        operators.add(operator);
        operators.add(operator1);

        if(pageNum==null){
            pageNum=0;
        }

        if(pageSize==null){
            pageSize=10;
        }

        Pageable pageable = new PageRequest(pageNum,pageSize,sort);

        return findAll(operators, pageable);
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    public Page<Issue> findByAll(Integer pageNum,Integer pageSize, HttpServletRequest request){
        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"sort","updateTime");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        operators.add(operator);

        if(pageNum==null){
            pageNum=0;
        }

        if(pageSize==null){
            pageSize=10;
        }

        Pageable pageable = new PageRequest(pageNum,pageSize,sort);

        return findAll(operators, pageable);
    }

    /**
     * 根据条件获取所有对象
     * @param operators
     * @param pageable
     * @return
     */
    public Page<Issue> findAll(List<SpecificationOperator> operators, Pageable pageable) {

        MySpecification<Issue> mySpecifications = new MySpecification<>(operators);
       return issueRepository.findAll(mySpecifications,pageable);

    }

    public Issue findId(Long id){

        return issueRepository.findOne(id);
    }
}
