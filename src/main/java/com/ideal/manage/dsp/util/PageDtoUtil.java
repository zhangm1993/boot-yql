package com.ideal.manage.dsp.util;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageDtoUtil {

    public static PageDto getPageDto(Page<?> page) {

        List<?> pageList = page.getContent();
        long total = page.getTotalElements();

        PageDto pageDto = new PageDto();
        pageDto.setRows(pageList);
        pageDto.setTotal(total);
        return pageDto;

    }
}
