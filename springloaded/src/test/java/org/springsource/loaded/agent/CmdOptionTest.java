package org.springsource.loaded.agent;

import org.junit.Test;

import static org.junit.Assert.*;

public class CmdOptionTest {

    @Test
    public void testFromStringArgs() {
        CmdOption option = CmdOption.fromStringArgs("prefix:com.demo.app,com/demo/lib;remote");
        assertEquals("com/demo/app,com/demo/lib", option.getClassNamePrefix());
        assertEquals(true, option.isEnableRemote());
    }
}