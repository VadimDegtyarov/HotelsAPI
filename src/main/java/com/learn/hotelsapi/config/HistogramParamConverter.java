package com.learn.hotelsapi.config;

import com.learn.hotelsapi.enums.HistogramParam;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HistogramParamConverter implements Converter<String, HistogramParam> {

    @Override
    public HistogramParam convert(String source) {
        return HistogramParam.valueOf(source.toUpperCase());
    }
}
