package com.importer.importer;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;

import java.util.Base64;
import java.util.Properties;

@Component("PropertiesListener")
public class PropertiesListner implements ApplicationListener<ApplicationPreparedEvent> {


    private final KmsClient kmsClient = KmsClient.builder().region(Region.of("eu-north-1"))
            .credentialsProvider(DefaultCredentialsProvider.create()).build();


    private static final String AWS_KEY_ID = "0fb038b0-326b-42a3-aca8-3a3eac2a2a9a";

    private static final String encryptedPassword = "AQICAHg5hJ+rvo4AEgGfVOeARj33fF/BqZrcLUy4LjgIbBO5BwEjyqEAX/cWrLNNSHxSP/njAAAAYjBgBgkqhkiG9w0BBwagUzBRAgEAMEwGCSqGSIb3DQEHATAeBglghkgBZQMEAS4wEQQMOrHcbaztUjoJwxOYAgEQgB82TPKwwZ0UT2mvz/M36BlZtf7fpMxE3T6yohnr1S1J";
    private static final String region = "eu-north-1";


    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
       String databaseSecret = kmsDecrypt(encryptedPassword);
        System.out.println("decrypted password "+ databaseSecret + "---------------------------------------------------------");
       ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
       Properties props = new Properties();

       props.put("spring.datasource.password", databaseSecret);

       environment.getPropertySources().addFirst(new PropertiesPropertySource("spring.datasource1", props));

    }

    public String kmsDecrypt(String base64EncodedValue){
        DecryptRequest decryptRequest = buildDecryptRequest( base64EncodedValue );
        DecryptResponse decryptResponse = this.kmsClient.decrypt(decryptRequest);
        return decryptResponse.plaintext().asUtf8String();
    }

    public String kmsEncrypt(String plainText){
        EncryptRequest encryptRequest = buildEncryptRequest(plainText);
        EncryptResponse encryptResponse = kmsClient.encrypt(encryptRequest);
        SdkBytes cipherTextBytes = encryptResponse.ciphertextBlob();
        byte[] base64EncodedValue = Base64.getEncoder().encode(cipherTextBytes.asByteArray());
        return new String(base64EncodedValue);
    }

    private EncryptRequest buildEncryptRequest(String plainText){
        SdkBytes plaiTextBytes = SdkBytes.fromUtf8String(plainText);
        return EncryptRequest.builder().keyId(AWS_KEY_ID).plaintext(plaiTextBytes).build();
    }

    private DecryptRequest buildDecryptRequest(String base64EncodedValue){
        SdkBytes encryptBytes = SdkBytes.fromByteArray(Base64.getDecoder().decode(base64EncodedValue));
        return DecryptRequest.builder().keyId(AWS_KEY_ID).ciphertextBlob(encryptBytes).build();
    }

}