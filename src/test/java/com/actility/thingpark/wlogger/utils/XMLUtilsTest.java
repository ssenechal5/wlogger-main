package com.actility.thingpark.wlogger.utils;

import com.actility.thingpark.smp.rest.dto.AuthenticateDto;
import com.actility.thingpark.smp.rest.dto.admin.LoginDto;
import org.junit.jupiter.api.Test;
import org.xmlunit.matchers.CompareMatcher;

import javax.xml.bind.JAXBException;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XMLUtilsTest {

    @Test
    void convertToSmpObjectLogin() throws JAXBException {
        LoginDto login = XMLUtils.convertToSmpObject("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<ns:login xmlns:ns=\"http://www.actility.com/smp/ws/admin\">\n" +
                "  <sessionToken>fakeSessionToken</sessionToken>\n" +
                "  <thingparkID>fakeThingParkID</thingparkID>\n" +
                "</ns:login>", LoginDto.class);
        assertNotNull(login);
        assertEquals("fakeSessionToken", login.getSessionToken());
        assertEquals("fakeThingParkID", login.getThingparkID());
    }

    @Test
    void convertToSmpObjectAuthenticate() throws JAXBException {
        AuthenticateDto auth = XMLUtils.convertToSmpObject("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<ns:authenticate xmlns:ns=\"http://www.actility.com/smp/ws/application\">\n" +
                "  <grantedPermissions>\n" +
                "    <permission>a</permission>\n" +
                "    <permission>x</permission>\n" +
                "  </grantedPermissions>\n" +
                "  <href>/thingpark/smp/rest/applications/app1/subscriptions/subs1</href>\n" +
                "  <subscriber>\n" +
                "    <ID>100000002</ID>\n" +
                "    <extID>fakeExt</extID>\n" +
                "  </subscriber>\n" +
                "  <user>\n" +
                "    <ID>tpk-100000002</ID>\n" +
                "    <href>/thingpark/smp/rest/applications/app1/subscriptions/users/usr1</href>\n" +
                "  </user>\n" +
                "</ns:authenticate>", AuthenticateDto.class);
        assertNotNull(auth);
        assertNotNull(auth.getGrantedPermissions());
        assertEquals(Arrays.asList("a", "x"), auth.getGrantedPermissions().getPermission());
        assertEquals("/thingpark/smp/rest/applications/app1/subscriptions/subs1", auth.getHref());
        assertNotNull(auth.getSubscriber());
        assertEquals("100000002", auth.getSubscriber().getID());
        assertEquals("fakeExt", auth.getSubscriber().getExtID());
        assertNotNull(auth.getUser());
        assertEquals("tpk-100000002", auth.getUser().getID());
        assertEquals("/thingpark/smp/rest/applications/app1/subscriptions/users/usr1", auth.getUser().getHref());
    }

    @Test
    void writeSmpObject() throws Exception {
        LoginDto login = new LoginDto();
        login.setLogin("john.doe");
        login.setPassword("p@ssw0rd");
        byte[] out = XMLUtils.writeSmpObject(login);
        final String s = new String(out);
        assertThat(s, CompareMatcher.isSimilarTo("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns:login xmlns:ns=\"http://www.actility.com/smp/ws/admin\">\n" +
                "  <login>john.doe</login>\n" +
                "  <password>p@ssw0rd</password>\n" +
                "</ns:login>").ignoreWhitespace());
    }

}