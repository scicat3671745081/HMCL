
/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2021  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.jackhuang.hmcl.game;

import org.jackhuang.hmcl.util.platform.JavaVersion;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author huangyuhui
 */
public class LaunchOptions implements Serializable {

    private File gameDir;
    private JavaVersion java;
    private String versionName;
    private String versionType;
    private String profileName;
    private List<String> gameArguments = new ArrayList<>();
    private List<String> javaArguments = new ArrayList<>();
    private List<String> javaAgents = new ArrayList<>(0);
    private Integer minMemory;
    private Integer maxMemory;
    private Integer metaspace;
    private Integer width;
    private Integer height;
    private boolean fullscreen;
    private String serverIp;
    private String wrapper;
    private Proxy proxy;
    private String proxyUser;
    private String proxyPass;
    private boolean noGeneratedJVMArgs;
    private String preLaunchCommand;
    private String postExitCommand;
    private NativesDirectoryType nativesDirType;
    private String nativesDir;
    private ProcessPriority processPriority = ProcessPriority.NORMAL;
    private boolean useSoftwareRenderer;
    private boolean useNativeGLFW;
    private boolean useNativeOpenAL;
    private boolean daemon;
    private boolean hiperMode;

    private boolean hiperMode;

    /**
     * The game directory
     */
    public File getGameDir() {
        return gameDir;
    }

    /**
     * The Java Environment that Minecraft runs on.
     */
    public JavaVersion getJava() {
        return java;
    }

    /**
     * Will shown in the left bottom corner of the main menu of Minecraft.
     * null if use the id of launch version.
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * Will shown in the left bottom corner of the main menu of Minecraft.
     * null if use Version.versionType.
     */
    public String getVersionType() {
        return versionType;
    }

    /**
     * Don't know what the hell this is.
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * User custom additional minecraft command line arguments.
     */
    @NotNull
    public List<String> getGameArguments() {
        return Collections.unmodifiableList(gameArguments);
    }

    /**
     * User custom additional java virtual machine command line arguments.
     */
    @NotNull
    public List<String> getJavaArguments() {
        return Collections.unmodifiableList(javaArguments);
    }

    @NotNull
    public List<String> getJavaAgents() {
        return Collections.unmodifiableList(javaAgents);
    }

    /**
     * The minimum memory that the JVM can allocate.
     */
    public Integer getMinMemory() {
        return minMemory;
    }

    /**
     * The maximum memory that the JVM can allocate.
     */
    public Integer getMaxMemory() {
        return maxMemory;
    }

    /**
     * The maximum metaspace memory that the JVM can allocate.
     * For Java 7 -XX:PermSize and Java 8 -XX:MetaspaceSize
     * Containing class instances.
     */
    public Integer getMetaspace() {
        return metaspace;
    }

    /**
     * The initial game window width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * The initial game window height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Is inital game window fullscreen.
     */
    public boolean isFullscreen() {
        return fullscreen;
    }

    /**
     * Is the game start in hiper mode.
     */
    public boolean isHiperMode() {
        return hiperMode;
    }

    /**
     * The server ip that will connect to when enter game main menu.
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * i.e. optirun
     */
    public String getWrapper() {
        return wrapper;
    }

    /**
     * Proxy settings
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * The user name of the proxy, optional.
     */
    public String getProxyUser() {
        return proxyUser;
    }

    /**
     * The password of the proxy, optional
     */
    public String getProxyPass() {
        return proxyPass;
    }

    /**
     * Prevent game launcher from generating default JVM arguments like max memory.
     */
    public boolean isNoGeneratedJVMArgs() {
        return noGeneratedJVMArgs;
    }

    /**
     * Command called before game launches.
     */
    public String getPreLaunchCommand() {
        return preLaunchCommand;
    }

    /**
     * Command called after game exits.
     */
    public String getPostExitCommand() {
        return postExitCommand;
    }

    /**
     * 0 - ./minecraft/versions/&lt;version&gt;/natives
     * 1 - custom natives directory
     */
    public NativesDirectoryType getNativesDirType() {
        return nativesDirType;
    }

    /**
     * Path to the natives directory, optional
     */
    public String getNativesDir() {
        return nativesDir;
    }

    /**
     * Process priority
     */
    public ProcessPriority getProcessPriority() {
        return processPriority;
    }

    public boolean isUseSoftwareRenderer() {
        return useSoftwareRenderer;
    }

    public boolean isUseNativeGLFW() {
        return useNativeGLFW;
    }

    public boolean isUseNativeOpenAL() {
        return useNativeOpenAL;
    }

    /**
     * Will launcher keeps alive after game launched or not.
     */
    public boolean isDaemon() {
        return daemon;
    }

    public boolean isHiperMode() {
        return hiperMode;
    }

    public static class Builder {

        private final LaunchOptions options = new LaunchOptions();

        public LaunchOptions create() {
            return options;
        }

        /**
         * The game directory
         */
        public File getGameDir() {
            return options.gameDir;
        }

        public Builder setGameDir(File gameDir) {
            options.gameDir = gameDir;
            return this;
        }

        /**
         * The Java Environment that Minecraft runs on.
         */
        public JavaVersion getJava() {
            return options.java;
        }

        public Builder setJava(JavaVersion java) {
            options.java = java;
            return this;
        }

        /**
         * Will shown in the left bottom corner of the main menu of Minecraft.
         * null if use the id of launch version.
         */
        public String getVersionName() {
            return options.versionName;
        }

        public Builder setVersionName(String versionName) {
            options.versionName = versionName;
            return this;
        }

        /**
         * Will shown in the left bottom corner of the main menu of Minecraft.
         * null if use Version.versionType.
         */
        public String getVersionType() {
            return options.versionType;
        }

        public Builder setVersionType(String versionType) {
            options.versionType = versionType;
            return this;
        }

        /**
         * Don't know what the hell this is.
         */
        public String getProfileName() {
            return options.profileName;
        }

        public Builder setProfileName(String profileName) {
            options.profileName = profileName;
            return this;
        }

        /**
         * User custom additional minecraft command line arguments.
         */
        public List<String> getGameArguments() {
            return options.gameArguments;
        }

        public Builder setGameArguments(List<String> gameArguments) {
            options.gameArguments.clear();
            options.gameArguments.addAll(gameArguments);
            return this;
        }

        /**
         * User custom additional java virtual machine command line arguments.
         */
        public List<String> getJavaArguments() {
            return options.javaArguments;
        }

        public Builder setJavaArguments(List<String> javaArguments) {
            options.javaArguments.clear();
            options.javaArguments.addAll(javaArguments);
            return this;
        }

        public List<String> getJavaAgents() {
            return options.javaAgents;
        }

        public Builder setJavaAgents(List<String> javaAgents) {
            options.javaAgents.clear();
            options.javaAgents.addAll(javaAgents);
            return this;
        }

        /**
         * The minimum memory that the JVM can allocate.
         */
        public Integer getMinMemory() {
            return options.minMemory;
        }

        public Builder setMinMemory(Integer minMemory) {
            options.minMemory = minMemory;
            return this;
        }

        /**
         * The maximum memory that the JVM can allocate.
         */
        public Integer getMaxMemory() {
            return options.maxMemory;
        }

        public Builder setMaxMemory(Integer maxMemory) {
            options.maxMemory = maxMemory;
            return this;
        }

        /**
         * The maximum metaspace memory that the JVM can allocate.
         * For Java 7 -XX:PermSize and Java 8 -XX:MetaspaceSize
         * Containing class instances.
         */
        public Integer getMetaspace() {
            return options.metaspace;
        }

        public Builder setMetaspace(Integer metaspace) {
            options.metaspace = metaspace;
            return this;
        }

        /**
         * The initial game window width
         */
        public Integer getWidth() {
            return options.width;
        }

        public Builder setWidth(Integer width) {
            options.width = width;
            return this;
        }

        /**
         * The initial game window height
         */
        public Integer getHeight() {
            return options.height;
        }

        public Builder setHeight(Integer height) {
            options.height = height;
            return this;
        }

        /**
         * Is inital game window fullscreen.
         */
        public boolean isFullscreen() {
            return options.fullscreen;
        }

        public Builder setFullscreen(boolean fullscreen) {
            options.fullscreen = fullscreen;
            return this;
        }

        /**
         * The server ip that will connect to when enter game main menu.
         */
        public String getServerIp() {
            return options.serverIp;
        }

        public Builder setServerIp(String serverIp) {
            options.serverIp = serverIp;
            return this;
        }

        /**
         * i.e. optirun
         */
        public String getWrapper() {
            return options.wrapper;
        }

        public Builder setWrapper(String wrapper) {
            options.wrapper = wrapper;
            return this;
        }

        /**
         * Proxy settings
         */
        public Proxy getProxy() {
            return options.proxy;
        }

        public Builder setProxy(Proxy proxy) {
            options.proxy = proxy;
            return this;
        }

        /**
         * The user name of the proxy, optional.
         */
        public String getProxyUser() {
            return options.proxyUser;
        }

        public Builder setProxyUser(String proxyUser) {
            options.proxyUser = proxyUser;
            return this;
        }

        /**
         * The password of the proxy, optional
         */
        public String getProxyPass() {
            return options.proxyPass;
        }

        public Builder setProxyPass(String proxyPass) {
            options.proxyPass = proxyPass;
            return this;
        }

        /**
         * Prevent game launcher from generating default JVM arguments like max memory.
         */
        public boolean isNoGeneratedJVMArgs() {
            return options.noGeneratedJVMArgs;
        }

        public Builder setNoGeneratedJVMArgs(boolean noGeneratedJVMArgs) {
            options.noGeneratedJVMArgs = noGeneratedJVMArgs;
            return this;
        }

        /**
         * Called command line before launching the game.
         */
        public String getPreLaunchCommand() {
            return options.preLaunchCommand;
        }

        public Builder setPreLaunchCommand(String preLaunchCommand) {
            options.preLaunchCommand = preLaunchCommand;
            return this;
        }

        public NativesDirectoryType getNativesDirType() {
            return options.nativesDirType;
        }

        public Builder setNativesDirType(NativesDirectoryType nativesDirType) {
            options.nativesDirType = nativesDirType;
            return this;
        }

        public String getNativesDir() {
            return options.nativesDir;
        }

        public Builder setNativesDir(String nativesDir) {
            options.nativesDir = nativesDir;
            return this;
        }

        public boolean isUseSoftwareRenderer() {
            return options.useSoftwareRenderer;
        }

        public Builder setUseSoftwareRenderer(boolean useSoftwareRenderer) {
            options.useSoftwareRenderer = useSoftwareRenderer;
            return this;
        }

        public boolean isUseNativeGLFW() {
            return options.useNativeGLFW;
        }

        public Builder setUseNativeGLFW(boolean useNativeGLFW) {
            options.useNativeGLFW = useNativeGLFW;
            return this;
        }

        public boolean isUseNativeOpenAL() {
            return options.useNativeOpenAL;
        }

        public Builder setUseNativeOpenAL(boolean useNativeOpenAL) {
            options.useNativeOpenAL = useNativeOpenAL;
            return this;
        }

        public boolean isDaemon() {
            return options.daemon;
        }

        public Builder setDaemon(boolean daemon) {
            options.daemon = daemon;
            return this;
        }

        public Builder setPostExitCommand(String postExitCommand) {
            options.postExitCommand = postExitCommand;
            return this;
        }

        public Builder setProcessPriority(@NotNull ProcessPriority processPriority) {
            options.processPriority = processPriority;
            return this;
        }

        public Builder setHiperMode(boolean hiperMode) {
            options.hiperMode = hiperMode;
            return this;
        }

        public Builder setHiperMode(boolean hiperMode){
            options.hiperMode = hiperMode;
            return this;
        }

    }
}
