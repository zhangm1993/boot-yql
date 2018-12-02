package com.ideal.manage.dsp.controller.industry;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.Partner;
import com.ideal.manage.dsp.service.industry.PartnerService;
import com.ideal.manage.dsp.util.PageDtoUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("industry")
public class PartnerController {


    @Resource
    private PartnerService partnerService;


    /**
     * 合作伙伴列表请求
     * @param model
     */
    @RequestMapping("partner_list")
    public void partnerList(Model model) {}

    /**
     * 列表数据
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("partner_data")
    @ResponseBody
    public PageDto partnerData(int pageNum, HttpServletRequest request) {

        Page<Partner> page = partnerService.getPartnerData(pageNum, request);
        return PageDtoUtil.getPageDto(page);

    }


    /**
     * 新增请求
     * @param model
     */
    @RequestMapping("partner_add")
    public void partnerAdd(Model model) {}

    /**
     * 修改请求
     * @param model
     */
    @RequestMapping("partner_edit")
    public void partnerEdit(Model model, Long id) {

        Partner partner = partnerService.getPartnerById(id);
        model.addAttribute("partner", partner);
    }

    /**
     * 保存方法
     */
    @RequestMapping("partner_save")
    @ResponseBody
    public Result partnerSave(HttpServletRequest request,
                              @RequestParam(value = "picture", required = false) MultipartFile picture,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "id", required = false) Long id) throws Exception {

        partnerService.partnerSave(picture, name, id);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    /**
     *  状态修改
     * @param request
     * @param status   1:上线  0:下线
     * @param id
     * @return
     */
    @RequestMapping("partner_status_update")
    @ResponseBody
    public Result partnerStatusUpdate(HttpServletRequest request,
                                      String status, Long id) {

        //保存操作
        String msg = partnerService.partnerStatusUpdate(status, id);

        Result result = new Result();
        result.setType("alert");
        result.setMessage(msg);
        return result;
    }


    /**
     * 批量删除
     * @param ids
     * @return
     */
    @RequestMapping("partner_delete")
    @ResponseBody
    public Result partnerDelete(Long[] ids){

        partnerService.partnerDelete(ids);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }
}
