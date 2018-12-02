package com.ideal.manage.dsp.repository.system;

import com.ideal.manage.dsp.bean.system.Customer;
import com.ideal.manage.dsp.repository.framework.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends BaseRepository<Customer,Long> {
    @Query("select a from Customer a where delFlag = 0")
    List<Customer> findAllCustomer();
}
