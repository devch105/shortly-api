package com.devch105.shortly.service;

public interface QRCodeService {

    byte[] generateQRCode(String url, int width, int height);

}