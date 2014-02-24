package com.orientalcomics.profile.biz.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

import com.orientalcomics.profile.biz.model.EmailText;


@DAO
public interface EmailTextDAO {
	
    String TABLE          = "email_text";
    
    // -------- { Column Defines
    
    String ID             = "id";
    String EMAIL_TYPE     = "email_type";
    String EMAIL_TITLE    = "email_title";
    String EMAIL_CONTENT  = "email_content";

    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "email_type,email_title,email_content";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;
    
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `email_type` = :1")
    public EmailText query(String emailType);
    
}
