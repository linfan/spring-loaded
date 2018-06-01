package org.springsource.loaded.remote;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.springsource.loaded.SpringLoaded;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClassSwapper implements HttpHandler {

    private static final String ERROR_MISSING_PATH_PARAMETER = "Missing \"path\" parameter in request body";
    private static final String ERROR_ACCEPT_POST_METHOD_ONLY = "Accept POST method only";
    private static final String ERROR_NOT_A_VALID_FOLDER = "\"%s\" is not a valid folder";
    private static final String ERROR_COULD_NOT_COMPLETELY_READ = "Could not completely read file %s";
    private static final String ERROR_LOAD_NEW_CLASS_VERSION = "Failed to load new class version: %s";
    private static final String NEW_CLASSES_LOADED = "OK";
    private static final String PATTEN_EXTRACT_PATH_PARAM = "^.*\"path\":[ ]*\"([^\"]+)\".*$";

    private static Logger log = Logger.getLogger(ClassSwapper.class.getName());

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("POST")) {
            InputStream requestBody = httpExchange.getRequestBody();
            String body = new BufferedReader(new InputStreamReader(requestBody)).readLine();
            String path = body.replaceAll(PATTEN_EXTRACT_PATH_PARAM, "$1");
            if (body.equals(path)) {
                sendResponse(httpExchange, 400, ERROR_MISSING_PATH_PARAMETER);
                return;
            }
            try {
                List<String> classFiles = listClassFiles(path);
                ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                for (String filePath : classFiles) {
                    String dottedClassName = getDottedClassName(path, filePath);
                    byte[] bytes = getBytesFromFile(filePath);
                    int res = SpringLoaded.loadNewVersionOfType(classLoader, dottedClassName, bytes);
                    if (res != 0 && res != 2) {
                        log.log(Level.WARNING, String.format("Load %s failed with code %d", dottedClassName, res));
                    }
                }
                sendResponse(httpExchange, 200, NEW_CLASSES_LOADED);
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(httpExchange, 400, String.format(ERROR_LOAD_NEW_CLASS_VERSION, e.getMessage()));
            }
        } else {
            sendResponse(httpExchange, 405, ERROR_ACCEPT_POST_METHOD_ONLY);
        }
    }

    private String getDottedClassName(String basePath, String filePath) {
        if (!basePath.endsWith("/")) {
            basePath = basePath + "/";
        }
        return filePath.substring(basePath.length(), filePath.length() - 6).replace(File.separator, ".");
    }

    private byte[] getBytesFromFile(String fileName) throws IOException {
        File file = new File(fileName);
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        int numRead, offset = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException(String.format(ERROR_COULD_NOT_COMPLETELY_READ, file.getName()));
        }
        is.close();
        return bytes;
    }

    private void sendResponse(HttpExchange httpExchange, int code, String responseString) throws IOException {
        httpExchange.sendResponseHeaders(code, responseString.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(responseString.getBytes());
        os.close();
    }

    private List<String> listClassFiles(String dir) {
        List<String> classFiles = new ArrayList<String>();
        File directory = new File(dir);
        if (!directory.isDirectory()) {
            throw new InvalidParameterException(String.format(ERROR_NOT_A_VALID_FOLDER, dir));
        }
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {
                if (file.isFile() && file.getName().endsWith(".class")) {
                    classFiles.add(file.getPath());
                } else if (file.isDirectory()) {
                    classFiles.addAll(listClassFiles(file.getPath()));
                }
            }
        }
        return classFiles;
    }

}