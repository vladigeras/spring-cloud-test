package ru.cloud.test.eurekaclient1.eurekaclient1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancer;

    @GetMapping(value = "/")
    public String home() {
        return "<a href='testCallClient-service-2'>/Test Call client-service-2</a>";
    }

    @GetMapping(value = "/testCallClient-service-2")
    public String showService() {

        String serviceId = "client-service-2".toLowerCase();

        List<ServiceInstance> instances = this.discoveryClient.getInstances(serviceId);

        if (instances == null || instances.isEmpty()) {
            return "No instances for service: " + serviceId;
        }
        String html = "<h2>Instances for Service Id: " + serviceId + "</h2>";

        for (ServiceInstance serviceInstance : instances) {
            html += "<h3>Instance :" + serviceInstance.getUri() + "</h3>";
        }

        RestTemplate restTemplate = new RestTemplate();

        html += "<br><h4>Call /hello of service: " + serviceId + "</h4>";

        try {
            ServiceInstance serviceInstance = this.loadBalancer.choose(serviceId);

            html += "<br>===> Load Balancer choose: " + serviceInstance.getUri();

            String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/hello";

            html += "<br>Make a Call: " + url;
            html += "<br>";

            String result = restTemplate.getForObject(url, String.class);

            html += "<br>Result: " + result;
        } catch (IllegalStateException e) {
            html += "<br>loadBalancer.choose ERROR: " + e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            html += "<br>Other ERROR: " + e.getMessage();
            e.printStackTrace();
        }
        return html;
    }
}
