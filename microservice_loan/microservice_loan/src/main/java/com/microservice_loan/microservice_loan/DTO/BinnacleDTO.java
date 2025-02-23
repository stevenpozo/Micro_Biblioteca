package com.microservice_loan.microservice_loan.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class BinnacleDTO {

    private String codeBook;
    private String author;
    private String grade;
    private String title;
    private String language;
    private String section;
    private String user_name;
    private String user_last_name;
    private String mail;
    private String role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date acquisition_date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date_of_devolution;
    private boolean confirm_devolution;
}

