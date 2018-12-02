package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.Blogroll;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.BlogrollRepository;
import com.ideal.manage.dsp.service.system.ParameterService;
import com.ideal.manage.dsp.util.HttpRequests;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class BlogrollService {


    @Resource
    private BlogrollRepository blogrollRepository;

    @Resource
    private ParameterService parameterService;

    /**
     * 获取 友情链接数据
     * @param pageNum
     * @param request
     * @return
     */
    public Page<Blogroll> getBlogrollData(int pageNum, HttpServletRequest request) {

        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        operators.add(operator);

        Pageable pageable = new PageRequest(pageNum,15,sort);

        return findAll(operators, pageable);
    }

    /**
     *  获取数据
     * @param operators
     * @param pageable
     * @return
     */
    private Page<Blogroll> findAll(List<SpecificationOperator> operators, Pageable pageable) {

        MySpecification<Blogroll> mySpecifications = new MySpecification<Blogroll>(operators);
        return blogrollRepository.findAll(mySpecifications, pageable);
    }


    /**
     *  优秀案例 上下线
     * @param status
     * @param id
     * @return
     */
    public String blogrollStatusUpdate(String status, Long id) {

        String msg = "";

        //封装数据
        Blogroll blogroll = blogrollRepository.findOne(id);
        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        blogroll.setStatus(status);
        blogroll.setUpdateTime(new Date());
        blogroll.setUpdateUser(new User(user.getId()));

        if (status.equals("1")) {    //上线


        } else {                     //下线

        }


        return msg;
    }
}
