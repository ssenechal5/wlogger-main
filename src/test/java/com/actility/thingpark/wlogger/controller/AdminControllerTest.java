package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.wlogger.dao.AdminDAOLocal;
import com.actility.thingpark.wlogger.dto.ElementData;
import com.actility.thingpark.wlogger.dto.ResponseList;
import com.actility.thingpark.wlogger.entity.Admin;
import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.service.AdminService;
import com.actility.thingpark.wlogger.service.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AdminControllerTest {

  private AdminDAOLocal daoMock;
  private AdminService adminService;
  private AdminController admin;

  private final String login = "login", password = "password";

  @BeforeEach
  void setUp() {
    daoMock = mock(AdminDAOLocal.class);
    adminService = new AdminServiceImpl(daoMock);

    admin = new AdminController();
    admin.inject(adminService);
  }

  @Test
  void add() throws WloggerException {
    when(daoMock.findAdminByLogin(eq(login))).thenReturn(null);

    final ResponseList response = (ResponseList) this.admin.add(login, password);

    verify(daoMock, times(1)).findAdminByLogin(eq(login));
    final ArgumentCaptor<Admin> argument = ArgumentCaptor.forClass(Admin.class);
    verify(daoMock, times(1)).createAdmin(argument.capture());
    assertNotNull(argument.getValue());
    assertEquals(login, argument.getValue().getLogin());
    assertNotNull(argument.getValue().getPassword());

    assertAdminResponseList(response, 3);
  }

  @Test
  void getNone() {
    final Long id = 1L;
    when(daoMock.findAdmin(eq(id))).thenReturn(null);
    assertThrows(WloggerException.class, () -> this.admin.get(id.toString()));
    verify(daoMock, times(1)).findAdmin(eq(id));
  }

  @Test
  void get() throws WloggerException {
    final Long id = 1L;
    when(daoMock.findAdmin(eq(id))).thenReturn(new Admin(login,password));
    final ResponseList response = (ResponseList) this.admin.get(id.toString());
    verify(daoMock, times(1)).findAdmin(eq(id));

    assertAdminResponseList(response, 3);
    assertEquals(login, ((ElementData)response.getData().get(1)).value);
    assertNotNull(((ElementData)response.getData().get(2)).value);
  }

  @Test
  void getByLogin() throws WloggerException {
    when(daoMock.findAdminByLogin(eq(login))).thenReturn(new Admin(login,password));
    final ResponseList response = (ResponseList) this.admin.getByLogin(login);
    verify(daoMock, times(1)).findAdminByLogin(eq(login));

    assertAdminResponseList(response, 3);
  }

  @Test
  void deleteByLogin() throws WloggerException {
    when(daoMock.findAdminByLogin(eq(login))).thenReturn(new Admin(login,password));
    final ResponseList response = (ResponseList) this.admin.deleteByLogin(login);

    verify(daoMock, times(1)).findAdminByLogin(eq(login));
    final ArgumentCaptor<Admin> argument = ArgumentCaptor.forClass(Admin.class);
    verify(daoMock, times(1)).delete(argument.capture());
    assertNotNull(argument.getValue());
    assertEquals(login, argument.getValue().getLogin());

    assertAdminResponseList(response, 0);
  }

  private void assertAdminResponseList(ResponseList response, int length) {
    assertNotNull(response);
    assertEquals("200", response.getStatusCode());
    assertNotNull(response.isSuccess());
    assertTrue(response.isSuccess());
    assertNotNull(response.getData());
    assertEquals(length, response.getData().size());

    if (response.getData().size() > 0) {
      assertEquals(login, ((ElementData) response.getData().get(1)).value);
      assertNotNull(((ElementData) response.getData().get(2)).value);
    }
  }

}