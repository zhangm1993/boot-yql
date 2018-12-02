package com.ideal.manage.dsp.repository.framework;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

public class MySpecification<T> implements Specification<T> {

    private List<SpecificationOperator> specificationOperatorList;

    public MySpecification (List<SpecificationOperator> specificationOperatorList){
        this.specificationOperatorList = specificationOperatorList;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate result = null;
        for(SpecificationOperator oper : specificationOperatorList){
            Predicate predicate = generatePredicate(root,oper,criteriaBuilder);
            if(result != null){
                result = criteriaBuilder.and(result,predicate);
            }else{
                result = criteriaBuilder.and(predicate);
            }

        }
        return result;
    }

    public Predicate generatePredicate(Root<T> root, SpecificationOperator oper, CriteriaBuilder criteriaBuilder){
        String[]args = oper.getKey().split("\\.");
        Path expression = root.get(args[0]);
        for(int i = 1;i < args.length;i++){
            expression = expression.get(args[i]);
        }
        switch (oper.getOper()){
            case "EQ" :
                return criteriaBuilder.equal(expression,oper.getValue());
            case "ISNULL" :
                return criteriaBuilder.isNull(expression);
            case "LIKE":
                return criteriaBuilder.like(expression,"%"+oper.getValue()+"%");
            case "GT" :
                return criteriaBuilder.greaterThan(expression,(Comparable)oper.getValue());
            case "LT" :
                return criteriaBuilder.lessThan(expression,(Comparable)oper.getValue());
            case "GTE" :
                return criteriaBuilder.equal(expression,(Comparable)oper.getValue());
            case "LTE" :
                return criteriaBuilder.equal(expression,(Comparable)oper.getValue());
            case "ISNOTNULL":
                return criteriaBuilder.isNotNull(root.get(oper.getKey()));
            case "IN" :
                return expression.in(oper.getValue());
            case "NEQ":
                return criteriaBuilder.equal(expression,oper.getValue());
        }
        return null;
    }
}
