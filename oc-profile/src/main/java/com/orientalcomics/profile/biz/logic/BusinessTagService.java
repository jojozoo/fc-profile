package com.orientalcomics.profile.biz.logic;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.biz.dao.BusinessTagDAO;
import com.orientalcomics.profile.biz.model.BusinessTag;


@Service
public class BusinessTagService {

    @Autowired
    BusinessTagDAO businessTagDAO;

//    @Autowired
//    IndexService   indexService;

    public List<BusinessTag> queryAll(int offset, int count) {
        return businessTagDAO.queryAll(offset, count);
    }

    public List<BusinessTag> queryAll() {
        return businessTagDAO.queryAll();
    }

    public int countAll() {
        return businessTagDAO.countAll();
    }

    public List<BusinessTag> query(int chargeUserId) {
        return businessTagDAO.query(chargeUserId);
    }

    public List<BusinessTag> query(Collection<Integer> chargeUserIds) {
        return businessTagDAO.query(chargeUserIds);
    }

    public int update(BusinessTag model) {
        int result = businessTagDAO.update(model);
//        try {
//            indexService.indexUserId(model.getChargeUser());
//        } catch (IndexException e) {
//            e.printStackTrace();
//        }
        return result;
    }

    public Integer save(BusinessTag model) {
        Integer result = businessTagDAO.save(model);
//        try {
//            indexService.indexUserId(model.getChargeUser());
//        } catch (IndexException e) {
//            e.printStackTrace();
//        }
        return result;
    }

    public void delete(int id, int userId) {
        businessTagDAO.delete(id);
//        try {
//            indexService.indexUserId(userId);
//        } catch (IndexException e) {
//            e.printStackTrace();
//        }
    }

    public void deleteByUserIdAndTagName(int userId, String tagName) {
        businessTagDAO.deleteByUserIdAndTagName(userId, tagName);
//        try {
//            indexService.indexUserId(userId);
//        } catch (IndexException e) {
//            e.printStackTrace();
//        }
    }

}
