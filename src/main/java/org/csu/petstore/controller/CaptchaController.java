package org.csu.petstore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/captcha")
public class CaptchaController {

    @Autowired
    private HttpSession session;

    private static final int WIDTH = 100;
    private static final int HEIGHT = 40;
    private static final int CODE_LENGTH = 4;
    private static final char[] CODE_SEQUENCE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '2', '3', '4', '5', '6', '7', '8', '9'
    };

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCaptcha() throws IOException {
        // 创建图像缓冲区
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 设置边框
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        // 添加干扰线
        Random random = new Random();
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 生成随机验证码
        StringBuilder captchaCode = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            String strRand = String.valueOf(CODE_SEQUENCE[random.nextInt(CODE_SEQUENCE.length)]);
            captchaCode.append(strRand);
            // 将验证码显示到图像中
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString(strRand, 20 * i + 10, 25);
        }

        // 将验证码存入session
        session.setAttribute("captcha", captchaCode.toString());

        // 释放图形上下文
        g.dispose();

        // 将图像转换为Base64编码的字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", baos);
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        return "{\"captchaImage\":\"data:image/jpeg;base64," + base64Image + "\"}";
    }
}