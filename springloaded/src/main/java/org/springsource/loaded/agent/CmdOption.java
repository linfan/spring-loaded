package org.springsource.loaded.agent;

public class CmdOption {

    private boolean enableRemote = false;
    private String classNamePrefix = null;

    public boolean isEnableRemote() {
        return enableRemote;
    }

    public void setEnableRemote(boolean enableRemote) {
        this.enableRemote = enableRemote;
    }

    public String getClassNamePrefix() {
        return classNamePrefix;
    }

    public void setClassNamePrefix(String classNamePrefix) {
        this.classNamePrefix = classNamePrefix;
    }
}
