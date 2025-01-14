package com.pixeltalk.service;

import com.pixeltalk.dto.EmployeeLoginDTO;
import com.pixeltalk.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
