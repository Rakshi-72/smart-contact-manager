package com.smart_conatct_managet.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.smart_conatct_managet.entities.User;
import com.smart_conatct_managet.help.EmailSender;
import com.smart_conatct_managet.help.MyMessage;
import com.smart_conatct_managet.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /* Home Page */
    /**
     * This function is called when the user navigates to the root of the
     * application. It returns the
     * name of the view to be rendered
     * 
     * @param model This is the model object that is used to pass data from the
     *              controller to the view.
     * @return The name of the view to be rendered.
     */
    @GetMapping("/")
    public String homeHandlerString(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "contact_manager";
    }

    /* forwarding to signup page */
    /**
     * This function is called when the user navigates to the /signup page. It adds
     * a title to the
     * model and a new user object to the model. It then returns the signup page
     * 
     * @param model This is the model object that is used to pass data from the
     *              controller to the view.
     * @return The signup page.
     */
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("title", "signup - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    /* sign up page */
    /**
     * It takes a user object, validates it, and if it's valid, it saves it to the
     * database
     * 
     * @param user          The user object that will be used to store the user's
     *                      information.
     * @param bindingResult This is the object that holds the validation results.
     * @param accept        This is a boolean value that is set to false by default.
     * @param model         The model is a Map of object names and objects that will
     *                      be passed to the view.
     * @param session       the HttpSession object
     * @return A String
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
            @RequestParam(value = "accept", defaultValue = "false") boolean accept, Model model,
            HttpSession session) {

        try {
            if (!accept) {
                throw new Exception("please accept the terms and conditions");
            } else if (bindingResult.hasErrors()) {
                model.addAttribute("user", user);
                session.setAttribute("message",
                        new MyMessage("something went wrong reason: ", "alert-danger"));
                return "signup";
            }
            user.setEnabled(true);
            user.setRole("ROLE_USER");
            user.setImageUrl("user.png");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            System.out.println(user);
            model.addAttribute("user", new User());

            repository.save(user);
            session.setAttribute("message",
                    new MyMessage("you have successfully registered thank you...!", "alert-success"));
            return "signin";
        } catch (Exception e) {

            model.addAttribute("user", user);
            session.setAttribute("message",
                    new MyMessage("Oops something went wrong: " + e.getMessage(), "alert-danger"));
            return "signup";
        }
    }

    /**
     * It returns the login page
     * 
     * @return The login.html page
     */
    @GetMapping("/signin")
    public String loginHandler(Model model) {
        model.addAttribute("title", "Sign - in");
        return "signin";
    }

    /**
     * The function contactUs() returns the string "contact" which is the name of
     * the html file that
     * will be rendered
     * 
     * @return The contact.html page
     */
    @GetMapping("/contact")
    public String contactUs() {
        return "contact";
    }

    /**
     * This function is used to render the forgot_password.html page
     * 
     * @return A String
     */
    @Autowired
    private EmailSender emailSender;

    @GetMapping("/forgot")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "forgot-password");
        return "forgot_password";
    }

    private static String OTP;

    /**
     * It takes the email as a parameter and sends an OTP to the email.
     * 
     * @param email   the email address of the user
     * @param session The session object is used to store the OTP value.
     * @return A String
     */
    @PostMapping("/forgot/change")
    public String changePassword(@RequestParam("email") String email, HttpSession session, Model model) {

        User user = repository.getUserByName(email);
        if (user != null) {
            OTP = emailSender.sendEmail(email);
            if (OTP != null) {
                session.setAttribute("message", new MyMessage("Otp sent successfully", "alert-success"));
                session.setAttribute("email", email);
                return "enter_otp";
            } else {
                session.setAttribute("message",
                        new MyMessage("something went wrong plaese try again after some time", "alert-danger"));
                model.addAttribute("email", email);
                return "forgot_password";
            }
        }
        session.setAttribute("message", new MyMessage("invalid email", "alert-danger"));
        return "forgot_password";
    }

    /**
     * If the OTP entered by the user matches the OTP sent to the user's email, then
     * the user is
     * redirected to the reset_password page
     * 
     * @param Otp     The OTP that the user has entered.
     * @param session The session object is used to store the email of the user.
     * @param email   The email address of the user.
     * @return A String
     */
    @PostMapping("/validate")
    public String validateOtp(@RequestParam("Otp") String Otp, HttpSession session,
            @RequestParam("email") String email) {

        if (OTP.equals(Otp)) {
            session.setAttribute("email", email);
            return "reset_password";
        }
        session.setAttribute("message", new MyMessage("Invalid OTP", "alert-danger"));
        return "forgot_password";
    }

    /**
     * The function takes in the email, new password and confirm password from the
     * user and checks if
     * the new password and confirm password are the same. If they are the same, the
     * password is
     * changed and the user is redirected to the sign in page. If they are not the
     * same, the user is
     * redirected to the forgot password page with an error message
     * 
     * @param email           the email address of the user
     * @param newPAssword     the new password
     * @param confirmPassword The password that the user entered in the confirm
     *                        password field.
     * @param session         the session object
     * @return A String
     */
    @PostMapping("/change")
    public String changePassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPAssword,
            @RequestParam("confirmPassword") String confirmPassword, HttpSession session) {

        if (newPAssword.equals(confirmPassword)) {
            User user = repository.getUserByName(email);
            user.setPassword(bCryptPasswordEncoder.encode(confirmPassword));
            repository.save(user);

            session.setAttribute("message", new MyMessage("password recovered successfully sign in", "alert-success"));
            return "signin";
        }
        session.setAttribute("message", new MyMessage("new password and confirm password miss match", "alert-danger"));
        return "forgot_password";
    }

}
