package Classes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	
	@Value("${download.path}")
	private String uploadPath;
	
	
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/Login").setViewName("Login");
	}
	



	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		// TODO Auto-generated method stub
		registry.addResourceHandler("/upload/**").addResourceLocations(uploadPath);
	}
	
}