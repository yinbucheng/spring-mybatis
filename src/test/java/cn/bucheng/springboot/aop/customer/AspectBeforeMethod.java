package cn.bucheng.springboot.aop.customer;

import org.aopalliance.intercept.Joinpoint;

import java.lang.reflect.Method;

/**
 * @author ：yinchong
 * @create ：2019/6/26 17:37
 * @description：
 * @modified By：
 * @version:
 */
public abstract class AspectBeforeMethod implements MethodInterceptor, Match {

    public abstract void before();


    @Override
    public Object invoke(Joinpoint joinPoint, Method method) {
        try {
            if (match(joinPoint.getThis(),method)) {
                before();
            }
            return joinPoint.proceed();
        } catch (Throwable t) {
            System.err.println(t);
            return null;
        }
    }
}
