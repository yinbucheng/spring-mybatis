package cn.bucheng;

import cn.bucheng.aware.ApplicationUtils;
import cn.bucheng.aware.BeanFactoryUtils;
import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@SpringBootApplication
@MapperScan(basePackages = {"cn.bucheng.authmanager.dao"})
@Import(FdfsClientConfig.class)
@RestController
@Configuration
public class BootstrapApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(BootstrapApplication.class, args);
        invokeTest();
    }


    @RequestMapping("/test2")
    public Object test(HttpServletRequest request) {
        System.out.println(request.getServletPath());
        System.out.println(request.getRequestURL());
        System.out.println(request.getRequestURI());
        return "{\"code\":200,\"content\":\"test\"}";
    }

//    @RequestMapping("/test")
//    public Object test2(){
//        invokeTest();
//        return "success";
//    }



    public static void invokeTest() {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        ((MockHttpServletRequest) request).setMethod("GET");
        ((MockHttpServletRequest) request).setRequestURI("/test2");
        ((MockHttpServletRequest) request).setServletPath("/test2");
        DispatcherServlet dispatcherServlet = BeanFactoryUtils.getBean(DispatcherServlet.class);
        try {
            Method doDispatch = DispatcherServlet.class.getDeclaredMethod("doDispatch", HttpServletRequest.class, HttpServletResponse.class);
            doDispatch.setAccessible(true);
            doDispatch.invoke(dispatcherServlet, request, response);
            String contentAsString = ((MockHttpServletResponse) response).getContentAsString();
            System.out.println(contentAsString);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
