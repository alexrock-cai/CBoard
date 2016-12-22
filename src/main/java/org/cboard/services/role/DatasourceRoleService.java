package org.cboard.services.role;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.cboard.dao.DatasourceDao;
import org.cboard.dao.WidgetDao;
import org.cboard.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by yfyuan on 2016/12/14.
 */
@Repository
@Aspect
public class DatasourceRoleService {

    @Autowired
    private DatasourceDao datasourceDao;

    @Autowired
    private AuthenticationService authenticationService;

    @Around("execution(* org.cboard.services.DatasourceService.update(..))")
    public Object update(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String json = (String) proceedingJoinPoint.getArgs()[1];
        JSONObject jsonObject = JSONObject.parseObject(json);
        String userid = authenticationService.getCurrentUser().getUserId();
        if (datasourceDao.checkDatasourceRole(userid, jsonObject.getLong("id")) > 0) {
            Object value = proceedingJoinPoint.proceed();
            return value;
        } else {
            return null;
        }
    }

    @Around("execution(* org.cboard.services.DatasourceService.delete(..))")
    public Object delete(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Long id = (Long) proceedingJoinPoint.getArgs()[1];
        String userid = authenticationService.getCurrentUser().getUserId();
        if (datasourceDao.checkDatasourceRole(userid, id) > 0) {
            Object value = proceedingJoinPoint.proceed();
            return value;
        } else {
            return null;
        }
    }
}
