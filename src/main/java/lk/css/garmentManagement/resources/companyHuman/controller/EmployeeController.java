package lk.css.garmentManagement.resources.companyHuman.controller;

import lk.CSS.GarmentManagement.general.security.entity.User;
import lk.CSS.GarmentManagement.general.security.service.UserService;
import lk.CSS.GarmentManagement.resources.companyHuman.entity.Employee;
import lk.CSS.GarmentManagement.resources.companyHuman.entity.Enum.*;
import lk.CSS.GarmentManagement.resources.companyHuman.service.EmployeeService;
import lk.CSS.GarmentManagement.util.service.DateTimeAgeService;
import lk.CSS.GarmentManagement.util.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@RequestMapping("/employee")
@Controller
public class EmployeeController {
    private final EmployeeService employeeService;
    private final UserService userService;
    private final DateTimeAgeService dateTimeAgeService;
    private final EmailService emailService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, UserService userService, DateTimeAgeService dateTimeAgeService, EmailService emailService) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.dateTimeAgeService = dateTimeAgeService;
        this.emailService = emailService;
    }

    @RequestMapping
    public String employeePage(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "employee/employee";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String employeeView(@PathVariable("id") Long id, Model model) {
        model.addAttribute("employeeDetail", employeeService.findById(id));
        model.addAttribute("addStatus", false);
        return "employee/employee-detail";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editEmployeeFrom(@PathVariable("id") Long id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        model.addAttribute("newEmployee", employeeService.findById(id).getNumber());
        model.addAttribute("addStatus", false);
        commonMethodForEmployee(model);
        return "employee/addEmployee";
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String employeeAddFrom(Model model) {
        String newEmployeeNumber = "";
        String input;
        if (employeeService.lastEmployee() != null) {
            input = employeeService.lastEmployee().getNumber();
            int employeeNumber = Integer.valueOf(input.replaceAll("[^0-9]+", "")).intValue() + 1;

            if ((employeeNumber < 10) && (employeeNumber > 0)) {
                newEmployeeNumber = "CSS000" + employeeNumber;
            }
            if ((employeeNumber < 100) && (employeeNumber > 10)) {
                newEmployeeNumber = "CSS00" + employeeNumber;
            }
            if ((employeeNumber < 1000) && (employeeNumber > 100)) {
                newEmployeeNumber = "CSS0" + employeeNumber;
            }
            if (employeeNumber > 10000) {
                newEmployeeNumber = "CSS" + employeeNumber;
            }
        } else {
            newEmployeeNumber = "CSS0001";
            input = "CSS0000";
        }

        model.addAttribute("addStatus", true);
        model.addAttribute("lastEmployee", input);
        model.addAttribute("newEmployee", newEmployeeNumber);
        commonMethodForEmployee(model);
        model.addAttribute("employee", new Employee());
        return "employee/addEmployee";
    }

    private void commonMethodForEmployee(Model model) {
        model.addAttribute("title", Title.values());
        model.addAttribute("gender", Gender.values());
        model.addAttribute("civilStatus", CivilStatus.values());
        model.addAttribute("employeeStatus", EmployeeStatus.values());
        model.addAttribute("designation", Designation.values());
        model.addAttribute("bloodGroup", BloodGroup.values());
    }


    private boolean commonMail(Employee employee) {
        String message = "Welcome to Excellent Health Solution \n Your registration number is " + employee.getNumber() + "\nYour Details are \n " + employee.getTitle().getTitle() + " " + employee.getName() + "\n " + employee.getNic() + "\n " + employee.getDateOfBirth() + "\n " + employee.getMobile() + "\n " + employee.getLand() + "\n " + employee.getAddress() + "\n " + employee.getDateOfAssignment() + "\n\n\n\n\n Highly advice you, if there is any changes on your details, Please informed the management\n If you update your date up to date with us, otherwise we will not have to provide better serviceStation to you.\n \n \n   Thank You\n Excellent Health Solution";


        boolean isFlag = emailService.sendPatientRegistrationEmail(employee.getEmail(), "Welcome to Excellent Health Solution ", message);
        return isFlag;
    }

    @RequestMapping(value = {"/add", "/update"}, method = RequestMethod.POST)
    public String addEmployee(@Valid @ModelAttribute Employee employee, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (dateTimeAgeService.getAge(employee.getDateOfBirth()) < 18) {
            ObjectError error = new ObjectError("dateOfBirth", "Employee must be 18 old ");
            result.addError(error);
        }
        if (result.hasErrors()) {
            model.addAttribute("addStatus", true);
            if (employeeService.lastEmployee() != null) {
                model.addAttribute("lastEmployee", employeeService.lastEmployee().getNumber());
            }

            model.addAttribute("title", Title.values());
            model.addAttribute("gender", Gender.values());
            model.addAttribute("civilStatus", CivilStatus.values());
            model.addAttribute("employeeStatus", EmployeeStatus.values());
            model.addAttribute("designation", Designation.values());
            model.addAttribute("employee", employee);
            model.addAttribute("bloodGroup", BloodGroup.values());
            return "redirect:/employee/add";
        }
        if (employeeService.isEmployeePresent(employee)) {
            System.out.println("already on ");
            User user = userService.findById(userService.findByEmployeeId(employee.getId()));
            if (employee.getEmployeeStatus() != EmployeeStatus.WORKING) {
                user.setEnabled(false);
                employee.setUpdatedAt(dateTimeAgeService.getCurrentDate());
                employeeService.persist(employee);
            }
            System.out.println("update working");
            user.setEnabled(true);
            employee.setUpdatedAt(dateTimeAgeService.getCurrentDate());
            employeeService.persist(employee);
            return "redirect:/employee";
        }
        if (employee.getId() != null) {
            boolean isFlag = commonMail(employee);
            if (isFlag) {
                redirectAttributes.addFlashAttribute("message", "Successfully Update and Email was sent.");
                redirectAttributes.addFlashAttribute("alertStatus", true);
                employee.setUpdatedAt(dateTimeAgeService.getCurrentDate());
                employeeService.persist(employee);
            } else {
                redirectAttributes.addFlashAttribute("message", "Successfully Add but Email was not sent.");
                redirectAttributes.addFlashAttribute("alertStatus", false);
                employee.setUpdatedAt(dateTimeAgeService.getCurrentDate());
                employeeService.persist(employee);
            }
        }
        if (employee.getEmail() != null) {
            boolean isFlag = commonMail(employee);
            if (isFlag) {
                redirectAttributes.addFlashAttribute("message", "Successfully Update and Email was sent.");
                redirectAttributes.addFlashAttribute("alertStatus", true);
                employee.setCreatedAt(dateTimeAgeService.getCurrentDate());
                employeeService.persist(employee);
            } else {
                redirectAttributes.addFlashAttribute("message", "Successfully Add but Email was not sent.");
                redirectAttributes.addFlashAttribute("alertStatus", false);
                employee.setCreatedAt(dateTimeAgeService.getCurrentDate());
                employeeService.persist(employee);
            }
        }
        System.out.println("save no id");
        employee.setCreatedAt(dateTimeAgeService.getCurrentDate());
        employeeService.persist(employee);
        return "redirect:/employee";
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
    public String removeEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return "redirect:/employee";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model, Employee employee) {
        model.addAttribute("employeeDetail", employeeService.search(employee));
        return "employee/employee-detail";
    }
}