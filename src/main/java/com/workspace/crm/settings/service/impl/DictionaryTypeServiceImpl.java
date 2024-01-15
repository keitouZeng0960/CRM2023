package com.workspace.crm.settings.service.impl;

import com.workspace.crm.settings.mapper.DictionaryTypeMapper;
import com.workspace.crm.settings.pojo.DictionaryType;
import com.workspace.crm.settings.service.DictionaryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DictionaryTypeServiceImpl implements DictionaryTypeService {
    @Autowired
    DictionaryTypeMapper mapper;

    @Override
    public List<DictionaryType> queryAllDicTypeByPages(int beginNo, int pageSize) {
        return mapper.selectAllDicTypeByPages(beginNo,pageSize);
    }

    @Override
    public int saveDicType(DictionaryType type) {
        return mapper.insertDicType(type);
    }

    @Override
    public int deleteDicTypeByCodes(String[] idString) {
        return mapper.deleteDicTypeByCode(idString);
    }

    @Override
    public int updateDicTypeByCode(DictionaryType type) {
        return mapper.updateDicTypeByCode(type);
    }

    @Override
    public DictionaryType queryDicTypeByCode(String code) {
        return mapper.selectDicTypeByCode(code);
    }

    @Override
    public List<DictionaryType> queryAllTypes() {
        return mapper.selectAllTypes();
    }
}
