package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.dao.AdminDAOLocal;
import com.actility.thingpark.wlogger.dto.ElementData;
import com.actility.thingpark.wlogger.dto.ResponseList;
import com.actility.thingpark.wlogger.entity.Admin;
import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.response.ResponseFactory;
import com.actility.thingpark.wlogger.utils.CodingUtils;
import io.quarkus.arc.DefaultBean;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import static com.google.common.collect.ImmutableList.of;
import static org.apache.commons.lang3.StringUtils.isBlank;

@ApplicationScoped
@DefaultBean
public class AdminServiceImpl implements AdminService {

  public static final String ADMIN_NOT_FOUND = "Admin not found";
  private final AdminDAOLocal dao;

  @Inject
  public AdminServiceImpl(final AdminDAOLocal dao) {
    this.dao = dao;
  }

  private static ResponseList getLoginResponse(Admin admin) {
    return ResponseFactory.createSuccessResponse()
        .withData(
            of(
                ElementData.builder().name("ID").value(String.valueOf(admin.getUID())).build(),
                ElementData.builder().name("LOGIN").value(admin.getLogin()).build(),
                ElementData.builder().name("PASSWORD").value(admin.getPassword()).build()));
  }

  private static String hashPassword(String password) {
    String salt = Hex.encodeHexString(RandomUtils.nextBytes(32));
    return salt + DigestUtils.sha256Hex(salt + password);
  }

  @Transactional
  public ResponseList create(String login, String password) throws WloggerException {
    if (isBlank(login)) {
      throw WloggerException.invalidValue("Login not valid");
    }
    if (isBlank(password)) {
      throw WloggerException.invalidValue("Password not valid");
    }
    if (getAdminByLogin(login) != null) {
      throw WloggerException.invalidValue("Login already used");
    }
    final Admin admin = new Admin(login, hashPassword(password));
    dao.createAdmin(admin);
    return getLoginResponse(admin);
  }

  public ResponseList get(String id) throws WloggerException {
    Admin admin = dao.findAdmin(Long.valueOf(id));
    if (admin == null) {
      throw WloggerException.notFound(Errors.UNKNOWN_ID, ADMIN_NOT_FOUND);
    }
    return getLoginResponse(admin);
  }

  public ResponseList getByLogin(String login) throws WloggerException {
    Admin admin = getAdminByLogin(login);
    if (admin == null) {
      throw WloggerException.notFound(Errors.UNKNOWN_ID, ADMIN_NOT_FOUND);
    }
    return getLoginResponse(admin);
  }

  @Transactional
  public ResponseList deleteByLogin(String login) throws WloggerException {
    Admin admin = getAdminByLogin(login);
    if (admin == null) {
      throw WloggerException.notFound(Errors.UNKNOWN_ID, ADMIN_NOT_FOUND);
    }
    dao.delete(admin);
    return ResponseFactory.createSuccessResponse();
  }

  public Admin getAdminByLogin(String login) {
    return dao.findAdminByLogin(login);
  }

  public String logAdmin(String login, String password) throws WloggerException {
    Admin admin = getAdminByLogin(login);
    if (admin != null) {
      if (!CodingUtils.checkSHA256Password(admin.getPassword(), password))
        throw WloggerException.applicationError(
            Errors.AUTH_SMP_FAILED, "Username or password invalid !");
      return admin.getUID().toString();
    }
    return null;
  }
}
