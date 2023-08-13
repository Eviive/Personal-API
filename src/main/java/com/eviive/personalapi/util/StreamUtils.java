package com.eviive.personalapi.util;

import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class StreamUtils {

    private static final DataSize DEFAULT_BUFFER_SIZE = DataSize.ofMegabytes(4);

    public void transferTo(InputStream inputStream, OutputStream outputStream, DataSize bufferSize) throws IOException {
        int nextByte;
        long position = 0;
        while ((nextByte = inputStream.read()) != -1) {
            outputStream.write(nextByte);
            if (++position % bufferSize.toBytes() == 0) {
                outputStream.flush();
            }
        }
        if (position % bufferSize.toBytes() != 0) {
            outputStream.flush();
        }
    }

    public void transferTo(InputStream inputStream, OutputStream outputStream) throws IOException {
        transferTo(inputStream, outputStream, DEFAULT_BUFFER_SIZE);
    }

}
