package com.ideal.manage.dsp.service.system;

import com.ideal.manage.dsp.bean.system.Customer;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.Role;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.system.CustomerRepository;
import com.ideal.manage.dsp.util.HttpRequests;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Page<Customer> findAll(int pageNum, HttpServletRequest request){
        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        operators.add(operator);
        MySpecification<Customer> mySpecifications = new MySpecification<>(operators);
        Pageable pageable = new PageRequest(pageNum,15,sort);
        Page<Customer> page = customerRepository.findAll(mySpecifications,pageable);
        return page;
    }

    public Customer findById(Long id){
        Customer customer = customerRepository.findOne(id);
        return customer;
    }

    public List<Customer> findAllCustomer(){
        List<Customer> customers = customerRepository.findAllCustomer();
        return customers;
    }

    public void saveCustomer(Long id,String name,Long manager,Long companyType,String companyTelephone,
                             String contactName,String contactMobile,String email,String address,
                             String legalPerson,String businessNumber,String bankAccount,String remark){
        MyShiroRealm.ShiroUser shiroUser = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Customer customer = new Customer();
        if(id != null){
            customer = findById(id);
            customer.setUpdateUser(new User(shiroUser.getId())); //修改人
            customer.setDateLastUpdated(new Date());             //修改时间
        }else {
            customer.setCreateUser(new User(shiroUser.getId()));
            customer.setCreateDate(new Date());
            customer.setDelFlag(0L);
        }
        customer.setName(name);
        customer.setManager(new User(manager));
        customer.setCompanyType(new Parameter(companyType));
        customer.setCompanyTelephone(companyTelephone);
        customer.setContactName(contactName);
        customer.setContactMobile(contactMobile);
        customer.setEmail(email);
        customer.setAddress(address);
        customer.setLegalPerson(legalPerson);
        customer.setBusinessNumber(businessNumber);
        customer.setBankAccount(bankAccount);
        customer.setRemark(remark);
        customerRepository.save(customer);
    }

    public void delCustomer(Long [] ids){
        List<Customer> customers = customerRepository.findAll(Arrays.asList(ids));
        for(Customer customer : customers){
            customer.setDelFlag(1L);
        }
        customerRepository.save(customers);
    }


}
