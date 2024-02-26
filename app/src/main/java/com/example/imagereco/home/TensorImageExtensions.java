package com.example.imagereco.home;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

public class TensorImageExtensions {

    public static TensorImage fromTensorBuffer(TensorBuffer buffer) {
        TensorImage image = new TensorImage(buffer.getDataType());
        image.load(buffer);
        return image;
    }

    // Add any other extension methods if needed
}

