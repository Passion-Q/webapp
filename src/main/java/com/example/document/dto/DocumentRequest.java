package com.example.document.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DocumentRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200")
    private String title;

    @Size(max = 50, message = "文号长度不能超过50")
    private String documentNo;

    @Size(max = 50, message = "类型长度不能超过50")
    private String type;

    @Size(max = 5000, message = "内容长度不能超过5000")
    private String content;

    @Size(max = 20, message = "优先级只能是HIGH/NORMAL/LOW")
    private String priority = "NORMAL";
}