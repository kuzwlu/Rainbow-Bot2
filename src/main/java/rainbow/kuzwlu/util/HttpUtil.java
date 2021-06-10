package rainbow.kuzwlu.util;

import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @Author kuzwlu
 * @Description HttpClient
 * @Date 2020/11/16 19:55
 * @Email kuzwlu@gmail.com
 */
public class HttpUtil {

    // 编码格式。发送编码格式统一用UTF-8
    private static String ENCODING = "UTF-8";

    // 设置连接超时时间，单位毫秒。
    private static int HTTP_CONNECT_TIMEOUT = 6000;

    // 请求获取数据的超时时间(即响应时间)，单位毫秒。
    private static int HTTP_SOCKET_TIMEOUT = 6000;

    //  路由的默认最大连接
    private static int POOL_MAX_PERROUTE = 500;

    //  整个连接池连接的最大值
    private static int POOL_MAX_TOTAL = 1000;

    //  多线程时设置sleep
    private static int THREAD_SLEEP_TIME = 0;

    //  客户端从连接池中获取连接的超时时间
    private static int HTTP_CON_REQ_TIMEOUT = 100;

    //  执行302跳转,每次执行完毕，重置为true
    private static boolean DO302 = true;

    //  是否开启回滚操作，默认true
    private static boolean ROLLBACK = true;

    //  设置网页回滚的响应码
    private static Integer ROLLBACK_CODE = 429;

    //  请求头设置
    private static final Map<String, String> header = new HashMap<>();

    //  参数设置
    private static final Map<String, String> param = new HashMap<>();

    //  IP代理池
    private static final List<String> ipPool = new ArrayList<>();
    private static final Map<String, Integer> ipPoolFlag = new TreeMap<>();

    //  是否开始监控线程
    private static boolean MONITOR_THREAD = false;

    //  httpclient实例
    private static CloseableHttpClient httpClient;

    //  监控线程
    private static ScheduledExecutorService monitorExecutor;

    //  连接池管理器
    private PoolingHttpClientConnectionManager connManager;

    private HttpUtil() {
        connManager = new PoolingHttpClientConnectionManager();
        //  设置socket配置
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();
        connManager.setDefaultSocketConfig(socketConfig);
        connManager.setMaxTotal(POOL_MAX_TOTAL);
        connManager.setDefaultMaxPerRoute(POOL_MAX_PERROUTE);
        //  设置获取连接超时时间、建立连接超时时间、从服务端读取数据的超时时间
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                .setConnectTimeout(HTTP_CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(HTTP_CON_REQ_TIMEOUT)
                .build();
        //  创建httpclient实例
        httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
        //开启监控线程,对异常和空闲线程进行关闭
        monitorExecutor = Executors.newScheduledThreadPool(1, r -> new Thread(r, "monitor-connManager-thread"));
    }

    private static final class SingletonHolder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    public static HttpUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 默认配置
     * 编码格式：ENCODING = "UTF-8"
     * 连接超时时间：HTTP_CONNECT_TIMEOUT = 6000
     * 响应超时时间：HTTP_SOCKET_TIMEOUT = 6000
     * 路由的默认最大连接：POOL_MAX_PERROUTE = 500
     * 连接池连接的最大值：POOL_MAX_TOTAL = 1000
     * 连接池获取连接超时时间：HTTP_CON_REQ_TIMEOUT = 100
     * 是否允许302跳转：DO302 = true
     * 用户自定义配置
     */
    //重置配置
    public HttpUtil resetConfig() {
        ENCODING = "UTF-8";
        HTTP_CONNECT_TIMEOUT = 6000;
        HTTP_SOCKET_TIMEOUT = 6000;
        POOL_MAX_PERROUTE = 500;
        POOL_MAX_TOTAL = 1000;
        THREAD_SLEEP_TIME = 0;
        HTTP_CON_REQ_TIMEOUT = 100;
        DO302 = true;
        ROLLBACK = true;
        ROLLBACK_CODE = 429;
        MONITOR_THREAD = false;
        return this;
    }

    public HttpUtil config(
            boolean do302
    ) {
        DO302 = do302;
        return this;
    }

    public HttpUtil config(
            Integer threadSleepTime
    ) {
        THREAD_SLEEP_TIME = threadSleepTime;
        return this;
    }

    public HttpUtil config(
            String encoding
    ) {
        ENCODING = encoding;
        return this;
    }

    public HttpUtil config(
            String encoding,
            boolean do302
    ) {
        ENCODING = encoding;
        return config(do302);
    }

    public HttpUtil config(
            boolean do302,
            boolean rollback
    ) {
        DO302 = do302;
        ROLLBACK = rollback;
        return this;
    }

    public HttpUtil config(
            String encoding,
            Integer thread_sleep_time
    ) {
        THREAD_SLEEP_TIME = thread_sleep_time;
        return config(encoding);
    }

    public HttpUtil config(
            String encoding,
            Integer thread_sleep_time,
            boolean do302
    ) {
        DO302 = do302;
        return config(encoding, thread_sleep_time);
    }

    public HttpUtil config(
            String encoding,
            Integer thread_sleep_time,
            boolean do302,
            boolean rollback
    ) {
        ROLLBACK = rollback;
        return config(encoding, thread_sleep_time, do302);
    }

    public HttpUtil config(
            String encoding,
            Integer thread_sleep_time,
            boolean do302,
            boolean rollback,
            boolean monitor_thread
    ) {
        MONITOR_THREAD = monitor_thread;
        return config(encoding, thread_sleep_time, do302, rollback);
    }

    public HttpUtil config(
            String encoding,
            Integer thread_sleep_time,
            boolean do302,
            boolean rollback,
            boolean monitor_thread,
            Integer rollback_code
    ) {
        ROLLBACK_CODE = rollback_code;
        return config(encoding, thread_sleep_time, do302, rollback, monitor_thread);
    }

    public HttpUtil config(
            String encoding,
            Integer thread_sleep_time,
            boolean do302,
            boolean rollback,
            boolean monitor_thread,
            Integer rollback_code,
            Integer http_connect_timeout
    ) {
        HTTP_CONNECT_TIMEOUT = http_connect_timeout;
        return config(encoding, thread_sleep_time, do302, rollback, monitor_thread, rollback_code);
    }

    public HttpUtil config(
            String encoding,
            Integer thread_sleep_time,
            boolean do302,
            boolean rollback,
            boolean monitor_thread,
            Integer rollback_code,
            Integer http_connect_timeout,
            Integer http_socket_timeout
    ) {
        HTTP_SOCKET_TIMEOUT = http_socket_timeout;
        return config(encoding, thread_sleep_time, do302, rollback, monitor_thread, rollback_code, http_connect_timeout);
    }

    public HttpUtil config(
            String encoding,
            Integer thread_sleep_time,
            boolean do302,
            boolean rollback,
            boolean monitor_thread,
            Integer rollback_code,
            Integer http_connect_timeout,
            Integer http_socket_timeout,
            Integer pool_max_perroute
    ) {
        POOL_MAX_PERROUTE = pool_max_perroute;
        return config(encoding, thread_sleep_time, do302, rollback, monitor_thread, rollback_code, http_connect_timeout, http_socket_timeout);
    }

    public HttpUtil config(
            String encoding,
            Integer thread_sleep_time,
            boolean do302,
            boolean rollback,
            boolean monitor_thread,
            Integer rollback_code,
            Integer http_connect_timeout,
            Integer http_socket_timeout,
            Integer pool_max_perroute,
            Integer pool_max_total
    ) {
        POOL_MAX_TOTAL = pool_max_total;
        return config(encoding, thread_sleep_time, do302, rollback, monitor_thread, rollback_code, http_connect_timeout, http_socket_timeout, pool_max_perroute);
    }

    public HttpUtil config(
            String encoding,
            Integer thread_sleep_time,
            boolean do302,
            boolean rollback,
            boolean monitor_thread,
            Integer rollback_code,
            Integer http_connect_timeout,
            Integer http_socket_timeout,
            Integer pool_max_perroute,
            Integer pool_max_total,
            Integer http_con_req_timeout
    ) {
        HTTP_CON_REQ_TIMEOUT = http_con_req_timeout;
        return config(encoding, thread_sleep_time, do302, rollback, monitor_thread, rollback_code, http_connect_timeout, http_socket_timeout, pool_max_perroute, pool_max_total);
    }

    public HttpUtil option(HttpUtilEnum option, Object value) {
        String valueE = value.toString();
        switch (option) {
            case ENCODING:
                ENCODING = valueE;
                break;
            case HTTP_CONNECT_TIMEOUT:
                HTTP_CONNECT_TIMEOUT = Integer.parseInt(valueE);
                break;
            case HTTP_SOCKET_TIMEOUT:
                HTTP_SOCKET_TIMEOUT = Integer.parseInt(valueE);
                break;
            case POOL_MAX_PERROUTE:
                POOL_MAX_PERROUTE = Integer.parseInt(valueE);
                break;
            case POOL_MAX_TOTAL:
                POOL_MAX_TOTAL = Integer.parseInt(valueE);
                break;
            case THREAD_SLEEP_TIME:
                THREAD_SLEEP_TIME = Integer.parseInt(valueE);
                break;
            case HTTP_CON_REQ_TIMEOUT:
                HTTP_CON_REQ_TIMEOUT = Integer.parseInt(valueE);
                break;
            case DO302:
                DO302 = Boolean.parseBoolean(valueE);
                break;
            case ROLLBACK:
                ROLLBACK = Boolean.parseBoolean(valueE);
                break;
            case ROLLBACK_CODE:
                ROLLBACK_CODE = Integer.parseInt(valueE);
                break;
            case MONITOR_THREAD:
                MONITOR_THREAD = Boolean.parseBoolean(valueE);
                break;
            default:
                new RuntimeException("请选择正确的配置!");
        }
        return this;
    }

    /**
     * 添加请求头参数
     *
     * @param key   键值
     * @param value 值
     * @return HttpUtil
     */
    public HttpUtil addHeader(
            String key,
            String value
    ) {
        if ("".equals(key) || key == null || "".equals(value) || value == null) {
            return this;
        }
        header.put(key, value);
        return this;
    }

    /**
     * 添加请求头参数
     * 传入一个Map<String,String>
     *
     * @param headers 请求头
     * @return HttpUtil
     */
    public HttpUtil addHeader(
            Map<String, String> headers
    ) {
        if (headers.size() == 0) {
            return this;
        }
        header.putAll(headers);
        return this;
    }

    /**
     * 传入表单内容
     *
     * @param key   键值
     * @param value 值
     * @return HttpUtil
     */
    public HttpUtil addParam(
            String key,
            String value
    ) {
        if ("".equals(key) || key == null || "".equals(value) || value == null) {
            return this;
        }
        param.put(key, value);
        return this;
    }

    /**
     * 传入表单内容
     *
     * @param params 表单内容
     * @return HttpUtil
     */
    public HttpUtil addParam(
            Map<String, String> params
    ) {
        if (params.size() == 0) {
            return this;
        }
        param.putAll(params);
        return this;
    }

    /**
     * 添加IP
     *
     * @param ip-String
     * @return HttpUtil
     */
    public HttpUtil addIP(
            String ip
    ) {
        isIp(Collections.singletonList(ip));
        ipPool.add(ip);
        ipPoolFlagInit();
        return this;
    }

    /**
     * 添加IP
     *
     * @param ip-list
     * @return HttpUtil
     */
    public HttpUtil addIP(
            List<String> ip
    ) {
        isIp(ip);
        ipPool.addAll(ip);
        ipPoolFlagInit();
        return this;
    }

    public Result get(
            String url
    ) {
        isUrl(url);
        HttpRequestBase httpGet;
        // 创建访问的地址
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (param != null) {
                param.forEach(uriBuilder::setParameter);
            }
            // 创建http对象
            httpGet = new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        /*
         * setConnectTimeout：设置连接超时时间，单位毫秒。
         * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
         * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
         * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
         */
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CONNECT_TIMEOUT).setSocketTimeout(HTTP_SOCKET_TIMEOUT).build();

        httpGet.setConfig(requestConfig);

        // 设置请求头
        packageHeader(httpGet);

        return getResult(httpClient, httpGet, DO302);
    }

    public Result post(
            String url
    ) {
        isUrl(url);
        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CONNECT_TIMEOUT).setSocketTimeout(HTTP_SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        // 设置请求头
        packageHeader(httpPost);

        // 封装请求参数
        packageParam(httpPost);

        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;

        return getResult(httpClient, httpPost, DO302);

    }

    public Result put(
            String url
    ) {
        isUrl(url);
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CONNECT_TIMEOUT).setSocketTimeout(HTTP_SOCKET_TIMEOUT).build();
        httpPut.setConfig(requestConfig);

        packageParam(httpPut);

        CloseableHttpResponse httpResponse = null;

        return getResult(httpClient, httpPut, DO302);
    }

    public Result delete(
            String url
    ) {
        isUrl(url);
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CONNECT_TIMEOUT).setSocketTimeout(HTTP_SOCKET_TIMEOUT).build();
        httpDelete.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        return getResult(httpClient, httpDelete, DO302);
    }

    public Result head(
            String url
    ) {
        isUrl(url);
        HttpHead httpHead = new HttpHead(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CONNECT_TIMEOUT).setSocketTimeout(HTTP_SOCKET_TIMEOUT).build();
        httpHead.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        return getResult(httpClient, httpHead, DO302);
    }

    public Result options(
            String url
    ) {
        isUrl(url);
        HttpOptions httpOptions = new HttpOptions(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CONNECT_TIMEOUT).setSocketTimeout(HTTP_SOCKET_TIMEOUT).build();
        httpOptions.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        return getResult(httpClient, httpOptions, DO302);
    }

    public Result patch(
            String url
    ) {
        isUrl(url);
        HttpPatch httpPatch = new HttpPatch(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CONNECT_TIMEOUT).setSocketTimeout(HTTP_SOCKET_TIMEOUT).build();
        httpPatch.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        return getResult(httpClient, httpPatch, DO302);
    }

    public Result trace(
            String url
    ) {
        isUrl(url);
        HttpTrace httpTrace = new HttpTrace(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CONNECT_TIMEOUT).setSocketTimeout(HTTP_SOCKET_TIMEOUT).build();
        httpTrace.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        return getResult(httpClient, httpTrace, DO302);
    }

    public Result download(
            String url,
            String filePath
    ) {
        isUrl(url);
        HttpRequestBase httpGet;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (param != null) {
                param.forEach(uriBuilder::setParameter);
            }
            // 创建http对象
            httpGet = new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CONNECT_TIMEOUT).setSocketTimeout(HTTP_SOCKET_TIMEOUT).build();

        httpGet.setConfig(requestConfig);

        // 设置请求头
        packageHeader(httpGet);

        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;

        // 执行请求并获得响应结果
        return getDownloadResult(httpClient, httpGet, filePath, DO302);
    }


    /**
     * 处理响应结果，返回Result
     *
     * @param httpClient httpClient
     * @param httpMethod httpMethod
     * @param do302      do302
     * @return HttpUtil.Result
     */
    private Result getResult(CloseableHttpClient httpClient, HttpRequestBase httpMethod, boolean do302) {
        if (MONITOR_THREAD) {
            startMonitorThread();
        }
        if (THREAD_SLEEP_TIME != 0) {
            synchronized (httpClient) {
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        RequestConfig requestConfig;
        if (!do302) {
            requestConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .setRedirectsEnabled(false)
                    .setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                    .setConnectTimeout(HTTP_CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(HTTP_CON_REQ_TIMEOUT)
                    .build();
        } else {
            requestConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                    .setConnectTimeout(HTTP_CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(HTTP_CON_REQ_TIMEOUT)
                    .build();
        }
        if (!ipPool.isEmpty()) {
            return doIPPoolResult(requestConfig, httpMethod, null);
        }
        return doNotIPResult(httpMethod, requestConfig, null);
    }


    /**
     * 下载文件处理响应结果，返回Result
     *
     * @param httpClient httpClient
     * @param httpMethod httpMethod
     * @param filePath   filePath
     * @param do302      do302
     * @return HttpUtil.Result
     */
    private Result getDownloadResult(CloseableHttpClient httpClient, HttpRequestBase httpMethod, String filePath, boolean do302) {
        if (MONITOR_THREAD) {
            startMonitorThread();
        }
        if (THREAD_SLEEP_TIME != 0) {
            synchronized (httpClient) {
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        RequestConfig requestConfig;
        if (!do302) {
            requestConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .setRedirectsEnabled(false)
                    .setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                    .setConnectTimeout(HTTP_CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(HTTP_CON_REQ_TIMEOUT)
                    .build();
        } else {
            requestConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                    .setConnectTimeout(HTTP_CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(HTTP_CON_REQ_TIMEOUT)
                    .build();
        }
        if (!ipPool.isEmpty()) {
            return doIPPoolResult(requestConfig, httpMethod, filePath);
        }
        return doNotIPResult(httpMethod, requestConfig, filePath);
    }


    private Result doIPPoolResult(RequestConfig requestConfig, HttpRequestBase httpMethod, String filePath) {
        boolean gogoFlag = true;
        boolean ipFlag = true;
        CloseableHttpResponse httpResponse = null;
        while (gogoFlag) {
            if (ipOver()) {
                throw new RuntimeException("IP池没有可用的IP！");
            }
            String key = "";
            for (Map.Entry<String, Integer> map : ipPoolFlag.entrySet()) {
                Integer value = map.getValue();
                //0表示ip有用
                if (value == 0 && ipFlag) {
                    key = map.getKey();
                    String[] split = key.split(":");
                    HttpHost httpHost = new HttpHost(split[0], Integer.parseInt(split[1]));
                    requestConfig = RequestConfig.custom()
                            .setCookieSpec(CookieSpecs.STANDARD)
                            .setRedirectsEnabled(false)
                            .setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                            .setConnectTimeout(HTTP_CONNECT_TIMEOUT)
                            .setConnectionRequestTimeout(HTTP_CON_REQ_TIMEOUT)
                            .build();
                    httpMethod.setConfig(RequestConfig.custom().setProxy(httpHost).build());
                    ipFlag = false;
                }
            }
            try {
                httpResponse = httpClient.execute(httpMethod);
                // 获取返回结果
                if (httpResponse != null && httpResponse.getStatusLine() != null) {
                    isSuccessCode(httpResponse.getStatusLine().getStatusCode());
                    ByteArrayOutputStream content = null;
                    Map<String, String> cookie = new HashMap<>();
                    if (httpResponse.getEntity() != null) {
                        cookie = getCookieFromResponse(httpResponse);
                        content = readInputStream(httpResponse.getEntity().getContent());
                        if (filePath != null) {
                            File file = new File(filePath);
                            FileOutputStream fileout = new FileOutputStream(file);
                            fileout.write(content.toByteArray());
                            fileout.close();
                        }
                    }
                    Map<String, String> header = headerToMap(httpResponse.getAllHeaders());
                    gogoFlag = false;
                    Result result = new Result(httpResponse.getStatusLine().getStatusCode(), content.toString(ENCODING), cookie, header);
                    //回滚操作
                    if (ROLLBACK) {
                        if (result.code == ROLLBACK_CODE) {
                            System.out.println("爬取网页" + httpMethod.getURI() + "失败\t网页状态码：" + result.code + "\t\t正在执行回滚操作");
                            setIPNext(true);
                            doIPPoolResult(requestConfig, httpMethod, filePath);
                        }
                    }
                    return result;
                }
                gogoFlag = false;
                return new Result(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                gogoFlag = true;
                ipFlag = true;
                ipPoolFlag.put(key, 1);
                System.err.println("IP：[" + key + "]已失效，已使用下个代理IP重新连接！");
            } finally {
                release(httpResponse);
            }
        }
        return new Result(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * 没有IP
     *
     * @param httpMethod    httpMethod
     * @param requestConfig requestConfig
     * @return Result
     */
    private Result doNotIPResult(HttpRequestBase httpMethod, RequestConfig requestConfig, String filePath) {
        httpMethod.setConfig(requestConfig);
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpMethod);
            // 获取返回结果
            if (httpResponse != null && httpResponse.getStatusLine() != null) {
                ByteArrayOutputStream content = null;
                Map<String, String> cookie = new HashMap<>();
                if (httpResponse.getEntity() != null) {
                    cookie = getCookieFromResponse(httpResponse);
                    content = readInputStream(httpResponse.getEntity().getContent());
                    if (filePath != null) {
                        File file = new File(filePath);
                        FileOutputStream fileout = new FileOutputStream(file);
                        fileout.write(content.toByteArray());
                        fileout.close();
                    }
                }
                Map<String, String> header = headerToMap(httpResponse.getAllHeaders());
                Result result = new Result(httpResponse.getStatusLine().getStatusCode(), content.toString(ENCODING), cookie, header);
                //回滚操作
                if (ROLLBACK) {
                    if (result.code == ROLLBACK_CODE) {
                        System.out.println("爬取网页" + httpMethod.getURI() + "失败\t网页状态码：" + result.code + "\t\t正在执行回滚操作");
                        setIPNext(true);
                        doNotIPResult(httpMethod, requestConfig, filePath);
                    }
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(httpResponse);
        }
        return new Result(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * 手动标记当前IP失效
     *
     * @param flag flag
     */
    private void setIPNext(boolean flag) {
        boolean ipFlag = true;
        if (flag) {
            for (Map.Entry<String, Integer> map : ipPoolFlag.entrySet()) {
                if (ipFlag && map.getValue() == 0) {
                    ipPoolFlag.put(map.getKey(), 1);
                    System.out.println("已切换IP：" + map.getKey());
                    ipFlag = false;
                }
            }
        }
    }

    /**
     * 用于判断响应状态码是否正确
     *
     * @param code code
     */
    private void isSuccessCode(int code) throws Exception {
        String codeS = String.valueOf(code);
        if (codeS.startsWith("4") || codeS.startsWith("5")) {
            throw new Exception();
        }
    }

    /**
     * 用于验证是否正确的url
     *
     * @param url 链接
     */
    private void isUrl(String url) {
        boolean http = Pattern.matches("http://.*", url);
        boolean https = Pattern.matches("https://.*", url);
        if (!http & !https) {
            throw new RuntimeException("请输入以http开头的URL链接！");
        }

    }

    /**
     * 验证ip是否合法
     *
     * @param ipList ipList
     */
    private void isIp(List<String> ipList) {
        ipList.forEach(ip -> {
            try {
                String[] split = ip.split(":");
                if (split.length == 2) {
                    String[] split1 = split[0].split("\\.");
                    int port = Integer.parseInt(split[1]);
                    if (split1.length != 4) {
                        IpException(ip);
                    } else if (split1.length == 4) {
                        Arrays.stream(split1).forEach(value -> {
                            int number = Integer.parseInt(value);
                            if (value.length() == 0 || value.length() > 3) {
                                IpException(ip);
                            }
                            if (number > 255) {
                                IpException(ip);
                            }
                        });
                    }
                    if (0 > port || port > 65535) {
                        IpException(ip);
                    }
                } else {
                    IpException(ip);
                }
            } catch (Exception e) {
                IpException(ip);
            }
        });
    }

    /**
     * 抛出异常
     * @param ip
     */
    private void IpException(String ip){
        throw new RuntimeException("添加的IP：[" + ip + "]\t不合法！");
    }

    /**
     * 判断ip是否轮训完-检测ip池中是否还有有效ip
     *
     * @return boolean
     */
    private boolean ipOver() {
        int i = 0;
        for (Map.Entry<String, Integer> map : ipPoolFlag.entrySet()) {
            if (map.getValue() == 1) {
                i++;
            }
        }
        return i >= ipPoolFlag.size();
    }

    /**
     * 用于判断ip是否有效-当IP加入IP池
     */
    private void ipPoolFlagInit() {
        ipPool.forEach(ip -> {
            if (ipPoolFlag.size() != 0) {
                for (Map.Entry<String, Integer> map : ipPoolFlag.entrySet()) {
                    if (map.getValue() == 1) {
                        ipPoolFlag.put(ip, 1);
                    } else {
                        ipPoolFlag.put(ip, 0);
                    }
                    return;
                }
            }
            ipPoolFlag.put(ip, 0);
        });
    }

    /**
     * 提供一个工具，把Map形式得cookie转为String，以便Set—Cookie使用
     *
     * @param cookie cookie
     * @return String
     */
    public String cookieHeader(Map<String, String> cookie) {
        StringBuilder stringBuilder = new StringBuilder();
        cookie.forEach((key, value) -> stringBuilder.append(key).append("=").append(value).append(";"));
        return stringBuilder.toString();
    }

    /**
     * 将Header[]转为Map
     *
     * @param headers 请求头
     * @return Map<String, String>
     */
    private Map<String, String> headerToMap(Header[] headers) {
        Map<String, String> headerMap = new HashMap<>();
        Arrays.stream(headers).forEach((header) -> headerMap.put(header.getName(), header.getValue()));
        return headerMap;
    }

    /**
     * 从响应获取cookie
     *
     * @param response 响应
     * @return Map
     */
    private Map<String, String> getCookieFromResponse(HttpResponse response) {
        Map<String, String> cookie = new HashMap<>();
        Header[] responseHeader = response.getHeaders("Set-Cookie");
        Arrays.stream(responseHeader)
                .filter(Objects::nonNull)
                .filter(responses -> "Set-Cookie".equals(responses.getName()) || ("set-cookie").equals(responses.getName()))
                .forEach(responses -> {
                    int i1 = responses.getValue().indexOf("=");
                    int i2 = responses.getValue().indexOf(";");
                    if (i1 != -1 && i2 != -1) {
                        String _value = responses.getValue().substring(i1 + 1, i2);
                        String _key = responses.getValue().substring(0, i1);
                        if (cookie.containsKey(_key)) {
                            if (!_value.isEmpty()) {
                                cookie.put(_key, _value);
                            }
                        } else {
                            cookie.put(_key, _value);
                        }
                    }
                });
        return cookie;
    }

    /**
     * Description: 封装请求头
     *
     * @param httpMethod httpMethod
     */
    private void packageHeader(HttpRequestBase httpMethod) {
        // 封装请求头
        if (header != null) {
            header.forEach(httpMethod::setHeader);
        }
    }

    /**
     * Description: 封装请求参数
     *
     * @param httpMethod httpMethod
     */
    private void packageParam(HttpEntityEnclosingRequestBase httpMethod) {
        // 封装请求参数
        if (param != null) {
            List<NameValuePair> nvps = new ArrayList<>();
            param.forEach((key, value) -> nvps.add(new BasicNameValuePair(key, value)));
            // 设置到请求的http对象中
            try {
                httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 将流中数据转化为字符串
     *
     * @param inputStream 输入流
     * @return String
     * @throws IOException IOException
     */
    private static String stream2string(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, ENCODING);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder resultBuilder = new StringBuilder();
        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            resultBuilder.append(temp);
        }
        inputStreamReader.close();
        bufferedReader.close();
        return resultBuilder.toString();
    }

    private static ByteArrayOutputStream readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream;
    }


    /**
     * Description: 释放资源
     *
     * @param httpResponse httpResponse
     */
    private void release(CloseableHttpResponse httpResponse) {
        if (httpResponse != null) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        header.clear();
        param.clear();
        DO302 = true;
        ROLLBACK = true;
    }

    /**
     * 关闭线程监控
     */
    private void closeMonitorThread() {
        monitorExecutor.shutdown();
    }

    /**
     * 开启线程监控
     */
    private void startMonitorThread() {
        monitorExecutor.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //关闭异常连接
                connManager.closeExpiredConnections();
                //关闭5s空闲的连接
                connManager.closeIdleConnections(5000, TimeUnit.MILLISECONDS);
            }
        }, 500, 2500, TimeUnit.MILLISECONDS);
    }

    /**
     * 清空IP池
     */
    public void clearIPPool() {
        ipPool.clear();
        ipPoolFlag.clear();
    }

    /**
     * 响应结果
     * code 响应码
     * result 响应数据
     * cookie
     * header
     */
    public static class Result implements Serializable {

        private static final long serialVersionUID = 2168152194164783950L;

        public Result() {

        }

        public Result(int code) {
            this.code = code;
        }

        public Result(String result) {
            this.result = result;
        }

        public Result(int code, String result, Map<String, String> cookie) {
            this.code = code;
            this.result = result;
            this.cookie = cookie;
        }

        public Result(int code, String result, Map<String, String> cookie, Map<String, String> header) {
            this.code = code;
            this.result = result;
            this.cookie = cookie;
            this.header = header;
        }

        /**
         * 响应状态码
         */
        private int code;

        /**
         * 响应数据
         */
        private String result;

        private Map<String, String> cookie;

        private Map<String, String> header;


        public Map<String, String> getCookie() {
            return cookie;
        }


        public int getCode() {
            return code;
        }


        public String getResult() {
            return result;
        }


        public Map<String, String> getHeader() {
            return header;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "code=" + code +
                    ", result=" + result +
                    ", cookie=" + cookie +
                    ", header=" + header +
                    '}';
        }
    }

    public enum HttpUtilEnum {
        // 编码格式。发送编码格式统一用UTF-8
        ENCODING,

        // 设置连接超时时间，单位毫秒。
        HTTP_CONNECT_TIMEOUT,

        // 请求获取数据的超时时间(即响应时间)，单位毫秒。
        HTTP_SOCKET_TIMEOUT,

        //  路由的默认最大连接
        POOL_MAX_PERROUTE,

        //  整个连接池连接的最大值
        POOL_MAX_TOTAL,

        //多线程时设置sleep
        THREAD_SLEEP_TIME,

        //  客户端从连接池中获取连接的超时时间
        HTTP_CON_REQ_TIMEOUT,

        //执行302跳转,每次执行完毕，重置为true
        DO302,

        //是否开启回滚操作，默认true
        ROLLBACK,

        //设置网页回滚的响应码
        ROLLBACK_CODE,

        //是否开启监控线程
        MONITOR_THREAD
    }

}