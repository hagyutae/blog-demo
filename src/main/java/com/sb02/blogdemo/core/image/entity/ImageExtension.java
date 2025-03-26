package com.sb02.blogdemo.core.image.entity;

public enum ImageExtension {
    JPG(".jpg"),
    JPEG(".jpeg"),
    PNG(".png"),
    GIF(".gif");

    private final String extension;

    ImageExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static boolean isAllowedExtension(String filename) {
        if (filename == null) return false;
        filename = filename.toLowerCase();

        for (ImageExtension ext : values()) {
            if (filename.endsWith(ext.getExtension())) {
                return true;
            }
        }
        return false;
    }

    public static String[] getAllExtensions() {
        ImageExtension[] values = values();
        String[] extensions = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            extensions[i] = values[i].getExtension();
        }

        return extensions;
    }
}
