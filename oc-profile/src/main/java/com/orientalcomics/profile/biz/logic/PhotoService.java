package com.orientalcomics.profile.biz.logic;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.MapMaker;
import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;
import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.logic.ProfileConfigs.IntegerConfigView;

@Service
public class PhotoService implements OcProfileConstants {

    @Autowired
    ProfileConfigHelper profileConfigHelper;

    private Map<String, UploadedPhotoItem> uploadedImgPathMap = new MapMaker().concurrencyLevel(50).expiration(1, TimeUnit.HOURS).makeMap();

    /**
     * 获得图片的文件大小上限，单位为M。
     *
     * @return
     */
    public int maxSize() {
        int maxSize = profileConfigHelper.getValue(IntegerConfigView.PHOTO_MAX_SIZE_M);
        if (maxSize < 0) {
            return 0;
        }
        return maxSize;
    }

    /**
     * 从Is中读取一个图片。
     *
     * @param is
     * @return
     * @throws IOException
     */
    public BufferedImage readImage(InputStream is) throws IOException {
        return ImageIO.read(is);
    }

    public UploadedPhotoItem getUploadedImageInfo(int userId, String token) {
        if (token == null) {
            return null;
        }
        return getFromMap(userId, token);
    }

    public void deleteUploadedImageInfo(int userId, String token) {
        if (token == null) {
            return;
        }
        UploadedPhotoItem item = getUploadedImageInfo(userId, token);
        if (item == null) {
            return;
        }
        removeFromMap(userId, token);
        FileUtils.deleteQuietly(new File(item.filePath));
    }

    /**
     * 加载已经上传的图片
     *
     * @param url
     * @return
     * @throws IOException
     */
    public BufferedImage loadUploadedImage(UploadedPhotoItem item) throws IOException {
        if (item == null) {
            return null;
        }
        FileInputStream is = null;
        BufferedImage result = null;
        try {
            is = new FileInputStream(item.filePath);//
            result = readImage(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return result;
    }

    /**
     * 保存用户上传的图片。
     *
     * @param userId
     * @param image
     * @return token
     * @throws IOException
     */
    public String saveUploadedImage(int userId, BufferedImage image) throws IOException {
        String path = genTmpPath(userId);
        String token = genToken();
        String filePath = writeImage(image, path);
        putToMap(userId, token, new UploadedPhotoItem(PHOTO_BASE_URL + path, filePath, image.getWidth(), image.getHeight()));
        return token;
    }

    public Map<Integer, String> saveScaledImages(int userId, BufferedImage image, int x, int y, int w, int h) throws IOException {
        Map<Integer, String> pathMap = genPath(userId);
        Map<Integer, String> result = new HashMap<Integer, String>(pathMap.size());
        image = image.getSubimage(x, y, w, h);// 先截取
        for (int size : PHOTO_SIZES) {
            BufferedImage newImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = newImage.createGraphics();
            ResampleOp resampleOp = new ResampleOp(size, size);
            resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
            BufferedImage rescaledImage = resampleOp.filter(image, null);
            g.drawImage(rescaledImage, 0, 0, null);
            g.dispose();
            String path = pathMap.get(size);
            if (path != null) {
                writeImage(newImage, path);
                result.put(size, PHOTO_BASE_URL + path);
            }

        }
        return result;
    }

    private String writeImage(BufferedImage image, String path) throws IOException {
        String filePath = PHOTO_DATA_DIR + path;
        File imagePath = new File(filePath);
        File dir = imagePath.getParentFile();
        if (!dir.exists()) {
            FileUtils.forceMkdir(dir);
        }
        ImageOutputStream output = new FileImageOutputStream(imagePath);
        ImageIO.write(image, PHOTO_EXT, output);
        return filePath;
    }

    /**
     * 临时的照片
     *
     * @return
     */
    public String genTmpPath(int userId) {
        // uploaded/{date}/{userId}_{hourmmss}_{random4}.jpg
        long now = System.currentTimeMillis();
        String date = DATE.format(now);
        String time = TIME.format(now);
        String random = RandomStringUtils.randomAlphabetic(4);
        return StringUtils.join(new Object[]{ //
                "/uploaded/", date, "/", userId, "_", time, "_", random, ".", PHOTO_EXT});
    }

    /**
     * 计算本地的文件名称
     *
     * @return key:size,value:path
     */
    public Map<Integer, String> genPath(int userId, int... sizes) {
        // 规则
        // photos/{user_hash}/userid/{date_hour_mm_ss}_{random4}_{size}.jpg
        int userHash = userId % 100;
        String time = DATE_TIME.format(System.currentTimeMillis());
        if (sizes == null || sizes.length == 0) {
            sizes = PHOTO_SIZES;
        }
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (int size : sizes) {
            String random = RandomStringUtils.randomAlphabetic(4);
            String path = StringUtils.join(new Object[]{"/photos/", userHash, "/", userId, "/", time, "_", random, "_", size, ".", PHOTO_EXT});
            result.put(size, path);
        }
        return result;
    }

    private String genToken() {
        return RandomStringUtils.randomAlphabetic(16);
    }

    private void putToMap(int userId, String token, UploadedPhotoItem item) {
        uploadedImgPathMap.put(userId + "_" + token, item);
    }

    private UploadedPhotoItem getFromMap(int userId, String token) {
        return uploadedImgPathMap.get(userId + "_" + token);
    }

    private void removeFromMap(int userId, String token) {
        uploadedImgPathMap.remove(userId + "_" + token);
    }

    public static final FastDateFormat DATE = FastDateFormat.getInstance("yyyyMMdd");
    public static final FastDateFormat TIME = FastDateFormat.getInstance("HHmmss");
    public static final FastDateFormat DATE_TIME = FastDateFormat.getInstance("yyyyMMddHHmmss");

    public static final class UploadedPhotoItem {
        public final String url;
        public final String filePath;
        public final int width;
        public final int height;
        public volatile int displayWidth;
        public volatile int displayHeight;

        public UploadedPhotoItem(String url, String filePath, int width, int height) {
            this.url = url;
            this.filePath = filePath;
            this.width = width;
            this.height = height;
        }
    }
}
