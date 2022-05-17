package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.wlogger.dao.AdminDAOLocal;
import com.actility.thingpark.wlogger.dto.ResponseList;
import com.actility.thingpark.wlogger.entity.Admin;
import com.actility.thingpark.wlogger.exception.WloggerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    private AdminDAOLocal daoMock;
    private AdminServiceImpl service;

    @BeforeEach
    void setUp() {
        daoMock = mock(AdminDAOLocal.class);
        service = new AdminServiceImpl(daoMock);
    }

  @AfterEach
  void tearDown() {}

  private static void assertAdminResponseList(ResponseList response, int length) {
    assertNotNull(response);
    assertEquals("200", response.getStatusCode());
    assertNotNull(response.isSuccess());
    assertTrue(response.isSuccess());
    assertNotNull(response.getData());
    assertEquals(length, response.getData().size());

  }

  @Test
  void create_newAdmin_Succeed() throws WloggerException {
    final String login = "login", password = "password";
    when(daoMock.findAdminByLogin(eq(login))).thenReturn(null);
    final ResponseList response = this.service.create(login, password);
    verify(daoMock, times(1)).findAdminByLogin(eq(login));
    final ArgumentCaptor<Admin> argument = ArgumentCaptor.forClass(Admin.class);
    verify(daoMock, times(1)).createAdmin(argument.capture());
    assertNotNull(argument.getValue());
    assertEquals(login, argument.getValue().getLogin());
    assertNotNull(argument.getValue().getPassword());
    assertAdminResponseList(response, 3);
  }

  @Test
  void create_alreadyUsedAdmin_ExceptionThrown() {
    final String login = "login", password = "password";
    when(daoMock.findAdminByLogin(eq(login))).thenReturn(new Admin());
    assertThrows(WloggerException.class, () -> this.service.create(login, password));
    verify(daoMock, times(1)).findAdminByLogin(eq(login));
    verify(daoMock, never()).createAdmin(any(Admin.class));
  }

  @Test
  void create_emptyLogin_ExceptionThrown() {
    final String login = "", password = "password";
    when(daoMock.findAdminByLogin(eq(login))).thenReturn(new Admin());
    assertThrows(WloggerException.class, () -> this.service.create(login, password));
    verify(daoMock, never()).findAdminByLogin(anyString());
    verify(daoMock, never()).createAdmin(any(Admin.class));
  }

  @Test
  void create_emptyPassword_ExceptionThrown() {
    final String login = "login", password = "";
    when(daoMock.findAdminByLogin(eq(login))).thenReturn(new Admin());
    assertThrows(WloggerException.class, () -> this.service.create(login, password));
    verify(daoMock, never()).findAdminByLogin(anyString());
    verify(daoMock, never()).createAdmin(any(Admin.class));
  }

  @Test
  void get_simpleId_Succeed() throws WloggerException {
    final Long id = 1L;
    when(daoMock.findAdmin(eq(id))).thenReturn(new Admin());
    final ResponseList response = this.service.get(id.toString());
    verify(daoMock, times(1)).findAdmin(eq(id));
    assertAdminResponseList(response, 3);
  }

  @Test
  void get_unknownId_ExceptionThrown() {
    final Long id = 1L;
    when(daoMock.findAdmin(eq(id))).thenReturn(null);
    assertThrows(WloggerException.class, () -> this.service.get(id.toString()));
    verify(daoMock, times(1)).findAdmin(eq(id));
  }

  @Test
  void get_invalidId_ExceptionThrown() {
    final String id = "invalid";
    assertThrows(NumberFormatException.class, () -> this.service.get(id));
    verify(daoMock, never()).findAdmin(anyLong());
  }

  @Test
  void getByLogin_simpleId_Succeed() throws WloggerException {
    final String login = "login";
    when(daoMock.findAdminByLogin(eq(login))).thenReturn(new Admin());
    final ResponseList response = this.service.getByLogin(login);
    verify(daoMock, times(1)).findAdminByLogin(eq(login));
    assertAdminResponseList(response, 3);
  }

  @Test
  void getByLogin_unknownId_ExceptionThrown() {
    final String login = "login";
    when(daoMock.findAdminByLogin(eq(login))).thenReturn(null);
    assertThrows(WloggerException.class, () -> this.service.getByLogin(login));
    verify(daoMock, times(1)).findAdminByLogin(eq(login));
  }

  @Test
  void deleteByLogin_simpleId_Succeed() throws WloggerException {
    final String login = "login";
    when(daoMock.findAdminByLogin(eq(login))).thenReturn(new Admin().setLogin(login));
    final ResponseList response = this.service.deleteByLogin(login);
    verify(daoMock, times(1)).findAdminByLogin(eq(login));
    final ArgumentCaptor<Admin> argument = ArgumentCaptor.forClass(Admin.class);
    verify(daoMock, times(1)).delete(argument.capture());
    assertNotNull(argument.getValue());
    assertEquals(login, argument.getValue().getLogin());
    assertAdminResponseList(response, 0);
  }

  @Test
  void deleteByLogin_unknownId_ExceptionThrown() {
    final String login = "login";
    when(daoMock.findAdminByLogin(eq(login))).thenReturn(null);
    assertThrows(WloggerException.class, () -> this.service.deleteByLogin(login));
    verify(daoMock, times(1)).findAdminByLogin(eq(login));
    verify(daoMock, never()).delete(any(Admin.class));
  }

}
