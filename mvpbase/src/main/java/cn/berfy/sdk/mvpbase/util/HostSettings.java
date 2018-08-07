package cn.berfy.sdk.mvpbase.util;

/**
 * HOST管理
 * Created by Berfy on 2016-08-10.
 */
public class HostSettings {

    /*
     * 服务器地址---开发
     */
    private static final String HOST_DEV = "http://192.168.1.27:18610/";

    /*;
     * 服务器地址---测试
     */
    private static final String HOST_TEST = "http://223.203.221.49:18610/";

    /*
     * 服务器地址---封测准正式服
     */
    private static final String HOST_PREPARED = "http://223.203.221.48:8110/";

    /*
     * 服务器地址--正式
     */
    private static final String HOST_PRO = "https://game.axingxing.com/";

    public final static int HOST_TYPE_DEV = 0;
    public final static int HOST_TYPE_TEST = 1;
    public final static int HOST_TYPE_PREPARED = 2;
    public final static int HOST_TYPE_PRO = 3;

    //设置默认服务器地址
    private static int HOST_TYPE = HostSettings.HOST_TYPE_PRO;

    public static String getHost() {
        String host = HostSettings.HOST_DEV;
        switch (HOST_TYPE) {
            case HostSettings.HOST_TYPE_DEV:
                host = HOST_DEV;
                break;
            case HostSettings.HOST_TYPE_TEST:
                host = HOST_TEST;
                break;
            case HostSettings.HOST_TYPE_PREPARED:
                host = HOST_PREPARED;
                break;
            case HostSettings.HOST_TYPE_PRO:
                host = HOST_PRO;
                break;
        }
        return host;
    }

    public static int getHostType() {
        return HOST_TYPE;
    }

    public static void setHostType(int type) {
        HOST_TYPE = type;
    }
}
