package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.NewsInformation;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.NewsInformationRepository;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liuhang on 2018/3/9.
 */

@Service
public class NewsInformationService {

    @Resource
    private NewsInformationRepository newsInformationRepository;


    /**
     * 根据条件获取所有对象
     * @param operators
     * @param pageable
     * @return
     */
    public Page<NewsInformation> findAll(List<SpecificationOperator> operators, Pageable pageable) {

        MySpecification<NewsInformation> mySpecifications = new MySpecification<>(operators);
        return newsInformationRepository.findAll(mySpecifications, pageable);

    }

    /**
     * 获取新闻资讯数据
     * @param pageNum
     * @param request
     * @return
     */
    public Page<NewsInformation> getNewsInformationData(int pageNum, HttpServletRequest request) {

        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"editDate");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        operators.add(operator);

        Pageable pageable = new PageRequest(pageNum,10,sort);

        return findAll(operators, pageable);
    }

    /**
     * 通过 id查询数据
     */
    public NewsInformation findNewsInformationById(Long id){
        return newsInformationRepository.findNewsInformationById(id);
    }


    /**
     * 新增新闻资讯
     */
    @Transactional
    public NewsInformation addNewsInformation(NewsInformation newsInformation){

        MyShiroRealm.ShiroUser userInfo = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Timestamp nowTime = Timestamp.valueOf(time);

        newsInformation.setDelFlag(0L);
        newsInformation.setStatus(0L);   //未上线
        newsInformation.setCreateUser(new User(userInfo.getId()));
        newsInformation.setCreateDate(nowTime);
        newsInformation.setEditUser(new User(userInfo.getId()));
        newsInformation.setEditDate(nowTime);

        return newsInformationRepository.save(newsInformation);
    }

    /**
     * 修改新闻资讯
     */
    @Transactional
    public NewsInformation editNewsInformation(NewsInformation newsInformation){

        MyShiroRealm.ShiroUser userInfo = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Timestamp nowTime = Timestamp.valueOf(time);

        newsInformation.setEditUser(new User(userInfo.getId()));
        newsInformation.setEditDate(nowTime);

        return newsInformationRepository.save(newsInformation);
    }

    /**
     *  修改新闻资讯状态
     */
    @Transactional
    public NewsInformation statusUpdate(Long id,Long status){

        NewsInformation newsInformation = findNewsInformationById(id);
        newsInformation.setStatus(status);
        return newsInformationRepository.save(newsInformation);
    }

    /**
     * 批量删除新闻资讯
     */
    @Transactional
    public void delNewsInformation(Long[] ids){

        NewsInformation newsInformation = null;
        for(Long id : ids){
            newsInformation = newsInformationRepository.findNewsInformationById(id);
            newsInformation.setDelFlag(1L);
            newsInformationRepository.save(newsInformation);
        }
    }


    /**
     *   HTML页面的
     *   查询新闻资讯
     *
     */
    public Page<NewsInformation> findAllOnType(Long typeId,Integer pageNum,Integer pageSize){

        //List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request, "Q_");
        Sort sort = new Sort(Sort.Direction.DESC, "editDate");
       /* SpecificationOperator operator = new SpecificationOperator("delFlag", "0", "EQ");
        SpecificationOperator operator2 = new SpecificationOperator("status", "1", "EQ");
        if(typeId != 0){
            SpecificationOperator operator3 = new SpecificationOperator("type.id", typeId, "EQ");
            operators.add(operator3);
        }

        operators.add(operator);
        operators.add(operator2);*/
        if (pageSize == null) {
            pageSize = 10;
        }
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
       String sql="select t.id,t.name,(select u.name from t_user u where u.id=t.create_user),t.create_date from t_news_information t " +
               " where t.del_flag=0 and t.status=1";

       if(typeId!=null&&typeId!=0){
           sql+=" and t.type="+typeId;
       }


        String[] str=new String[]{"id","name","createUserName","createDate"};

        Page<NewsInformation> pages=newsInformationRepository.findAllBySql(sql,pageable,NewsInformation.class,str);

        return pages;
    }

}
