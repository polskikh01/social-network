package Classes;

import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HbbtvMimeMapping implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
       // mappings.add("html", "application/vnd.hbbtv.xhtml+xml; charset=utf-8");
        mappings.add("css", "text/css");
        factory.setMimeMappings(mappings);
    }
}