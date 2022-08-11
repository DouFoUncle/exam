package com.project.commons.util;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.ResourceUtils;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共的工具类
 * @author 斗佛Uncle
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
public class PublicUtils {

    /**
     * 获取HTML文本中的img标签
     * @param htmlStr
     * @return
     */
    public static List<String> getImgStr(String htmlStr) {
        List<String> list = new ArrayList<>();
        if(StrUtil.isBlankOrUndefined(htmlStr)) {
            return list;
        }
        String img = "";
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                m.group(1).replace(m.group(1), "123");
                list.add(m.group(1));
            }
        }
        return list;
    }

    /**
     * 将传入的html文本中的img标签的src替换
     * @param newsBody
     * @return
     */
    public static String documentBody(String newsBody, String httpHost, String filePath) {
        Element doc = Jsoup.parseBodyFragment(newsBody).body();
        Elements pngs = doc.select("img[src]");
        for (Element element : pngs) {
            String imgUrl = element.attr("src");
            // 获取截取src属性值中的Base64加密的地址
            String srcValue = null;
            String imgName = null;
            if(imgUrl.trim().indexOf(",") != -1 && imgUrl.trim().indexOf("base64") != -1) {
                // 处理base64
                srcValue = imgUrl.trim().substring(imgUrl.trim().indexOf(",") + 1);
                // 判断srcValue是否为空， 不为空则将拿到的Base64的地址解密，
                // 解密后将图片保存到本地并将src转换为https的方式访问
                if(!StrUtil.isBlankOrUndefined(srcValue)) {
                    // 解密Base64的内容，保存解密到的图片
                    imgName = generateImage(srcValue, filePath);
                }
            } else if(imgUrl.trim().indexOf("../") != -1) {
                // 处理相对路径
                imgName = imgUrl.trim().substring(imgUrl.trim().lastIndexOf("/") + 1);
            }
            if(StrUtil.isBlankOrUndefined(imgName)) {
                continue;
            }
            // 重新赋值
            element.attr("src", httpHost + imgName);
        }
        return doc.toString();
    }

    /**
     * base64字符串转图片
     * @param imgStr 图片的base64
     * @return
     */
    public static String generateImage(String imgStr, String path) {
        //如果图像数据为空
        if (imgStr == null) {
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            String localPath = ResourceUtils.getURL("classpath:").getPath().replace("%20"," ")+"/static/upload/notice/";
            if(!StrUtil.isBlankOrUndefined(path)) {
                localPath = path;
            }
            System.out.println(localPath);
            //解密
            byte[] b = decoder.decodeBuffer(imgStr);
            //处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //图片名称
            String fileName = UUID.randomUUID().toString().replace("-", "") + ".jpg";
            File image = new File(localPath+fileName);
            if (!image.exists()) {
                image.getParentFile().mkdir();
            }
            OutputStream out = new FileOutputStream(localPath+fileName);
            out.write(b);
            out.flush();
            out.close();
            // 返回图片名称，方便存入数据库中
            return (fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
