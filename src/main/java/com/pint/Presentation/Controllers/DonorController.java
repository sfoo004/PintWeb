package com.pint.Presentation.Controllers;

import com.pint.Data.Repositories.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DonorController {
    @Autowired
    private DonorRepository donorRepository;
}
