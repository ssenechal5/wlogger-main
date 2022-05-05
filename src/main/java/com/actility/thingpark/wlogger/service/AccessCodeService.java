package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.wlogger.accesscode.AccessCode;
import com.actility.thingpark.wlogger.accesscode.AccessCodePayload;
import com.actility.thingpark.wlogger.accesscode.AccessCodeType;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AccessCodeService {

  final WloggerConfig wloggerConfig;

  @Inject
  public AccessCodeService(WloggerConfig wloggerConfig) {
    this.wloggerConfig = wloggerConfig;
  }

  static String base64Encode(String string) {
    return Base64.encodeBase64String(StringUtils.getBytesUsAscii(string));
  }

  static String base64Decode(String string) {
    return StringUtils.newStringUsAscii(Base64.decodeBase64(StringUtils.getBytesUsAscii(string)));
  }

  String computeSignature(String content) {
    return DigestUtils.sha256Hex(
            content + this.wloggerConfig.accessCodeSecret());
  }

  private String computeSignature(AccessCodePayload userAccessCode) {
    return computeSignature(userAccessCode.stringify());
  }

  private AccessCode signPayload(AccessCodePayload payload) {
    String signature = computeSignature(payload);
    return new AccessCode(payload, signature);
  }

  public String generateUserAccessCode(String id) {
    return generateAccessCode(new AccessCodePayload(AccessCodeType.USER, id));
  }

  public String generateSubscriberAccessCode(String id) {
    return generateAccessCode(new AccessCodePayload(AccessCodeType.SUBSCRIBER, id));
  }

  private String generateAccessCode(AccessCodePayload payload) {
    return base64Encode(signPayload(payload).stringify());
  }

  @Nonnull
  public AccessCodePayload decode(@Nullable String accessCode) {
    AccessCode content  = AccessCode.decodeContent(base64Decode(accessCode));
    content.verifySignature(computeSignature(content.getPayload()));
    content.getPayload().validate();
    return content.getPayload();
  }
}
