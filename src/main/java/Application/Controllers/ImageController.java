package Application.Controllers;

import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
@Controller
public class ImageController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/avatar/{token}", method = RequestMethod.POST)
    @ResponseBody
    public String setAvatar(@PathVariable("token") String token, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                throw new MissingRequestHeaderException("Authorization", null);
            }
            String jwt = header.substring(7);

            if (JwtProvider.validateToken(jwt)) {
                StringBuilder data = new StringBuilder();
                String line;
                while ((line = request.getReader().readLine()) != null) {
                    data.append(line);
                }

                JSONObject receivedDataJson = new JSONObject(data.toString());
                JSONArray jArray = receivedDataJson.getJSONArray("avatar");
                byte[] avatar = new byte[jArray.length()];
                for (int i = 0; i < jArray.length(); i++) {
                    avatar[i] = (byte) jArray.getInt(i);
                }
                // Потом может убрать
                avatar = Base64.encodeBase64(avatar);

                User user = userService.findUserByToken(token);

                userService.updateAvatar(user, avatar);

                responseJson.put("status", "success");
            } else {
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            responseJson.put("status", "incorrect request headers");
        } catch (UsernameNotFoundException e) {
            responseJson.put("status", "user not found");
        } catch (JSONException e) {
            responseJson.put("status", "incorrect request body");
        }catch (IOException e) {
            responseJson.put("status", "incorrect byte sequence");
        } catch (Exception e) {
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }
}