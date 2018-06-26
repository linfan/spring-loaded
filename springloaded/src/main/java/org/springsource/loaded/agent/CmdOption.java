package org.springsource.loaded.agent;

public class CmdOption {

    private boolean enableRemote = false;
    private String classNamePrefix = null;

    public static CmdOption fromStringArgs(String argOptions) {
        // E.g. "prefix:com.demo.app,com/demo/lib;remote"
        final String PREFIX = "prefix:";
        final String REMOTE = "remote";
        CmdOption option = new CmdOption();
        if (argOptions != null) {
            String[] args = argOptions.split(";");
            for (String arg : args) {
                if (arg.startsWith(PREFIX)) {
                    option.setClassNamePrefix(
                        arg.substring(PREFIX.length()).replace(".", "/"));
                } else if (arg.equals(REMOTE)) {
                    option.setEnableRemote(true);
                }
            }
        }
        return option;
    }

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
