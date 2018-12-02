package com.ideal.manage.dsp.util;

import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class HttpRequests {
    public static List<SpecificationOperator> getParametersStartingWith(HttpServletRequest request, String prefix){
        List<SpecificationOperator> specificationOperators = new ArrayList<>();
        if(request == null){
            return specificationOperators;
        }
//        Enumeration paramNames = request.getParameterNames();
        Map<String,String[]> paramsMap = request.getParameterMap();
        Set<String> keys = paramsMap.keySet();
        for(String key :keys){
            if(key.startsWith(prefix)){
                String [] opers = key.split("_");
                if(opers.length != 3){
                    continue;
                }

                String[] value = paramsMap.get(key);
                if(value.length == 1){
                    if(StringUtils.isEmpty(value[0])){
                        continue;
                    }
                    SpecificationOperator specificationOperator = new SpecificationOperator(opers[2],value[0],opers[1]);
                    specificationOperators.add(specificationOperator);
                }else{
                    SpecificationOperator specificationOperator = new SpecificationOperator(opers[2],value,opers[1]);
                    specificationOperators.add(specificationOperator);
                }
            }
        }

        return specificationOperators;
    }
}
