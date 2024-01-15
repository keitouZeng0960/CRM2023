package com.workspace.crm.settings.web.controller;

import com.workspace.crm.commons.constants.Constants;
import com.workspace.crm.commons.pojo.ReturnObject;
import com.workspace.crm.settings.pojo.DictionaryType;
import com.workspace.crm.settings.service.DictionaryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DicTypeController {
    @Autowired
    DictionaryTypeService dictionaryTypeService;

    @RequestMapping("settings/dictionary/type/index.do")
    public String index() {
        return "settings/dictionary/type/index";
    }
    @RequestMapping("settings/dictionary/type/toTypeSave.do")
    public String toTypeSave(){return "settings/dictionary/type/save";}
    @RequestMapping("settings/dictionary/type/toTypeIndex.do")
    public String toTypeIndex(){return "settings/dictionary/type/index";}
    @RequestMapping("settings/dictionary/type/toTypeEdit.do")
    public String toTypeEdit(){return "settings/dictionary/type/edit";}



    //查询所有的数据字典类型
    @RequestMapping("settings/dictionary/type/queryAllDicTypeByPages.do")
    @ResponseBody
    public Object queryAllDicTypeByPages(Integer pageNo, Integer pageSize){
        List<DictionaryType> dictionaryTypeList = dictionaryTypeService.queryAllDicTypeByPages((pageNo-1)*pageSize,pageSize);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("typeList",dictionaryTypeList);
        return map;
    }

    //保存数据字典类型
    @RequestMapping("settings/dictionary/type/saveDicType.do")
    @ResponseBody
    public Object saveDicType(DictionaryType type){
        ReturnObject ro =new ReturnObject();
            try {
                int count = dictionaryTypeService.saveDicType(type);
                if (count > 0) {
                    ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                } else {
                    ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                    ro.setMessage("保存失败，稍后重试...");
                }
            } catch (Exception e) {
                e.printStackTrace();
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("编码不可重复，请重新填写");
            }
        return ro;
    }

    //批量删除
    @RequestMapping("settings/dictionary/type/deleteDicTypeByCode.do")
    @ResponseBody
    public Object deleteDicTypeByCode(String[] code){
        ReturnObject ro=new ReturnObject();
        try{
            int count=dictionaryTypeService.deleteDicTypeByCodes(code);
            if(count>0){
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统繁忙，请稍后重试...");
            }
        }catch (Exception e){
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙，请稍后重试...");
        }
        return ro;
    }

    //修改数据
    @RequestMapping("settings/dictionary/type/editDicTypeByCode.do")
    @ResponseBody
    public Object editDicTypeByCode(DictionaryType type){
        ReturnObject ro = new ReturnObject();
        try{
            int count = dictionaryTypeService.updateDicTypeByCode(type);
            if(count>0){
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统繁忙，请稍后再试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙，请稍后再试...");
        }
        return ro;
    }

    @RequestMapping("settings/dictionary/type/toTypeEditWithData.do")
    public ModelAndView toTypeEditWithData(String code){
        DictionaryType dictionaryType = dictionaryTypeService.queryDicTypeByCode(code);
        ModelAndView mv=new ModelAndView();
        mv.addObject("type",dictionaryType);
        mv.setViewName("settings/dictionary/type/edit");
        return mv;
    }


}

