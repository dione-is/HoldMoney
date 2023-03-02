package com.diotech.minhasfinancas.entity;

import lombok.Data;

@Data
public class WebHookWhatsApp {

    private String      instanceId;
    private String      status;
    private String[]    ids;
    private String      momment;
    private String      phoneDevice;
    private String      phone;
    private String      type;
    private String      isGroup;
}
