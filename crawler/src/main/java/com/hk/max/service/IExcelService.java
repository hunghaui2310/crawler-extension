package com.hk.max.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface IExcelService {
    void export(HttpServletResponse response) throws IOException;
}
