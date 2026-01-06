package com.thqh.enterprise_wechat_backend.controller;

import com.thqh.enterprise_wechat_backend.dto.*;
import com.thqh.enterprise_wechat_backend.entity.ChatMessage;
import com.thqh.enterprise_wechat_backend.service.ChatDataService;
import com.thqh.enterprise_wechat_backend.service.ChatMessageService;
import com.thqh.enterprise_wechat_backend.service.WeChatService;
import com.thqh.enterprise_wechat_backend.util.RSAUtil;
import com.thqh.enterprise_wechat_backend.util.WeChatSdkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.security.PrivateKey;
import java.util.Map;



/**
 * @ClassName: ChatMessageController
 * @Description:
 * @Author liubin
 * @Date 2025/3/28 14:55
 * @Version V1.0
 */


@RestController
@RequestMapping("/api/chatmessage")
@CrossOrigin(origins = "*", maxAge = 3600)//开发阶段允许跨域请求
public class ChatMessageController {
    private  final Logger logger = LoggerFactory.getLogger(ChatMessageController.class);

    private final ChatMessageService chatMessageService;

    private final WeChatService weChatService;

    private final ChatDataService chatDataService;


    public ChatMessageController(ChatMessageService chatMessageService, WeChatService weChatService, ChatDataService chatDataService) {
        this.chatMessageService = chatMessageService;
        this.weChatService = weChatService;
        this.chatDataService = chatDataService;
    }


    @PostMapping("/chatter")
    public ResponseEntity<?> getChatterListByType(@RequestBody ChatterListQueryDTO queryDTO) {
        logger.info("获取会话人列表请求参数: {}", queryDTO);
        Map<String, Object> chatterListResult = chatMessageService.getChatterListByType(queryDTO);
        logger.info("获取会话人列表响应内容: {}", ApiResponse.success(chatterListResult));
        // 返回成功响应
        return ResponseEntity.ok(ApiResponse.success(chatterListResult));
    }


    @PostMapping("/conversations")
    public ResponseEntity<?> getConversationList(@RequestBody ConversationListQueryDTO queryDTO) {
        logger.info("获取对话列表请求参数: {}", queryDTO);
        Map<String, Object> conversations = chatMessageService.getConversationList(queryDTO);
        logger.info("获取对话列表响应内容: {}", ApiResponse.success(conversations));
        // 返回成功响应
        return ResponseEntity.ok(ApiResponse.success(conversations));
    }


    @PostMapping("/detail")
    public ResponseEntity<?> getConversationDetail(@RequestBody ConversationDetailQueryDTO queryDTO) {
        logger.info("获取对话详情请求参数: {}", queryDTO);
        Map<String, Object> conversations = chatMessageService.getConversationDetail(queryDTO);
        logger.info("获取对话详情响应内容: {}", ApiResponse.success(conversations));
        // 返回成功响应
        return ResponseEntity.ok(ApiResponse.success(conversations));
    }


    @PostMapping("/chatmedia/download")
    public ResponseEntity<?> downloadChatMedia(@RequestBody MediaDownloadRequest request) {
        logger.info("多媒体附件下载请求参数: {}", request);
        byte[] fileBytes = chatMessageService.downloadChatMedia(request.getSdkfileid());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename(request.getSdkfileid()).build());

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }




    @GetMapping("/revoke_premsg")
    public ResponseEntity<?> revokePreMessage(@RequestParam  String pre_msgid) {
        logger.info("撤回消息请求参数: {}", pre_msgid);
        ChatMessage chatMessageByPreMsgid = chatMessageService.getChatMessageByPreMsgid(pre_msgid);
        return ResponseEntity.ok(ApiResponse.success(chatMessageByPreMsgid));

    }

    //用于群聊获取群成员名称和标签
    //构建的响应体需包含：from_userId,from_name,from_lable
    @GetMapping("/detail/getNameAndLable")
    public ResponseEntity<?> getGroupNameAndLable(@RequestParam  String from_userId) {
        logger.info("获取群成员名称和标签请求参数: {}", from_userId);
        Map<String, Object> groupNameAndLable = chatMessageService.getGroupNameAndLable(from_userId);
        return ResponseEntity.ok(ApiResponse.success(groupNameAndLable));

    }

    //用于群聊获取成员人数
    //构建的响应体需包含：groupMenberCount
    @GetMapping("/chatter/getgroupMenberCount")
    public ResponseEntity<ApiResponse<Integer>> getgroupMenberCount(@RequestParam  String chatId) {
        logger.info("获取群成员名称和标签请求参数: {}", chatId);
        int groupMenberCount = chatMessageService.getgroupMenberCount(chatId);
        return ResponseEntity.ok(ApiResponse.success(groupMenberCount));
    }






}
