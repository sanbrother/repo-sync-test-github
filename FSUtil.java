package com.sanbrother.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipFile;

/**
 * @brief File System Utility class
 * 
 * @author David 2014 0223
 *
 */
public class FSUtil {
    private final static int BUFFER_SIZE = 1024;
    private final static String LINE_SEPARATOR = "\n";

    private FSUtil() {
        /* defeat instantiation */
    }
    
	public static String readTextFile(String filePath) {
		InputStream is = null;

		try {
			is = new FileInputStream(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignored) {
				}
			}
		}

		return FSUtil.readTextFile(is);
	}
	
	public static String readTextFile(InputStream is) {
		String contents = "";
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(is));
			contents = reader.readLine();
			String line = null;

			while ((line = reader.readLine()) != null) {
				contents += FSUtil.LINE_SEPARATOR + line;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignored) {
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ignored) {
				}
			}
		}

		return contents;
	}

    public static void copy(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        
        if (source == null || dest == null || !source.isFile() || !source.exists()) {
            return;
        }
        
        prepareParentDirs(dest);
        
        is = new FileInputStream(source);
        os = new FileOutputStream(dest);

        streamCopy(is, os, true);
    }
    
    public static void streamCopy(InputStream is, OutputStream os, boolean closeStream) throws IOException {
        int length;
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            if (closeStream) {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            }
        }
    }
    
    public static OutputStream getOutputStream(String destPath) throws FileNotFoundException {
        File destFile = new File(destPath);
        
        prepareParentDirs(destFile);
        
        return new FileOutputStream(destFile);
    }
    
    public static boolean isValidZipFile(final String filePath) {
        return isValidZipFile(new File(filePath));
    }

    public static boolean isValidZipFile(final File file) {
        ZipFile zipfile = null;

        try {
            zipfile = new ZipFile(file);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (zipfile != null) {
                    zipfile.close();
                    zipfile = null;
                }
            } catch (IOException e) {
            }
        }
    }

    public static void prepareParentDirs(File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }
}


