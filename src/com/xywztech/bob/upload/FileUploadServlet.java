package com.xywztech.bob.upload;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.xywztech.bob.upload.FileUploadListener.FileUploadStats;
import com.xywztech.bob.upload.FileUploadListener.FileUploadStatus;

public class FileUploadServlet extends HttpServlet {

    private static final long serialVersionUID = -4671069818841645240L;    
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = "";
        String isImport = request.getParameter("isImport");
        //system.out.printlnln(null==isImport);
        try {
            StringBuilder builder = new StringBuilder();
            if(null==isImport)
                builder.append(FileTypeConstance.getUploadPath());
            else 
                builder.append(FileTypeConstance.getImportTmpPath());
            if (!builder.toString().endsWith(File.separator)) {
                builder.append(File.separator);
            }
            builder.append(File.separator);
            String saveFilePath = builder.toString();
            if (! new File(saveFilePath).exists()) {
                new File(saveFilePath).mkdir();
            }
            FileUploadListener listener = new FileUploadListener(request.getContentLength());
            request.getSession().setAttribute("FILE_UPLOAD_STATS", listener.getFileUploadStats());
            DiskFileItemFactory factory = new MonitoredDiskFileItemFactory(listener);
            factory.setRepository(new File(saveFilePath));
            ServletFileUpload upload = new ServletFileUpload(factory);
            //设置上传文件最大值，单位字节
            upload.setSizeMax(FileTypeConstance.getMaxUploadFileSize());
            @SuppressWarnings("unchecked")
            List<FileItem> items = upload.parseRequest(request);
            for (Iterator<FileItem> i = items.iterator(); i.hasNext();) {
                FileItem fileItem = i.next();
                if (!fileItem.isFormField()) {
                    fileName = FileTypeConstance.getSeqFileName() + FileTypeConstance.getExtFileName(fileItem.getName());
                    fileItem.write(new File(saveFilePath + fileName));
                }
            }
          //system.out.printlnntln(listener.getFileUploadStats().getCurrentStatus());
            response.setContentType("text/HTML");
            PrintWriter out = response.getWriter();
            /**
             * TODO 添加以下json语句可以消除form表单提交时的JS错误。
             */
            out.append("{success:true,realFileName:'"+fileName+"'}");
            out.flush();
        } catch (Exception e) {
        	FileUploadStats stats = new FileUploadListener.FileUploadStats();
            stats.setCurrentStatus(FileUploadStatus.ERROR);
            request.getSession().setAttribute("FILE_UPLOAD_STATS", stats);
            PrintWriter out = response.getWriter();
            /**
             * TODO 添加以下json语句可以消除form表单提交时的JS错误。
             */
            if("SizeLimitExceededException".equals(e.getClass().getSimpleName()))
            	out.append("{success:false,reason:'SizeLimitExceeded'}");
            else
            	out.append("{success:false,reason:'other'}");
            out.flush();
            e.printStackTrace();
        }
    }
}
