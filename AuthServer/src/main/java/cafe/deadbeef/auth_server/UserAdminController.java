package cafe.deadbeef.auth_server;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/admin")
public class UserAdminController {
	
	@Autowired UserRepository userRepository;
	
	@Autowired @Lazy BCryptPasswordEncoder passwordEncoder;
	
	/*
	 * JSON methods
	 */
    @GetMapping("/users.json")
    @ResponseBody
    public List<User> getClients() {
    	return userRepository.findAll();
        //return employees.stream().filter(x -> x.getEmail().equals(email)).findAny();
    }

    @GetMapping("/user/{user}.json")
    @ResponseBody
    public User getUser(@PathVariable User user) {
    	return user;
        //return employees.stream().filter(x -> x.getEmail().equals(email)).findAny();
    }
    
    /*
     * UI methods
     */
    @GetMapping("/user")
    public String showUserList(Model model) {
    	model.addAttribute("users", userRepository.findAll());
        return "list-user";
    }
     
    @GetMapping("/new-user")
    public String showNewUser(User user, Model model) {
        return "new-user";
    }
     
    @GetMapping("/user/{user}")
    public String showUser(@PathVariable User user, Model model) {
    	model.addAttribute("user", user);
        return "user";
    }
     
    @GetMapping("/user/{user}/edit")
    public String showEditUser(@PathVariable User user, Model model) {
    	model.addAttribute("user", user);
        return "edit-user";
    }
     
    @PostMapping("/user/{username}/update")
    public String updateUser(@PathVariable String username, User user, Model model, BindingResult result) {
    	Optional<User> userOptional = userRepository.findById(username);

    	if (!userOptional.isPresent()) {
    		result.rejectValue("username", "error.user", "Could not find user " + username);
    	} else {
    		user.setUsername(username);
    		userRepository.save(user);
    	}
    	
        return "redirect:/admin/user";
    }
    
    @GetMapping("/user/{user}/delete")
    public String deleteUser(@PathVariable User user, Model model) {
    	userRepository.delete(user);
    	
        return "redirect:/admin/user";
    }
     
    @PostMapping("/user")
    public String addUser(@ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "new-user";
        }
        
        String password = RandomStringUtils.randomAlphabetic(20);
        
        user.setPassword(passwordEncoder.encode(password));
         
        userRepository.save(user);
        model.addAttribute("password", password);
        
        return "redirect:/admin/user";
    }
    
}
