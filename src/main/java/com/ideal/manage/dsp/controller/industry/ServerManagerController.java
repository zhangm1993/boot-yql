package com.ideal.manage.dsp.controller.industry;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.Partner;
import com.ideal.manage.dsp.bean.industry.ServerManager;
import com.ideal.manage.dsp.service.industry.ServerManagerService;
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
public class ServerManagerController {

    @Resource
    private ServerManagerService serverManagerService;

    /**
     * 列表请求
     * @param model
     */
    @RequestMapping("server_list")
    public void partnerList(Model model) {}

    /**
     * 列表数据
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("server_data")
    @ResponseBody
    public PageDto partnerData(int pageNum, HttpServletRequest request) {

        Page<ServerManager> page = serverManagerService.getServerManagerData(pageNum, request);
        return PageDtoUtil.getPageDto(page);
    }

    /**
     * 新增请求
     * @param model
     */
    @RequestMapping("server_add")
    public void serverAdd(Model model) {}

    /**
     * 修改请求
     * @param model
     */
    @RequestMapping("server_edit")
    public void serverEdit(Model model, Long id) {

        ServerManager serverManager = serverManagerService.getServerManagerById(id);
        model.addAttribute("serverManager", serverManager);
    }


    /**
     * 保存方法
     */
    @RequestMapping("server_save")
    @ResponseBody
    public Result serverSave(HttpServletRequest request,
                              @RequestParam(value = "picture", required = false) MultipartFile picture,
                              @RequestParam(value = "backgroundPicture", required = false) MultipartFile backgroundPicture,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "description", required = false) String description,
                              @RequestParam(value = "id", required = false) Long id) throws Exception {

        serverManagerService.serverManagerSave(picture, backgroundPicture, name, description, id);

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
    @RequestMapping("server_status_update")
    @ResponseBody
    public Result serverStatusUpdate(HttpServletRequest request,
                                      String status, Long id) {

        //保存操作
        String msg = serverManagerService.serverManagerStatusUpdate(status, id);

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
    @RequestMapping("server_delete")
    @ResponseBody
    public Result serverDelete(Long[] ids){

        serverManagerService.serverManagerDelete(ids);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }
}
