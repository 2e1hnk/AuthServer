package cafe.deadbeef.auth_server;

import java.security.Principal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path = "/user")
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired @Lazy BCryptPasswordEncoder passwordEncoder;
	
	@Autowired UserRepository userRepository;

    @RequestMapping("/me")
    @ResponseBody
    public Principal user(Principal principal) {
        System.out.println(principal);
        return principal;
    }
    
    @GetMapping("/change-password")
    public String changePassword() {
    	return "change-password";
    }
    
    @PostMapping("/change-password")
    public RedirectView resetPassword(Authentication authentication, @RequestParam String password1, @RequestParam String password2, RedirectAttributes redir) {
    	
    	if ( !password1.equals(password2) ) {
    		redir.addFlashAttribute("error", "Passwords don't match");
    		return new RedirectView("/", true);
    	}
    	
    	User user = userRepository.findById(authentication.getName()).get();
    	
    	user.setPassword(passwordEncoder.encode(password1));
         
        userRepository.save(user);
        
        redir.addFlashAttribute("success", "Password updated");
        
        return new RedirectView("/",true);
    }
    
}