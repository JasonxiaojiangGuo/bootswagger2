package com.jason.bootswagger2;

import com.alibaba.fastjson.JSON;
import com.jason.bootswagger2.util.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.net.ssl.SSLContext;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class Bootswagger2ApplicationTests {

    @Test
    public void contextLoads() throws Exception {
        System.out.println(32 >> 5);


        BitSet bitSet=new BitSet(10000000);
        bitSet.set(898, true);
        System.out.println(bitSet.get(898));
        System.out.println(bitSet.size());

        Map<String,Object> map = new HashMap<>();
        map.put("key","测试:count");
        map.put("value",123456);

        //HttpUtils.postString("http://192.168.80.40:9090/my/redisUser/strSet",map);
        //HttpUtils.getString("https://www.baidu.com");

        qqBom();

    }

    /**
     * 文字
     * @throws AWTException
     */
    public void qqBom() throws Exception {
        Random random = new Random();
        Transferable tText = new StringSelection("从前有座山 山里有座庙 庙里有个老和尚和小和尚 老和尚对小和尚说：傻逼了吧！");// 定义要发送的话
        //Image image=new ImageIcon("C:\\Users\\Administrator\\Desktop\\background.png").getImage();//这里是获取图片，图片路径自己确定
        Image image = new ImageIcon(new URL("http://img.tupianzj.com/uploads/allimg/160703/9-160F3145500.jpg"),"jpg").getImage();

        Robot robot = new Robot();// 创建Robot对象

        //自动打开聊天窗口
        String QQ="1667550226";//这里设置你要发送的QQ号，需要已经添加好友
        String url="http://wpa.qq.com/msgrd?v=3&uin="+QQ+"&site=qq&menu=yes";//设置调用聊天框url
        String cmd = "explorer \"" +url+"\"";//通过cmd命令使用默认浏览器访问url
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            proc.waitFor();
        } catch (Exception e) { e.printStackTrace(); }

        robot.delay(3000);// 延迟6秒，主要是为了预留出打开窗口的时间，括号内的单位为毫秒
        robot.keyPress(KeyEvent.VK_LEFT);  //按左键
        robot.keyPress(KeyEvent.VK_ENTER); //按回车

        //粘贴板
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        for (int j = 0; j < 5000; j++) {//循环次数
            //粘贴板里设置类容，随机设置图片还是文字
            if(random.nextBoolean()){//设置图片
                clip.setContents(tText, null);
            }else {//设置文字
                setClipboardImage(image,clip);
            }

            // 以下两行按下了ctrl+v，完成粘贴功能
            robot.keyPress(KeyEvent.VK_CONTROL);//按下ctrl键
            robot.keyPress(KeyEvent.VK_V);//按下v键

            // 释放ctrl按键，像ctrl，退格键，删除键这样的功能性按键，在按下后一定要释放，不然会出问题。crtl如果按住没有释放，在按其他字母按键是，敲出来的回事ctrl的快捷键。
            robot.keyRelease(KeyEvent.VK_CONTROL);
            // 延迟一秒再发送，不然会一次性全发布出去，因为电脑的处理速度很快，每次粘贴发送的速度几乎是一瞬间，所以给人的感觉就是一次性发送了全部。这个时间可以自己改，想几秒发送一条都可以
            robot.delay(500);
            //按回车
            robot.keyPress(KeyEvent.VK_ENTER);
        }
    }

    /**
     * 将图片设置到粘贴板
     * @param image
     */
    public void setClipboardImage(final Image image,Clipboard clip) {
        Transferable trans = new Transferable() {
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] { DataFlavor.imageFlavor };
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }

            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor))
                    return image;
                throw new UnsupportedFlavorException(flavor);
            }

        };
        clip.setContents(trans, null);
    }
}
