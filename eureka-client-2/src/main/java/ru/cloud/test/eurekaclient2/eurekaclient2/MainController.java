package ru.cloud.test.eurekaclient2.eurekaclient2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class MainController {

    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());


    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/")
    public String home() {
        return "<a href='showAllServiceIds'>Show All Service Ids</a>";
    }

    @GetMapping(value = "/showAllServiceIds")
    public String showAllServiceIds() {
        List<String> serviceIds = this.discoveryClient.getServices();

        if (serviceIds == null || serviceIds.isEmpty()) {
            return "No services found!";
        }
        String html = "<h3>Service Ids:</h3>";
        for (String serviceId : serviceIds) {
            html += "<br><a href='showService?serviceId=" + serviceId + "'>" + serviceId + "</a>";
        }
        LOGGER.log(Level.INFO, "/showAllServiceIds method from eureka-client-2 was called");
        return html;
    }

    @GetMapping(value = "/showService")
    public String showService(@RequestParam(defaultValue = "") String serviceId) {
        List<ServiceInstance> instances = this.discoveryClient.getInstances(serviceId);
        LOGGER.log(Level.INFO, "/showService method from eureka-client-2 was called");

        if (instances == null || instances.isEmpty()) {
            return "No instances for service: " + serviceId;
        }
        String html = "<h2>Instances for Service Id: " + serviceId + "</h2>";

        for (ServiceInstance serviceInstance : instances) {
            html += "<h3>Instance: " + serviceInstance.getUri() + "</h3>";
            html += "Host: " + serviceInstance.getHost() + "<br>";
            html += "Port: " + serviceInstance.getPort() + "<br>";
        }

        return html;
    }

    //This method will call from eureka-client-1 with Load balanced Ribbon
    @GetMapping(value = "/hello")
    public String hello() {
        LOGGER.log(Level.INFO, "/hello method from eureka-client-2 was called");
        return "<html>Hello from client-service-1</html>";
    }

}
