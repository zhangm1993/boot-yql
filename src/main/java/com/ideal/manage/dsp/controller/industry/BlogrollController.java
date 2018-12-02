package com.ideal.manage.dsp.controller.industry;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.Blogroll;
import com.ideal.manage.dsp.service.industry.BlogrollService;
import com.ideal.manage.dsp.util.PageDtoUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("industry")
public class BlogrollController {

    @Resource
    private BlogrollService blogrollService;

    /**
     * 友情链接列表请求
     * @param model
     */
    @RequestMapping("blogroll_list")
    public void blogrollList(Model model) {
    }

    /**
     * 优秀案例列表数据
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("bolgroll_data")
    @ResponseBody
    public PageDto blogrollData(int pageNum, HttpServletRequest request) {

        Page<Blogroll> page = blogrollService.getBlogrollData(pageNum, request);
        return PageDtoUtil.getPageDto(page);
    }

    /**
     *  友情链接状态修改
     * @param request
     * @param status   1:上线  0:下线
     * @param id
     * @return
     */
    @RequestMapping("blogroll_status_update")
    @ResponseBody
    public Result blogrollStatusUpdate(HttpServletRequest request,
                                   String status, Long id) {

        //保存操作
        String msg = blogrollService.blogrollStatusUpdate(status, id);

        Result result = new Result();
        result.setType("alert");
        result.setMessage(msg);
        return result;
    }
}
