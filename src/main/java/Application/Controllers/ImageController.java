package Application.Controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ImageController {

    @RequestMapping(method = RequestMethod.POST)
    public String importImage(@RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        byte[] bytes = Base64.encodeBase64(multipartFile.getBytes());

        model.addAttribute("bytes", new String(bytes));

        return "image";
    }
}
