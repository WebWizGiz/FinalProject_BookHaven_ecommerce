package org.dci.bookhaven.controller;

import org.dci.bookhaven.model.Address;
import org.dci.bookhaven.model.User;
import org.dci.bookhaven.model.UserProfile;
import org.dci.bookhaven.repository.AddressRepository;
import org.dci.bookhaven.repository.UserRepository;
import org.dci.bookhaven.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class userProfileController {
    //Controller class for user profile
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    // GET method to display the user's profile
    @GetMapping("/view/{id}")
    public String viewProfile(Model model, @PathVariable Long id) {
        UserProfile userProfile = userProfileService.getUserProfileByUserId(id);

        // Add the userProfile object to the model so it can be accessed in the Thymeleaf template
        model.addAttribute("userProfile", userProfile);
        return "viewProfile";
    }

 /*   @GetMapping
    public String userProfileHome(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserProfile profile = userProfileService.getUserProfileByUserId(userDetails.getUserId());
        model.addAttribute("user", profile.getUser());
        model.addAttribute("profile", profile);
        return "userProfile";
    }*/

    @GetMapping()
    public String userProfile(Principal principal, Model model) {
        System.out.println("HELLO USER ");
        String loggedInUserEmail = principal.getName();
        User user = userRepository.findByEmail(loggedInUserEmail);
        System.out.println(user);
        model.addAttribute("user", user);

        /*User user = (User) principal;
        System.out.println(user);*/
        return "userProfile";
    }

    /* UserProfile userProfile = userProfileService.getUserProfileByUserId(id);
        model.addAttribute("userProfile", userProfile);*/

    // GET method to display the form to complete user's profile information
    @GetMapping("/complete/{id}")
    public String showCompleteProfileForm(Model model, @PathVariable Long id) {
        UserProfile userProfile = userProfileService.getUserProfileByUserId(id);
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("address", new Address());
        return "completeProfile";
    }

    // POST method to handle form submission and save the profile details
    @PostMapping("/complete")
    public String completeProfile(@RequestParam Long id,
                                  @RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                  @RequestParam String gender,
                                  @ModelAttribute Address address) {
        // Update user profile with the provided information
        userProfileService.updateProfile(id, firstName, lastName, dateOfBirth, gender);

        // Add address to the user profile
        userProfileService.addAddress(id, address);

        return "redirect:/profile/view?id=" + id;
    }


    // GET method to show the update form
    @GetMapping("/edit/{id}")
    public String showUpdateForm(Model model, @PathVariable Long id) {
        UserProfile userProfile = userProfileService.getUserProfileByUserId(id);
        model.addAttribute("userProfile", userProfile);
        System.out.println(userProfile);
        return "editProfile";
    }

    // POST method to handle form submission and update the profile
    @PostMapping("/update")
    public String updateProfile(@RequestParam Long id,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                @RequestParam String gender,
                                Model model) {
        userProfileService.updateProfile(id, firstName, lastName, dateOfBirth, gender);
        model.addAttribute("message", "Profile updated successfully!");
        System.out.println("ID: " + id);
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        return "redirect:/profile/view/" + id;
    }

    // Managing Addresses
    @GetMapping("/addresses")
    public String viewAddresses(@RequestParam Long id, Model model) {
        List<Address> addresses = userProfileService.getAddresses(id);
        model.addAttribute("addresses", addresses);
        model.addAttribute("userId", id);
        return "viewAddresses";
    }

    @GetMapping("/addresses/add")
    public String showAddAddressForm(@RequestParam Long id, Model model) {
        model.addAttribute("address", new Address());
        model.addAttribute("userId", id);
        return "addAddress";
    }

    @PostMapping("/addresses/add")
    public String addAddress(@RequestParam Long id, @ModelAttribute Address address) {
        userProfileService.addAddress(id, address);
        return "redirect:/profile/addresses?id=" + id;
    }

    @GetMapping("/addresses/edit")
    public String showEditAddressForm(@RequestParam Long id, Model model) {
        Address address = addressRepository.findById(id).orElse(null);
        model.addAttribute("address", address);
        model.addAttribute("userId", address.getUserProfile().getId());
        return "editAddress";
    }

    @PostMapping("/addresses/edit")
    public String updateAddress(@RequestParam Long id, @ModelAttribute Address address) {
        userProfileService.updateAddress(id, address);
        return "redirect:/profile/addresses?id=" + address.getUserProfile().getId();
    }

    @PostMapping("/addresses/delete")
    public String deleteAddress(@RequestParam Long id) {
        userProfileService.deleteAddress(id);
        return "redirect:/profile/addresses?id=" + id;
    }

}
