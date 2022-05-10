package com.actility.thingpark.wlogger.http;

import com.actility.thingpark.smp.rest.dto.admin.UserAccountDto;
import com.actility.thingpark.smp.rest.dto.application.SubscriptionDto;
import com.actility.thingpark.smp.rest.dto.application.UserContextsDto;
import com.actility.thingpark.smp.rest.dto.application.UserDto;
import com.actility.thingpark.smp.rest.dto.system.AuthenticateSystemDto;
import com.actility.thingpark.smp.rest.dto.system.OperatorsDto;
import com.actility.thingpark.smp.rest.dto.user.AccessCodeDto;
import com.actility.thingpark.wlogger.exception.SmpException;

public interface SmpClient {

  String getApplicationHref(String applicationID) throws SmpException;

  UserDto getUserInfos(String userHref) throws SmpException;

  UserContextsDto getUserContexts(String userHref) throws SmpException;

  com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto authenticateForThirdApp(
      String accessCode, String applicationID) throws SmpException;

  com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto authenticateBySubscription(
      String accessCode, String applicationID) throws SmpException;

  com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto authenticateUser(
      String accessCode, String applicationID) throws SmpException;

  com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto authenticateUserByOidc(
          String oidcUserId, String applicationID) throws SmpException;

  AuthenticateSystemDto authenticateUser(AuthenticateSystemDto authenticateDto) throws SmpException;

  AuthenticateSystemDto authenticateAdminUser(String adminAccessCode, String moduleID)
      throws SmpException;

  OperatorsDto getOperators(String id) throws SmpException;

  SubscriptionDto getApplicationSubscription(String href) throws SmpException;

  UserAccountDto getAdminUserNames(String userHref) throws SmpException;

  AccessCodeDto getAccessCodeUserByApi(String thingParkID, AccessCodeDto accessCodeDto) throws SmpException;

}
