package com.workspace.crm.settings.service;

import com.workspace.crm.settings.pojo.DictionaryType;

import java.util.List;

public interface DictionaryTypeService {

    //查询所有的数据字典类型
    List<DictionaryType> queryAllDicTypeByPages(int beginNo,int pageSize);

    //保存创建的数据字典类型
    int saveDicType(DictionaryType type);

    //根据code批量删除数据字典类型
    int deleteDicTypeByCodes(String[] codeList);

    int updateDicTypeByCode(DictionaryType type);

    DictionaryType queryDicTypeByCode(String code);

    List<DictionaryType> queryAllTypes();
}
