package cn.bucheng.springboot;

import cn.bucheng.springboot.annotest.bean.Bean;
import cn.bucheng.springboot.annotest.config.ConditionOnBeanConfig;
import cn.bucheng.springboot.annotest.config.ConditionOnClassConfig;
import cn.bucheng.springboot.annotest.config.ConditionOnMissClassConfig;
import cn.bucheng.springboot.annotest.config.ConditionOnMissBeanConfig;
import cn.bucheng.springboot.aop.customer.AspectAfterMethod;
import cn.bucheng.springboot.aop.customer.AspectBeforeMethod;
import cn.bucheng.springboot.ioc.ClassPathBeaDefinitionScanner;
import cn.bucheng.springboot.test.Animal;
import cn.bucheng.springboot.test.Cat;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author ：yinchong
 * @create ：2019/6/26 16:10
 * @description：检测测试
 * @modified By：
 * @version:
 */
public class SimpleBootTest {

    //扫描制定包名下面内容测试
    @Test
    public void testScannerBeanDefinition() {
        ClassPathBeaDefinitionScanner scanner = new ClassPathBeaDefinitionScanner();
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents("cn.bucheng.springboot.interfaces");
        System.out.println(candidateComponents.size());
        candidateComponents.forEach((param) -> {
            System.out.println(param.getBeanClassName());
            System.out.println(param.getScope());
        });
    }

    //手动创建ioc容器测试
    @Test
    public void testCustomFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(beanFactory);
        applicationContext.register(Cat.class);
        applicationContext.refresh();
        Cat bean = applicationContext.getBean(Cat.class);
        System.out.println(bean.say());
    }

    @Test
    public void testCustomAop() {
        Animal animal = new Cat();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                try {
                    System.out.println("----------------->aop before<--------------");
                    return invocation.proceed();
                } finally {
                    System.out.println("---------------->aop after<--------------");
                }
            }
        });
        proxyFactory.setTarget(animal);
        Animal proxy = (Animal) proxyFactory.getProxy();
        System.out.println(proxy.say());
    }

    @Test
    public void testAopCustom() {
        Animal animal = new Cat();
        cn.bucheng.springboot.aop.customer.ProxyFactory proxyFactory = new cn.bucheng.springboot.aop.customer.ProxyFactory();
        proxyFactory.setTarget(animal);
        AspectBeforeMethod before1 = new AspectBeforeMethod() {
            @Override
            public void before() {
                System.out.println("============test1 before==========");
            }

            @Override
            public boolean match(Object object, Method method) {
                return true;
            }
        };


        AspectAfterMethod after = new AspectAfterMethod() {
            @Override
            public void after() {
                System.out.println("===========test1 after==========");
            }

            @Override
            public boolean match(Object target, Method method) {
                if(method.getName().equals("eat")) {
                    return true;
                }
                return false;
            }
        };

        proxyFactory.addAdvice(after);
        proxyFactory.addAdvice(before1);
        Animal proxy = (Animal) proxyFactory.getProxy();
        proxy.eat("fish");
    }


    @Test
    public void testConditionOnMissBean(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("cn.bucheng.springboot.annotest.bean");
        context.register(ConditionOnMissBeanConfig.class);
        context.refresh();
        String[] beanNamesForType = context.getBeanNamesForType(Object.class);
        System.out.println(beanNamesForType.length);
        Bean bean = context.getBean(Bean.class);
        bean.test();
    }

    @Test
    public void testConditionOnBean(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("cn.bucheng.springboot.annotest.bean");
        context.register(ConditionOnBeanConfig.class);
        context.refresh();
        String[] beanNamesForType = context.getBeanNamesForType(Object.class);
        System.out.println(beanNamesForType.length);
        Bean bean = context.getBean(Bean.class);
        bean.test();
    }

    @Test
    public void testConditionOnClass(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("cn.bucheng.springboot.annotest.bean");
        context.register(ConditionOnClassConfig.class);
        context.refresh();
        String[] beanNamesForType = context.getBeanNamesForType(Object.class);
        System.out.println(beanNamesForType.length);
        Bean bean = context.getBean(Bean.class);
        bean.test();
    }

    @Test
    public void testConditionOnMissClass(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("cn.bucheng.springboot.annotest.bean");
        context.register(ConditionOnMissClassConfig.class);
        context.refresh();
        String[] beanNamesForType = context.getBeanNamesForType(Object.class);
        System.out.println(beanNamesForType.length);
        Bean bean = context.getBean(Bean.class);
        bean.test();
    }



}
