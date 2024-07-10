package com.importer.importer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component("PropertiesListener")
public class PropertiesListner implements ApplicationListener<ApplicationPreparedEvent> {

    private static final String encryptedPassword = "AQICAHg5hJ+rvo4AEgGfVOeARj33fF/BqZrcLUy4LjgIbBO5BwEjyqEAX/cWrLNNSHxSP/njAAAAYjBgBgkqhkiG9w0BBwagUzBRAgEAMEwGCSqGSIb3DQEHATAeBglghkgBZQMEAS4wEQQMOrHcbaztUjoJwxOYAgEQgB82TPKwwZ0UT2mvz/M36BlZtf7fpMxE3T6yohnr1S1J";

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        KMSUtil kmsUtil = new KMSUtil();
       String databaseSecret = kmsUtil.kmsDecrypt(encryptedPassword);
        System.out.println("decrypted password "+ databaseSecret + "---------------------------------------------------------");
       ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
       Properties props = new Properties();

       props.put("spring.datasource.password", databaseSecret);

       environment.getPropertySources().addFirst(new PropertiesPropertySource("spring.datasource1", props));
    }

}