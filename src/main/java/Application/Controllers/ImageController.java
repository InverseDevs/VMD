package Application.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {

    @RequestMapping(method = RequestMethod.POST)
    public String importImage(@RequestParam("image") MultipartFile multipartFile) {
        System.out.println(multipartFile.getName());
        System.out.println(multipartFile.getOriginalFilename());
        System.out.println(multipartFile.getContentType());

        return "home";
    }
}
