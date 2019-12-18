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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path = "/admin")
public class ClientAdminController {
	
	@Autowired ClientRepository clientRepository;
	
	@Autowired @Lazy BCryptPasswordEncoder passwordEncoder;
	
	/*
	 * JSON methods
	 */
    @GetMapping("/clients.json")
    @ResponseBody
    public List<Client> getClients() {
    	return clientRepository.findAll();
        //return employees.stream().filter(x -> x.getEmail().equals(email)).findAny();
    }

    @GetMapping("/client/{client_id}.json")
    @ResponseBody
    public Client getClient(@PathVariable Client client_id) {
    	return client_id;
        //return employees.stream().filter(x -> x.getEmail().equals(email)).findAny();
    }
    
    /*
     * UI methods
     */
    @GetMapping("/client")
    public String showClientList(Model model) {
    	model.addAttribute("clients", clientRepository.findAll());
        return "list-client";
    }
     
    @GetMapping("/new-client")
    public String showNewClient(Client client, Model model) {
        return "new-client";
    }
     
    @GetMapping("/client/{client_id}")
    public String showClient(@PathVariable Client client_id, Model model) {
    	model.addAttribute("client", client_id);
        return "client";
    }
     
    @GetMapping("/client/{client_id}/edit")
    public String showEditClient(@PathVariable Client client_id, Model model) {
    	model.addAttribute("client", client_id);
        return "edit-client";
    }
     
    @PostMapping("/client/{client_id}/update")
    public String updateClient(@PathVariable String client_id, Client client, Model model, BindingResult result) {
    	Optional<Client> clientOptional = clientRepository.findById(client_id);

    	if (!clientOptional.isPresent()) {
    		result.rejectValue("client_id", "error.client", "Could not find client " + client_id);
    	} else {
    		client.setClient_id(client_id);
    		clientRepository.save(client);
    	}
    	
        return "redirect:/admin/client";
    }
    
    @GetMapping("/client/{id}/delete")
    public String deleteClient(@PathVariable Client id, Model model) {
    	clientRepository.delete(id);
    	
        return "redirect:/admin/client";
    }
     
    @PostMapping("/client")
    public RedirectView addClient(@ModelAttribute Client client, BindingResult result, RedirectAttributes redir) {
        if (result.hasErrors()) {
        	return new RedirectView("/admin/new-client",true);
        }
        
        // NOTE: using alphanumeric rather than ascii to avoid potential issues
        // with special characters. It is quite long though!
        String client_secret = RandomStringUtils.randomAlphanumeric(40);
        
        client.setClient_secret(passwordEncoder.encode(client_secret));
         
        clientRepository.save(client);
        
        redir.addFlashAttribute("client_secret", client_secret);
        
        return new RedirectView("/admin/client",true);
    }
    
}
