/**
 * It's a controller class that handles all the requests that are related to the user
 */
package com.smart_conatct_managet.controllers;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.smart_conatct_managet.entities.Contact;
import com.smart_conatct_managet.entities.User;
import com.smart_conatct_managet.help.MyMessage;
import com.smart_conatct_managet.help.UploadImg;
import com.smart_conatct_managet.repository.ContactRepository;
import com.smart_conatct_managet.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

    // Injecting the repository and contRepository into the controller.
    @Autowired
    private UserRepository repository;

    @Autowired
    private ContactRepository contRepository;

    @Autowired
    private UploadImg uploadImg;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * This function is called before every request to the server. It gets the name
     * of the user that is
     * logged in and adds it to the model
     * 
     * @param model     This is the model object that is used to pass data to the
     *                  view.
     * @param principal This is the object that contains the information about the
     *                  user that is logged
     *                  in.
     */
    @ModelAttribute
    public void getCommanData(Model model, Principal principal) {
        // Getting the name of the user that is logged in.
        String userName = principal.getName();

        User user = repository.getUserByName(userName);

        model.addAttribute("user", user);
    }

    /**
     * It gets the name of the user that is logged in
     * 
     * @param model     This is the model object that is used to pass data to the
     *                  view.
     * @param principal This is the object that contains the information about the
     *                  user that is logged
     *                  in.
     * @return The name of the user that is logged in.
     */
    @GetMapping("/")
    public String loginHandler(Model model, Principal principal) {

        model.addAttribute("title", "User - Home");
        return "normal/index";
    }

    /**
     * It returns the index page of the normal user
     * 
     * @param model     This is the model object that is used to pass data to the
     *                  view.
     * @param principal The principal object is the user object that is currently
     *                  logged in.
     * @return A String
     */
    @GetMapping("/index")
    public String loginIndexHandler(Model model, Principal principal) {

        model.addAttribute("title", "User - Home");
        return "normal/index";
    }

    /**
     * This function is used to add a contact to the database
     * 
     * @param model This is the model object that is used to pass data from the
     *              controller to the view.
     * @return The addcontact.html page
     */
    @GetMapping("/add-contact")
    public String addContact(Model model) {

        model.addAttribute("title", "Add contacts");
        model.addAttribute("contact", new Contact());

        return "normal/addcontact";
    }

    /**
     * It takes a contact object, a binding result object, a multipart file object,
     * a principal object,
     * a session object and a model object as parameters. It checks if there are any
     * errors in the form.
     * If there are, it sets a message to the session object. It gets the name of
     * the user that is
     * logged in, gets the user object from the database, adds the contact to the
     * user's list of
     * contacts and sets the user property of the contact object to the user object.
     * It checks if the
     * profileImage is empty. If it is, it saves the user to the database and
     * returns the
     * addcontact.html page. If it is not, it checks if the file is an image/jpeg
     * file. If it is not, it
     * throws an IllegalArgumentException. If it is, it checks if the image is
     * uploaded successfully. If
     * it is, it sets the imageUrl property of the contact object to the name of the
     * image and saves the
     * user to the database.
     * 
     * @param contact      The contact object that is created from the form.
     * @param result       The BindingResult object that holds the result of the
     *                     validation and binding and
     *                     contains errors that may have occurred.
     * @param profileImage the image that the user uploads.
     * @param principal    This is the object that contains the name of the user
     *                     that is logged in.
     * @param session      The session object.
     * @param model        The model object is used to pass data from the controller
     *                     to the view.
     * @return The addcontact.html page.
     */
    @PostMapping("/add")
    public String processContact(@Valid @ModelAttribute Contact contact, BindingResult result,
            @RequestParam("profileImage") MultipartFile profileImage, Principal principal, HttpSession session,
            Model model) {

        model.addAttribute("title", "Add Contact");
        // Checking if there are any errors in the form. If there are, it sets a message
        // to the session object.
        if (result.hasErrors()) {
            session.setAttribute("message", new MyMessage("something went wrong :", "alert-danger"));
            return "normal/addcontact";
        }

        // Getting the name of the user that is logged in, getting the user object from
        // the
        // database, adding the contact to the user's list of contacts and setting the
        // user
        // property of the contact object to the user object.
        String userName = principal.getName();
        User user = repository.getUserByName(userName);
        user.getContacts().add(contact);
        contact.setUser(user);

        // Checking if the profileImage is empty. If it is, it saves the user to the
        // database and returns the addcontact.html page.
        if (profileImage.isEmpty()) {
            contact.setImageUrl("contact.png");
            repository.save(user);
            session.setAttribute("message",
                    new MyMessage("contact added successfully (profile picture is missing)", "alert-success"));
            return "normal/addcontact";
        }

        else {
            // Checking if the file is an image/jpeg file. If it is not, it throws an
            // IllegalArgumentException.
            if (!profileImage.getContentType().equals("image/jpeg")) {
                session.setAttribute("message", new MyMessage("image shoud be of jpg/jpeg format", "alert-danger"));
                return "normal/addcontact";
            }
            // Checking if the image is uploaded successfully. If it is, it sets the
            // imageUrl
            // property of the contact object to the name of the image and saves the user to
            // the database.
            else {
                try {

                    boolean isUploaded = uploadImg.processImage(profileImage);
                    if (isUploaded) {
                        contact.setImageUrl(profileImage.getOriginalFilename());
                        repository.save(user);
                        session.setAttribute("message", new MyMessage("contact added successfully ", "alert-success"));
                        return "normal/addcontact";
                    }

                    // Catching any exception that might occur and setting a message to the session
                    // object.
                } catch (Exception e) {
                    session.setAttribute("message", new MyMessage(e.getMessage(), "alert-danger"));
                    return "normal/addcontact";
                }

            }

            return "normal/addcontact";
        }
    }

    /**
     * It gets the contacts from the database and displays them on the page
     * 
     * @param page      the page number
     * @param model     The model is a Map of model objects, which can be used to
     *                  pass data to the view.
     * @param principal The principal object is used to get the currently logged in
     *                  user.
     * @return A list of contacts
     */
    @GetMapping("/view/{page}")
    public String viewContactForm(@PathVariable("page") Integer page, Model model, Principal principal) {

        model.addAttribute("title", "View Contacts");

        User user = repository.getUserByName(principal.getName());

        Pageable pageable = PageRequest.of(page, 5);

        Page<Contact> contacts = contRepository.findContactByUser(user, pageable);
        model.addAttribute("contacts", contacts);
        model.addAttribute("currentpage", page);
        model.addAttribute("totalpages", contacts.getTotalPages());
        return "normal/viewcontact";
    }

    /**
     * If the user is the owner of the contact, then show the contact profile.
     * 
     * @param cId       the id of the contact
     * @param model     The model is a Map that is used to store the data that will
     *                  be displayed on the view
     *                  page.
     * @param principal the user who is logged in
     * @return A contact object
     */
    @GetMapping("/profile/{cId}")
    public String showContactProfile(@PathVariable("cId") Integer cId, Model model, Principal principal) {

        Optional<Contact> contactOp = contRepository.findById(cId);
        Contact contact = contactOp.get();
        String userName = principal.getName();

        User user = repository.getUserByName(userName);

        if (user.getId() == contact.getUser().getId()) {
            model.addAttribute("title", "contact - profile");
            model.addAttribute("contact", contact);
            return "normal/profile";
        }

        else {
            model.addAttribute("title", "Un-Authorized");
            return "normal/profile";
        }

    }

    /**
     * It deletes a contact from the database and deletes the image associated with
     * the contact from the
     * server.
     * </code>
     * 
     * @param cId     the id of the contact to be deleted
     * @param session HttpSession
     * @return A String
     */
    @GetMapping("/delete/{cId}")
    public String deleteContact(@PathVariable("cId") Integer cId, HttpSession session) throws IOException {

        File file;

        Contact contact = contRepository.findById(cId).get();

        String path = new ClassPathResource("/static/images/").getFile().getAbsolutePath() + File.separator
                + contact.getImageUrl();

        file = new File(path);

        file.delete();

        contRepository.deleteById(cId);

        session.setAttribute("message", new MyMessage("contact deleted successfully", "alert-success"));

        return "redirect:/user/view/0";

    }

    /**
     * It takes the id of the contact you want to update, finds it in the database,
     * and then sends it
     * to the update page
     * 
     * @param cId   The id of the contact to be updated
     * @param model This is the model object that is used to pass data from the
     *              controller to the view.
     * @return The update page is being returned.
     */
    @GetMapping("/update/{cId}")
    public String updateContact(@PathVariable("cId") Integer cId, Model model) {

        Optional<Contact> contacts = contRepository.findById(cId);

        Contact contact = contacts.get();

        model.addAttribute("contact", contact);

        return "normal/update";

    }

    /**
     * It takes a contact object, a session object, a principal object, and a
     * multipart file object,
     * and then it updates the contact object to the database.
     * 
     * @param contact   is the object that is being updated
     * @param session   HttpSession
     * @param principal The principal object is used to get the currently logged in
     *                  user.
     * @param file      the file that is uploaded
     * @return A String
     */
    @PostMapping("/update/done")
    public String updateDone(@ModelAttribute Contact contact, HttpSession session, Principal principal,
            @RequestParam("profileImage") MultipartFile file) {

        User user = repository.getUserByName(principal.getName());

        if (file.isEmpty()) {
            contact.setImageUrl("contact.png");
        } else {
            boolean isUploaded = uploadImg.processImage(file);
            if (isUploaded) {
                contact.setImageUrl(file.getOriginalFilename());
            }
        }

        contact.setUser(user);
        contRepository.save(contact);

        session.setAttribute("message", new MyMessage("contact updated successfully", "alert-success"));

        return "redirect:/user/view/0";
    }

    /**
     * It gets the user's profile information from the database and displays it on
     * the userprofile.html
     * page
     * 
     * @param model     The model is a Map of model objects, which can be used to
     *                  pass data to the view.
     * @param principal The principal object represents the currently logged in
     *                  user.
     * @return The user profile page.
     */
    @GetMapping(value = "/userprofile")
    public String userProfile(Model model, Principal principal) {

        model.addAttribute("title", "User - profile");
        User user = repository.getUserByName(principal.getName());

        model.addAttribute("user", user);
        return "normal/userprofile";
    }

    /**
     * It returns a string that is the name of the html file that is to be rendered
     * 
     * @param model This is the model object that is used to pass data from the
     *              controller to the view.
     * @return A string
     */
    @GetMapping("/changepassword")
    public String changePassword(Model model) {

        model.addAttribute("title", "change password");
        return "normal/changepassword";
    }

    /**
     * It takes the old password, new password, and confirm password from the user,
     * and if the old
     * password matches the password in the database, it will change the password to
     * the new password
     * 
     * @param oldPassword     The old password of the user
     * @param newPassword     the new password
     * @param confirmPassword the password the user entered in the confirm password
     *                        field
     * @param principal       The principal object contains information about the
     *                        currently logged in user.
     * @param session         This is the session object that is created when the
     *                        user logs in.
     * @return A redirect to the logout page.
     */
    @PostMapping("/change")
    public String change(@RequestParam("old_password") String oldPassword,
            @RequestParam("new_password") String newPassword, @RequestParam("confirm_password") String confirmPassword,
            Principal principal, HttpSession session) {

        User user = repository.getUserByName(principal.getName());

        if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            // Checking if the new password is the same as the confirm password. If it is,
            // it will set
            // the password to the confirm password.
            if (newPassword.equals(confirmPassword)) {
                user.setPassword(bCryptPasswordEncoder.encode(confirmPassword));
                repository.save(user);

                session.setAttribute("message",
                        new MyMessage("password changed successfully", "alert-success"));


            } else {
                session.setAttribute("message",
                        new MyMessage("new password and confirm password missmatch", "alert-danger"));
                return "normal/changepassword";
            }
        } else {
            session.setAttribute("message",
                    new MyMessage("password missmatch enter a valid old password", "alert-danger"));

            return "normal/changepassword";
        }

        return "redirect:/logout";
    }

}
