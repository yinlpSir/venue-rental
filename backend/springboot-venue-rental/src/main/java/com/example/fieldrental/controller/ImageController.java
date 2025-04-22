package com.example.fieldrental.controller;


import com.example.fieldrental.entity.Image;
import com.example.fieldrental.handler.HasAnyRole;
import com.example.fieldrental.service.ImageService;
import com.example.fieldrental.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/image")
@ResponseBody
@Tag(name = "图片管理")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @HasAnyRole(roles = {"USER","ADMIN"})
    @PostMapping("/upload")
    public String createImage(@RequestParam("file") MultipartFile file) throws IOException
    {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (file.getContentType().equals("image/jpg") || file.getContentType().equals("image/png")
                    || file.getContentType().equals("image/jpeg") && userService.getUserByUsername(username) != null)
                return imageService.createImage(file, userService.getUserByUsername(username).getId());
            else
                throw new IllegalArgumentException("file type error");
        }
        throw new IllegalArgumentException("you can't upload img in your not login status ");
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @PostMapping("/")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException
    {
        if(file.getContentType().equals("image/jpg") || file.getContentType().equals("image/png")|| file.getContentType().equals("image/jpeg"))
            return imageService.addImage(file);
        else
            throw new IllegalArgumentException("file type error") ;
    }

    @Operation(summary = "上传头像（包含更新）")
    @HasAnyRole(roles = {"USER","ADMIN"})
    @PostMapping("/uploadUserImg")
    public boolean uploadUserImg(@RequestParam("file") MultipartFile file) throws IOException {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (file.getContentType().equals("image/jpg") || file.getContentType().equals("image/png")
                    || file.getContentType().equals("image/jpeg") && userService.getUserByUsername(username) != null) {
                String image = imageService.createImage(file, userService.getUserByUsername(username).getId());
                userService.uploadImg(username, image);
                return true;
            } else
                throw new IllegalArgumentException("file type error");
        }
        throw new IllegalArgumentException("you can't upload img in your not login status ");
    }

    @GetMapping("/{imageId}")
    public String getImage(@PathVariable String imageId) throws IOException {
        if(imageId==null || imageId.equals("") || imageId.equals("null"))
            return "null";
        return imageService.getImage(imageId);
    }

    @GetMapping("/get/{imageId}")
    public String getImage2(@PathVariable String imageId) throws IOException {
        if(imageId==null || imageId.equals("") || imageId.equals("null"))
            return "null";
        return "<img src='"+ imageService.getImage(imageId)+"'/>";
    }


    @HasAnyRole(roles = {"USER","ADMIN"})
    @PostMapping("/del/{imageId}")
    public void deleteImage(@PathVariable String imageId) {
         imageService.removeImage(imageId);
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @GetMapping("/getUserImage")
    public Image getUserImage() throws IOException {
        long id = 1L ;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            id = userService.getUserByUsername(authentication.getName()).getId();
        }
        else {
            throw new IllegalArgumentException("the Authentication has the illegal controller") ;
        }
        return imageService.getImageByUserId(id) ;
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @GetMapping("/getUserImageAll")
    public List<Image> getUserImage2() throws IOException {
        long id = 1L ;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()&& !authentication.getName().equals("anonymousUser")) {
            id = userService.getUserByUsername(authentication.getName()).getId();
        }
        else {
            throw new IllegalArgumentException("the Authentication has the illegal controller") ;
        }
        return imageService.getImageByUserIdAll(id) ;
    }
}
