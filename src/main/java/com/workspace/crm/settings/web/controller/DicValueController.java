package com.workspace.crm.settings.web.controller;

import com.workspace.crm.commons.constants.Constants;
import com.workspace.crm.commons.pojo.ReturnObject;
import com.workspace.crm.commons.utils.IdUtils;
import com.workspace.crm.settings.pojo.DictionaryType;
import com.workspace.crm.settings.pojo.DictionaryValue;
import com.workspace.crm.settings.service.DictionaryTypeService;
import com.workspace.crm.settings.service.DictionaryValueService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DicValueController {
    @Autowired
    DictionaryValueService dictionaryValueService;
    @Autowired
    DictionaryTypeService dictionaryTypeService;

    @RequestMapping("settings/dictionary/value/toValueIndex.do")
    public String toValueIndex(){
        return "settings/dictionary/value/index";
    }

    @RequestMapping("settings/dictionary/value/queryAllValueByPage.do")
    @ResponseBody
    public Object queryAllValueByPage(Integer pageNo, Integer pageSize){
        int beginNo=(pageNo-1)*pageSize;
        List<DictionaryValue> valueList = dictionaryValueService.queryAllDicValueByPages(beginNo, pageSize);
        int countNo=dictionaryValueService.queryCountOfValues();
        Map<String,Object> map=new HashMap<>();
        map.put("valueList",valueList);
        map.put("countNo",countNo);
        return map;
    }

    @RequestMapping("settings/dictionary/value/toValueSave.do")
    public String toValueSave(){return "settings/dictionary/value/save";}

    @RequestMapping("settings/dictionary/value/toValueEdit.do")
    public ModelAndView toValueEdit(String id){
        ModelAndView mv=new ModelAndView();
        DictionaryValue dictionaryValue = dictionaryValueService.queryValueById(id);
        mv.addObject("selectedValue",dictionaryValue);
        mv.setViewName("settings/dictionary/value/edit");
        return mv;
    }


    @RequestMapping("settings/dictionary/value/queryAllDicType.do")
    @ResponseBody
    public Object queryAllDicType(){
        List<DictionaryType> types = dictionaryTypeService.queryAllTypes();
        Map<String,Object> map=new HashMap<>();
        map.put("typeList",types);
        return map;
    }

    @RequestMapping("settings/dictionary/value/saveDicValue.do")
    @ResponseBody
    public Object saveDicValue(DictionaryValue value){
        value.setId(IdUtils.createId());
        ReturnObject ro=new ReturnObject();
        try {
            int count = dictionaryValueService.saveValue(value);
            if(count>0){
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统繁忙，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙，请稍后重试");
        }
        return ro;
    }


    @RequestMapping("settings/dictionary/value/editValueById")
    @ResponseBody
    public Object editValueById(DictionaryValue value){
        ReturnObject ro=new ReturnObject();
        try{
            int count=dictionaryValueService.editValueById(value);
            if(count>0){
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统繁忙，请稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙，请稍后再试");
        }
        return ro;
    }

    @RequestMapping("settings/dictionary/value/removeValueById.do")
    @ResponseBody
    public Object removeValueById(String[] id) {
        ReturnObject ro = new ReturnObject();
        try {
            int count = dictionaryValueService.deleteValueByIds(id);
            if (count > 0) {
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统繁忙，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙，请稍后再试");
        }
        return ro;
    }
}
