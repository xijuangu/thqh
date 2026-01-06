package com.org.bddsserver.dto;

import lombok.Data;

/**
 * @author PY.Lu
 * @date 2025/10/27
 * @Description 上传文件数据对象
 */
@Data
public class UploadFileData {
 private String class_code;
 private String class_name;
 private String sub_code;
 private String sub_name;
 private String remark;
 private String personnel_code;

 public boolean isEmpty() {
  if ((class_code == null || class_code.trim().equals("")) && (class_name == null || class_name.trim().equals("")) && (sub_code == null || sub_code.trim().equals("")) && (sub_name == null || sub_name.trim().equals("")) && (remark == null || remark.trim().equals("")) && (personnel_code == null || personnel_code.trim().equals(""))) {
    return true;
  }
  return false;
 }
}
