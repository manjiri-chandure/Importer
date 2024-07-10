package com.importer.importer;
import org.springframework.beans.factory.annotation.Value;
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

@Component
public class KMSUtil {

  @Value("${cmkKeyARN}")
  private String cmkKeyARN;

  private final KmsClient kmsClient = KmsClient.builder().region(Region.of("eu-north-1"))
    .credentialsProvider(DefaultCredentialsProvider.create()).build();

  public String kmsEncrypt(String plainText){
    EncryptRequest encryptRequest = buildEncryptRequest(plainText);
    EncryptResponse encryptResponse = kmsClient.encrypt(encryptRequest);
    SdkBytes cipherTextBytes = encryptResponse.ciphertextBlob();
    byte[] base64EncodedValue = Base64.getEncoder().encode(cipherTextBytes.asByteArray());
    return new String(base64EncodedValue);
  }

  public String kmsDecrypt(String base64EncodedValue){
    DecryptRequest decryptRequest = buildDecryptRequest( base64EncodedValue );
    DecryptResponse decryptResponse = this.kmsClient.decrypt(decryptRequest);
    return decryptResponse.plaintext().asUtf8String();
  }

  private EncryptRequest buildEncryptRequest(String plainText){
    SdkBytes plaiTextBytes = SdkBytes.fromUtf8String(plainText);
    return EncryptRequest.builder().keyId(cmkKeyARN).plaintext(plaiTextBytes).build();
  }

  private DecryptRequest buildDecryptRequest(String base64EncodedValue){
      SdkBytes encryptBytes = SdkBytes.fromByteArray(Base64.getDecoder().decode(base64EncodedValue));
      return DecryptRequest.builder().keyId(cmkKeyARN).ciphertextBlob(encryptBytes).build();
  }


}
