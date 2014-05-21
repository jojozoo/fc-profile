package com.orientalcomics.profile.web.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.orientalcomics.profile.biz.logic.PhotoService;
import com.orientalcomics.profile.biz.logic.PhotoService.UploadedPhotoItem;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.core.base.HtmlPage;
import com.orientalcomics.profile.web.controllers.internal.LoginRequiredController;


public class PhotosController extends LoginRequiredController {
    private static final long STEP    = 1024;
    private static final long BYTES_K = STEP;
    private static final long BYTES_M = BYTES_K * STEP;
    @Autowired
    PhotoService              photoService;

    @Autowired
    UserService               userService;

    @Post("upload")
    public String uploadFile(Invocation inv, HtmlPage page, @Param("photo") MultipartFile photoFile) {
        int userId = currentUserId();
        $: try {
            if (photoFile == null) {
                page.error("请选择文件");
                break $;
            }
            long size = photoFile.getSize();
            int maxSize = photoService.maxSize();
            if (maxSize > 0 && size > maxSize * BYTES_M) {
                page.error("上传的图片不能大于" + maxSize + "M");
                break $;
            }
            InputStream is = null;
            BufferedImage image = null;
            try {
                is = photoFile.getInputStream();
                image = photoService.readImage(is);
            } catch (IOException e) {
                LOG.error(e);
                page.error("读取图片失败");
                break $;
            } finally {
                IOUtils.closeQuietly(is);
            }
            if(image == null){
                page.error("这不是一个有效的图片文件");
                break $;
            }
            int width = image.getWidth();
            int height = image.getHeight();
            if (width > 3 * height || height > 3 * width) {
                page.error("图片太窄或太长，宽高比只能在1/3~3之间");
                break $;
            }
            String token = null;
            try {
                token = photoService.saveUploadedImage(userId, image);
            } catch (IOException e) {
                LOG.error(e);
                page.error("保存图片失败");
                break $;
            } finally {
                IOUtils.closeQuietly(is);
            }
            return "r:set?token=" + token;
        } catch (Exception e) {
            LOG.error(e);
            page.error("上传图片失败");
        }
        return "r:/info/my";
    }

    @Get("set")
    public String get_set(Invocation inv, HtmlPage page, @Param("token") String token) {
        int userId = currentUserId();
        $: {
        	UploadedPhotoItem item = photoService.getUploadedImageInfo(userId, token);

            if (item == null) {
                page.error("传入的参数错误");
                break $;
            }
            int width = item.width;
            int height = item.height;
            inv.addModel("width", width);
            inv.addModel("height", height);
            inv.addModel("token", token);
            inv.addModel("imageurl", item.url);

            int containerSize = PHOTO_CONTAINER_SIZE;
            int _width, _height;
            if (width > height) {
                _width = containerSize;
                _height = _width * height / width;
            } else {
                _height = containerSize;
                _width = _height * width / height;
            }
            inv.addModel("_width", _width);
            inv.addModel("_height", _height);

            item.displayWidth = _width; // sync
            item.displayHeight = _height;
            return "photos_set";
        }
        return "r:/info/my";
    }

    @Post("set")
    public String set(Invocation inv, HtmlPage page,
            @Param("token") String pToken,//
            @Param("cx") int pCx,// 选择的x偏移
            @Param("cy") int pCy,// 选择的y偏移
            @Param("cw") int pCw,// 选择的宽度
            @Param("ch") int pCh// 选择的高度
    ) {
        int userId = currentUserId();
        $: try {
            UploadedPhotoItem imageInfo = photoService.getUploadedImageInfo(userId, pToken);
            if (imageInfo == null) {
                page.error("传入参数有误");
                break $;
            }
            int displayWidth = imageInfo.displayWidth;
            int displayHeight = imageInfo.displayHeight;
            if (pCx < 0 || pCy < 0 || pCw <= 0 || pCh <= 0) {
                page.error("传入参数有误");
                break $;
            }

            if (pCx + pCw > displayWidth || pCy + pCh > displayHeight) {
                page.error("传入参数有误");
                break $;
            }

            if (pCw != pCh) {// 宽与高必须相同
                page.error("传入参数有误");
                break $;
            }

            BufferedImage image = null;
            try {
                image = photoService.loadUploadedImage(imageInfo);
            } catch (IOException e) {
                LOG.error(e);
                page.error("加载图片失败");
                break $;
            }
            if (image == null) {
                page.error("加载图片失败");
                break $;
            }
            int width = image.getWidth();
            int height = image.getHeight();

            // 下面需要抠出这块图图出来，需要注意的是，要防止溢出

            double dwScale = (double) displayWidth / width;// 显示图片的放缩比例
            double dhScale = (double) displayHeight / height;
            double dScale = Math.max(dwScale, dhScale);// 选择一个大的，防止越界

            int x = (int) (pCx / dScale);
            int y = (int) (pCy / dScale);
            int w = (int) (pCw / dScale);
            int h = (int) (pCh / dScale);
            Map<Integer/* size */, String/* url */> urls = photoService.saveScaledImages(userId, image, x, y, w, h);

            String tinyUrl = urls.get(PHOTO_SIZE_TINY);
            String mainUrl = urls.get(PHOTO_SIZE_MAIN);
            if (tinyUrl == null || mainUrl == null) {
                page.error("保存图像失败");
                break $;
            }
            userService.setPhotos(userId, tinyUrl, mainUrl);
            // 删除临时图片
            photoService.deleteUploadedImageInfo(userId, pToken);
            return "r:/info/my";
        } catch (Exception e) {
            LOG.error(e);
            page.error("上传图片失败");
        }
        return "r:/info/my";
    }
}
